// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.parser;

import java.util.Locale;
import java.util.Arrays;
import java.io.IOException;
import org.jsoup.UncheckedIOException;
import java.io.StringReader;
import org.jsoup.helper.Validate;
import java.io.Reader;

public final class CharacterReader
{
    static final char EOF = '\uffff';
    private static final int maxStringCacheLen = 12;
    static final int maxBufferLen = 32768;
    private static final int readAheadLimit = 24576;
    private final char[] charBuf;
    private final Reader reader;
    private int bufLength;
    private int bufSplitPoint;
    private int bufPos;
    private int readerPos;
    private int bufMark;
    private final String[] stringCache;
    
    public CharacterReader(final Reader input, final int sz) {
        this.stringCache = new String[512];
        Validate.notNull(input);
        Validate.isTrue(input.markSupported());
        this.reader = input;
        this.charBuf = new char[(sz > 32768) ? 32768 : sz];
        this.bufferUp();
    }
    
    public CharacterReader(final Reader input) {
        this(input, 32768);
    }
    
    public CharacterReader(final String input) {
        this(new StringReader(input), input.length());
    }
    
    private void bufferUp() {
        if (this.bufPos < this.bufSplitPoint) {
            return;
        }
        try {
            this.reader.skip(this.bufPos);
            this.reader.mark(32768);
            final int read = this.reader.read(this.charBuf);
            this.reader.reset();
            if (read != -1) {
                this.bufLength = read;
                this.readerPos += this.bufPos;
                this.bufPos = 0;
                this.bufMark = 0;
                this.bufSplitPoint = ((this.bufLength > 24576) ? 24576 : this.bufLength);
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public int pos() {
        return this.readerPos + this.bufPos;
    }
    
    public boolean isEmpty() {
        this.bufferUp();
        return this.bufPos >= this.bufLength;
    }
    
    private boolean isEmptyNoBufferUp() {
        return this.bufPos >= this.bufLength;
    }
    
    public char current() {
        this.bufferUp();
        return this.isEmptyNoBufferUp() ? '\uffff' : this.charBuf[this.bufPos];
    }
    
    char consume() {
        this.bufferUp();
        final char val = this.isEmptyNoBufferUp() ? '\uffff' : this.charBuf[this.bufPos];
        ++this.bufPos;
        return val;
    }
    
    void unconsume() {
        --this.bufPos;
    }
    
    public void advance() {
        ++this.bufPos;
    }
    
    void mark() {
        this.bufMark = this.bufPos;
    }
    
    void rewindToMark() {
        this.bufPos = this.bufMark;
    }
    
    int nextIndexOf(final char c) {
        this.bufferUp();
        for (int i = this.bufPos; i < this.bufLength; ++i) {
            if (c == this.charBuf[i]) {
                return i - this.bufPos;
            }
        }
        return -1;
    }
    
    int nextIndexOf(final CharSequence seq) {
        this.bufferUp();
        final char startChar = seq.charAt(0);
        for (int offset = this.bufPos; offset < this.bufLength; ++offset) {
            if (startChar != this.charBuf[offset]) {
                while (++offset < this.bufLength && startChar != this.charBuf[offset]) {}
            }
            int i = offset + 1;
            final int last = i + seq.length() - 1;
            if (offset < this.bufLength && last <= this.bufLength) {
                for (int j = 1; i < last && seq.charAt(j) == this.charBuf[i]; ++i, ++j) {}
                if (i == last) {
                    return offset - this.bufPos;
                }
            }
        }
        return -1;
    }
    
    public String consumeTo(final char c) {
        final int offset = this.nextIndexOf(c);
        if (offset != -1) {
            final String consumed = cacheString(this.charBuf, this.stringCache, this.bufPos, offset);
            this.bufPos += offset;
            return consumed;
        }
        return this.consumeToEnd();
    }
    
    String consumeTo(final String seq) {
        final int offset = this.nextIndexOf(seq);
        if (offset != -1) {
            final String consumed = cacheString(this.charBuf, this.stringCache, this.bufPos, offset);
            this.bufPos += offset;
            return consumed;
        }
        return this.consumeToEnd();
    }
    
    public String consumeToAny(final char... chars) {
        this.bufferUp();
        final int start = this.bufPos;
        final int remaining = this.bufLength;
        final char[] val = this.charBuf;
    Label_0087:
        while (this.bufPos < remaining) {
            for (final char c : chars) {
                if (val[this.bufPos] == c) {
                    break Label_0087;
                }
            }
            ++this.bufPos;
        }
        return (this.bufPos > start) ? cacheString(this.charBuf, this.stringCache, start, this.bufPos - start) : "";
    }
    
    String consumeToAnySorted(final char... chars) {
        this.bufferUp();
        final int start = this.bufPos;
        final int remaining = this.bufLength;
        final char[] val = this.charBuf;
        while (this.bufPos < remaining && Arrays.binarySearch(chars, val[this.bufPos]) < 0) {
            ++this.bufPos;
        }
        return (this.bufPos > start) ? cacheString(this.charBuf, this.stringCache, start, this.bufPos - start) : "";
    }
    
    String consumeData() {
        this.bufferUp();
        final int start = this.bufPos;
        final int remaining = this.bufLength;
        final char[] val = this.charBuf;
        while (this.bufPos < remaining) {
            final char c = val[this.bufPos];
            if (c == '&' || c == '<') {
                break;
            }
            if (c == '\0') {
                break;
            }
            ++this.bufPos;
        }
        return (this.bufPos > start) ? cacheString(this.charBuf, this.stringCache, start, this.bufPos - start) : "";
    }
    
    String consumeTagName() {
        this.bufferUp();
        final int start = this.bufPos;
        final int remaining = this.bufLength;
        final char[] val = this.charBuf;
        while (this.bufPos < remaining) {
            final char c = val[this.bufPos];
            if (c == '\t' || c == '\n' || c == '\r' || c == '\f' || c == ' ' || c == '/' || c == '>') {
                break;
            }
            if (c == '\0') {
                break;
            }
            ++this.bufPos;
        }
        return (this.bufPos > start) ? cacheString(this.charBuf, this.stringCache, start, this.bufPos - start) : "";
    }
    
    String consumeToEnd() {
        this.bufferUp();
        final String data = cacheString(this.charBuf, this.stringCache, this.bufPos, this.bufLength - this.bufPos);
        this.bufPos = this.bufLength;
        return data;
    }
    
    String consumeLetterSequence() {
        this.bufferUp();
        final int start = this.bufPos;
        while (this.bufPos < this.bufLength) {
            final char c = this.charBuf[this.bufPos];
            if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z') && !Character.isLetter(c)) {
                break;
            }
            ++this.bufPos;
        }
        return cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
    }
    
    String consumeLetterThenDigitSequence() {
        this.bufferUp();
        final int start = this.bufPos;
        while (this.bufPos < this.bufLength) {
            final char c = this.charBuf[this.bufPos];
            if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z') && !Character.isLetter(c)) {
                break;
            }
            ++this.bufPos;
        }
        while (!this.isEmptyNoBufferUp()) {
            final char c = this.charBuf[this.bufPos];
            if (c < '0' || c > '9') {
                break;
            }
            ++this.bufPos;
        }
        return cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
    }
    
    String consumeHexSequence() {
        this.bufferUp();
        final int start = this.bufPos;
        while (this.bufPos < this.bufLength) {
            final char c = this.charBuf[this.bufPos];
            if ((c < '0' || c > '9') && (c < 'A' || c > 'F') && (c < 'a' || c > 'f')) {
                break;
            }
            ++this.bufPos;
        }
        return cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
    }
    
    String consumeDigitSequence() {
        this.bufferUp();
        final int start = this.bufPos;
        while (this.bufPos < this.bufLength) {
            final char c = this.charBuf[this.bufPos];
            if (c < '0' || c > '9') {
                break;
            }
            ++this.bufPos;
        }
        return cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
    }
    
    boolean matches(final char c) {
        return !this.isEmpty() && this.charBuf[this.bufPos] == c;
    }
    
    boolean matches(final String seq) {
        this.bufferUp();
        final int scanLength = seq.length();
        if (scanLength > this.bufLength - this.bufPos) {
            return false;
        }
        for (int offset = 0; offset < scanLength; ++offset) {
            if (seq.charAt(offset) != this.charBuf[this.bufPos + offset]) {
                return false;
            }
        }
        return true;
    }
    
    boolean matchesIgnoreCase(final String seq) {
        this.bufferUp();
        final int scanLength = seq.length();
        if (scanLength > this.bufLength - this.bufPos) {
            return false;
        }
        for (int offset = 0; offset < scanLength; ++offset) {
            final char upScan = Character.toUpperCase(seq.charAt(offset));
            final char upTarget = Character.toUpperCase(this.charBuf[this.bufPos + offset]);
            if (upScan != upTarget) {
                return false;
            }
        }
        return true;
    }
    
    boolean matchesAny(final char... seq) {
        if (this.isEmpty()) {
            return false;
        }
        this.bufferUp();
        final char c = this.charBuf[this.bufPos];
        for (final char seek : seq) {
            if (seek == c) {
                return true;
            }
        }
        return false;
    }
    
    boolean matchesAnySorted(final char[] seq) {
        this.bufferUp();
        return !this.isEmpty() && Arrays.binarySearch(seq, this.charBuf[this.bufPos]) >= 0;
    }
    
    boolean matchesLetter() {
        if (this.isEmpty()) {
            return false;
        }
        final char c = this.charBuf[this.bufPos];
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c);
    }
    
