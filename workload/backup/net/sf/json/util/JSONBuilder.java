// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import java.io.IOException;
import net.sf.json.JSONException;
import java.io.Writer;

public class JSONBuilder
{
    private static final int MAXDEPTH = 20;
    private boolean comma;
    protected char mode;
    private char[] stack;
    private int top;
    protected Writer writer;
    
    public JSONBuilder(final Writer w) {
        this.comma = false;
        this.mode = 'i';
        this.stack = new char[20];
        this.top = 0;
        this.writer = w;
    }
    
    private JSONBuilder append(final String s) {
        if (s == null) {
            throw new JSONException("Null pointer");
        }
        if (this.mode != 'o') {
            if (this.mode != 'a') {
                throw new JSONException("Value out of sequence.");
            }
        }
        try {
            if (this.comma && this.mode == 'a') {
                this.writer.write(44);
            }
            this.writer.write(s);
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
        if (this.mode == 'o') {
            this.mode = 'k';
        }
        this.comma = true;
        return this;
    }
    
    public JSONBuilder array() {
        if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
            this.push('a');
            this.append("[");
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced array.");
    }
    
    private JSONBuilder end(final char m, final char c) {
        if (this.mode != m) {
            throw new JSONException((m == 'o') ? "Misplaced endObject." : "Misplaced endArray.");
        }
        this.pop(m);
        try {
            this.writer.write(c);
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
        this.comma = true;
        return this;
    }
    
    public JSONBuilder endArray() {
        return this.end('a', ']');
    }
    
    public JSONBuilder endObject() {
        return this.end('k', '}');
    }
    
    public JSONBuilder key(final String s) {
        if (s == null) {
            throw new JSONException("Null key.");
        }
        if (this.mode == 'k') {
            try {
                if (this.comma) {
                    this.writer.write(44);
                }
                this.writer.write(JSONUtils.quote(s));
                this.writer.write(58);
                this.comma = false;
                this.mode = 'o';
                return this;
            }
            catch (IOException e) {
                throw new JSONException(e);
            }
        }
        throw new JSONException("Misplaced key.");
    }
    
    public JSONBuilder object() {
        if (this.mode == 'i') {
            this.mode = 'o';
        }
        if (this.mode == 'o' || this.mode == 'a') {
            this.append("{");
            this.push('k');
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced object.");
    }
    
    private void pop(final char c) {
        if (this.top <= 0 || this.stack[this.top - 1] != c) {
            throw new JSONException("Nesting error.");
        }
        --this.top;
        this.mode = ((this.top == 0) ? 'd' : this.stack[this.top - 1]);
    }
    
    private void push(final char c) {
        if (this.top >= 20) {
            throw new JSONException("Nesting too deep.");
        }
        this.stack[this.top] = c;
        this.mode = c;
        ++this.top;
    }
    
    public JSONBuilder value(final boolean b) {
        return this.append(b ? "true" : "false");
    }
    
    public JSONBuilder value(final double d) {
        return this.value(new Double(d));
    }
    
    public JSONBuilder value(final long l) {
        return this.append(Long.toString(l));
    }
    
    public JSONBuilder value(final Object o) {
        return this.append(JSONUtils.valueToString(o));
    }
}
