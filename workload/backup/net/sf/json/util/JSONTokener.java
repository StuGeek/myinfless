// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import net.sf.json.JSONException;
import org.apache.commons.lang.math.NumberUtils;
import net.sf.json.JSONNull;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.regexp.RegexpUtils;

public class JSONTokener
{
    private int myIndex;
    private String mySource;
    
    public static int dehexchar(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'W';
        }
        return -1;
    }
    
    public JSONTokener(String s) {
        this.myIndex = 0;
        if (s != null) {
            s = s.trim();
        }
        else {
            s = "";
        }
        if (s.length() > 0) {
            final char first = s.charAt(0);
            final char last = s.charAt(s.length() - 1);
            if (first == '[' && last != ']') {
                throw this.syntaxError("Found starting '[' but missing ']' at the end.");
            }
            if (first == '{' && last != '}') {
                throw this.syntaxError("Found starting '{' but missing '}' at the end.");
            }
        }
        this.mySource = s;
    }
    
    public void back() {
        if (this.myIndex > 0) {
            --this.myIndex;
        }
    }
    
    public int length() {
        if (this.mySource == null) {
            return 0;
        }
        return this.mySource.length();
    }
    
    public boolean matches(final String pattern) {
        final String str = this.mySource.substring(this.myIndex);
        return RegexpUtils.getMatcher(pattern).matches(str);
    }
    
    public boolean more() {
        return this.myIndex < this.mySource.length();
    }
    
    public char next() {
        if (this.more()) {
            final char c = this.mySource.charAt(this.myIndex);
            ++this.myIndex;
            return c;
        }
        return '\0';
    }
    
    public char next(final char c) {
        final char n = this.next();
        if (n != c) {
            throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'.");
        }
        return n;
    }
    
    public String next(final int n) {
        final int i = this.myIndex;
        final int j = i + n;
        if (j >= this.mySource.length()) {
            throw this.syntaxError("Substring bounds error");
        }
        this.myIndex += n;
        return this.mySource.substring(i, j);
    }
    
    public char nextClean() {
    Label_0154:
        while (true) {
            char c = this.next();
            if (c == '/') {
                switch (this.next()) {
                    case '/': {
                        do {
                            c = this.next();
                            if (c != '\n' && c != '\r') {
                                continue;
                            }
                            break;
                        } while (c != '\0');
                        continue;
                    }
                    case '*': {
                        while (true) {
                            c = this.next();
                            if (c == '\0') {
                                throw this.syntaxError("Unclosed comment.");
                            }
                            if (c != '*') {
                                continue;
                            }
                            if (this.next() == '/') {
                                continue Label_0154;
                            }
                            this.back();
                        }
                        break;
                    }
                    default: {
                        this.back();
                        return '/';
                    }
                }
            }
            else if (c == '#') {
                do {
                    c = this.next();
                    if (c != '\n' && c != '\r') {
                        continue;
                    }
                    break;
                } while (c != '\0');
            }
            else {
                if (c == '\0' || c > ' ') {
                    return c;
                }
                continue;
            }
        }
    }
    
    public String nextString(final char quote) {
        final StringBuffer sb = new StringBuffer();
        while (true) {
            char c = this.next();
            switch (c) {
                case '\0':
                case '\n':
                case '\r': {
                    throw this.syntaxError("Unterminated string");
                }
                case '\\': {
                    c = this.next();
                    switch (c) {
                        case 'b': {
                            sb.append('\b');
                            continue;
                        }
                        case 't': {
                            sb.append('\t');
                            continue;
                        }
                        case 'n': {
                            sb.append('\n');
                            continue;
                        }
                        case 'f': {
                            sb.append('\f');
                            continue;
                        }
                        case 'r': {
                            sb.append('\r');
                            continue;
                        }
                        case 'u': {
                            sb.append((char)Integer.parseInt(this.next(4), 16));
                            continue;
                        }
                        case 'x': {
                            sb.append((char)Integer.parseInt(this.next(2), 16));
                            continue;
                        }
                        default: {
                            sb.append(c);
                            continue;
                        }
                    }
                    break;
                }
                default: {
                    if (c == quote) {
                        return sb.toString();
                    }
                    sb.append(c);
                    continue;
                }
            }
        }
    }
    
    public String nextTo(final char d) {
        final StringBuffer sb = new StringBuffer();
        char c;
        while (true) {
            c = this.next();
            if (c == d || c == '\0' || c == '\n' || c == '\r') {
                break;
            }
            sb.append(c);
        }
        if (c != '\0') {
            this.back();
        }
        return sb.toString().trim();
    }
    
    public String nextTo(final String delimiters) {
        final StringBuffer sb = new StringBuffer();
        char c;
        while (true) {
            c = this.next();
            if (delimiters.indexOf(c) >= 0 || c == '\0' || c == '\n' || c == '\r') {
                break;
            }
            sb.append(c);
        }
        if (c != '\0') {
            this.back();
        }
        return sb.toString().trim();
    }
    
    public Object nextValue() {
        return this.nextValue(new JsonConfig());
    }
    
    public Object nextValue(final JsonConfig jsonConfig) {
        char c = this.nextClean();
        switch (c) {
            case '\"':
            case '\'': {
                return this.nextString(c);
            }
            case '{': {
                this.back();
                return JSONObject.fromObject(this, jsonConfig);
            }
            case '[': {
                this.back();
                return JSONArray.fromObject(this, jsonConfig);
            }
            default: {
                final StringBuffer sb = new StringBuffer();
                final char b = c;
                while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                    sb.append(c);
                    c = this.next();
                }
                this.back();
                final String s = sb.toString().trim();
                if (s.equals("")) {
                    throw this.syntaxError("Missing value.");
                }
                if (s.equalsIgnoreCase("true")) {
                    return Boolean.TRUE;
                }
                if (s.equalsIgnoreCase("false")) {
                    return Boolean.FALSE;
                }
                if (s.equals("null") || (jsonConfig.isJavascriptCompliant() && s.equals("undefined"))) {
                    return JSONNull.getInstance();
                }
                if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+') {
                    Label_0309: {
                        if (b == '0') {
                            Label_0293: {
                                if (s.length() > 2) {
                                    if (s.charAt(1) != 'x') {
                                        if (s.charAt(1) != 'X') {
                                            break Label_0293;
                                        }
                                    }
                                    try {
                                        return new Integer(Integer.parseInt(s.substring(2), 16));
                                    }
                                    catch (Exception e) {
                                        break Label_0309;
                                    }
                                }
                                try {
                                    return new Integer(Integer.parseInt(s, 8));
                                }
                                catch (Exception ex) {}
                            }
                        }
                        try {
                            return NumberUtils.createNumber(s);
                        }
                        catch (Exception e) {
                            return s;
                        }
                    }
                }
                if (JSONUtils.isFunctionHeader(s) || JSONUtils.isFunction(s)) {
                    return s;
                }
                switch (this.peek()) {
                    case ',':
                    case '[':
                    case ']':
                    case '{':
                    case '}': {
                        throw new JSONException("Unquotted string '" + s + "'");
                    }
                    default: {
                        return s;
                    }
                }
                break;
            }
        }
    }
    
    public char peek() {
        if (this.more()) {
            final char c = this.mySource.charAt(this.myIndex);
            return c;
        }
        return '\0';
    }
    
    public void reset() {
        this.myIndex = 0;
    }
    
    public void skipPast(final String to) {
        this.myIndex = this.mySource.indexOf(to, this.myIndex);
        if (this.myIndex < 0) {
            this.myIndex = this.mySource.length();
        }
        else {
            this.myIndex += to.length();
        }
    }
    
    public char skipTo(final char to) {
        final int index = this.myIndex;
        char c;
        do {
            c = this.next();
            if (c == '\0') {
                this.myIndex = index;
                return c;
            }
        } while (c != to);
        this.back();
        return c;
    }
    
    public JSONException syntaxError(final String message) {
        return new JSONException(message + this.toString());
    }
    
    public String toString() {
        return " at character " + this.myIndex + " of " + this.mySource;
    }
}