    boolean matchesDigit() {
        if (this.isEmpty()) {
            return false;
        }
        final char c = this.charBuf[this.bufPos];
        return c >= '0' && c <= '9';
    }
    
    boolean matchConsume(final String seq) {
        this.bufferUp();
        if (this.matches(seq)) {
            this.bufPos += seq.length();
            return true;
        }
        return false;
    }
    
    boolean matchConsumeIgnoreCase(final String seq) {
        if (this.matchesIgnoreCase(seq)) {
            this.bufPos += seq.length();
            return true;
        }
        return false;
    }
    
    boolean containsIgnoreCase(final String seq) {
        final String loScan = seq.toLowerCase(Locale.ENGLISH);
        final String hiScan = seq.toUpperCase(Locale.ENGLISH);
        return this.nextIndexOf(loScan) > -1 || this.nextIndexOf(hiScan) > -1;
    }
    
    @Override
    public String toString() {
        return new String(this.charBuf, this.bufPos, this.bufLength - this.bufPos);
    }
    
    private static String cacheString(final char[] charBuf, final String[] stringCache, final int start, final int count) {
        if (count > 12) {
            return new String(charBuf, start, count);
        }
        if (count < 1) {
            return "";
        }
        int hash = 0;
        int offset = start;
        for (int i = 0; i < count; ++i) {
            hash = 31 * hash + charBuf[offset++];
        }
        final int index = hash & stringCache.length - 1;
        String cached = stringCache[index];
        if (cached == null) {
            cached = new String(charBuf, start, count);
            stringCache[index] = cached;
        }
        else {
            if (rangeEquals(charBuf, start, count, cached)) {
                return cached;
            }
            cached = new String(charBuf, start, count);
            stringCache[index] = cached;
        }
        return cached;
    }
    
    static boolean rangeEquals(final char[] charBuf, final int start, int count, final String cached) {
        if (count == cached.length()) {
            int i = start;
            int j = 0;
            while (count-- != 0) {
                if (charBuf[i++] != cached.charAt(j++)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    boolean rangeEquals(final int start, final int count, final String cached) {
        return rangeEquals(this.charBuf, start, count, cached);
    }
}
