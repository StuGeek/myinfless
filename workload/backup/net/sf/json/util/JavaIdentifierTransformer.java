// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import org.apache.commons.lang.StringUtils;
import net.sf.json.JSONException;

public abstract class JavaIdentifierTransformer
{
    public static final JavaIdentifierTransformer CAMEL_CASE;
    public static final JavaIdentifierTransformer NOOP;
    public static final JavaIdentifierTransformer STRICT;
    public static final JavaIdentifierTransformer UNDERSCORE;
    public static final JavaIdentifierTransformer WHITESPACE;
    
    public abstract String transformToJavaIdentifier(final String p0);
    
    protected final String shaveOffNonJavaIdentifierStartChars(final String str) {
        String str2 = str;
        boolean ready = false;
        while (!ready) {
            if (!Character.isJavaIdentifierStart(str2.charAt(0))) {
                str2 = str2.substring(1);
                if (str2.length() == 0) {
                    throw new JSONException("Can't convert '" + str + "' to a valid Java identifier");
                }
                continue;
            }
            else {
                ready = true;
            }
        }
        return str2;
    }
    
    static {
        CAMEL_CASE = new CamelCaseJavaIdentifierTransformer();
        NOOP = new NoopJavaIdentifierTransformer();
        STRICT = new StrictJavaIdentifierTransformer();
        UNDERSCORE = new UnderscoreJavaIdentifierTransformer();
        WHITESPACE = new WhiteSpaceJavaIdentifierTransformer();
    }
    
    private static final class CamelCaseJavaIdentifierTransformer extends JavaIdentifierTransformer
    {
        public String transformToJavaIdentifier(final String str) {
            if (str == null) {
                return null;
            }
            final String str2 = this.shaveOffNonJavaIdentifierStartChars(str);
            final char[] chars = str2.toCharArray();
            int pos = 0;
            final StringBuffer buf = new StringBuffer();
            boolean toUpperCaseNextChar = false;
            while (pos < chars.length) {
                if (!Character.isJavaIdentifierPart(chars[pos]) || Character.isWhitespace(chars[pos])) {
                    toUpperCaseNextChar = true;
                }
                else if (toUpperCaseNextChar) {
                    buf.append(Character.toUpperCase(chars[pos]));
                    toUpperCaseNextChar = false;
                }
                else {
                    buf.append(chars[pos]);
                }
                ++pos;
            }
            return buf.toString();
        }
    }
    
    private static final class NoopJavaIdentifierTransformer extends JavaIdentifierTransformer
    {
        public String transformToJavaIdentifier(final String str) {
            return str;
        }
    }
    
    private static final class StrictJavaIdentifierTransformer extends JavaIdentifierTransformer
    {
        public String transformToJavaIdentifier(final String str) {
            throw new JSONException("'" + str + "' is not a valid Java identifier.");
        }
    }
    
    private static final class UnderscoreJavaIdentifierTransformer extends JavaIdentifierTransformer
    {
        public String transformToJavaIdentifier(final String str) {
            if (str == null) {
                return null;
            }
            final String str2 = this.shaveOffNonJavaIdentifierStartChars(str);
            final char[] chars = str2.toCharArray();
            int pos = 0;
            final StringBuffer buf = new StringBuffer();
            boolean toUnderScorePreviousChar = false;
            while (pos < chars.length) {
                if (!Character.isJavaIdentifierPart(chars[pos]) || Character.isWhitespace(chars[pos])) {
                    toUnderScorePreviousChar = true;
                }
                else {
                    if (toUnderScorePreviousChar) {
                        buf.append("_");
                        toUnderScorePreviousChar = false;
                    }
                    buf.append(chars[pos]);
                }
                ++pos;
            }
            if (buf.charAt(buf.length() - 1) == '_') {
                buf.deleteCharAt(buf.length() - 1);
            }
            return buf.toString();
        }
    }
    
    private static final class WhiteSpaceJavaIdentifierTransformer extends JavaIdentifierTransformer
    {
        public String transformToJavaIdentifier(final String str) {
            if (str == null) {
                return null;
            }
            String str2 = this.shaveOffNonJavaIdentifierStartChars(str);
            str2 = StringUtils.deleteWhitespace(str2);
            final char[] chars = str2.toCharArray();
            int pos = 0;
            final StringBuffer buf = new StringBuffer();
            while (pos < chars.length) {
                if (Character.isJavaIdentifierPart(chars[pos])) {
                    buf.append(chars[pos]);
                }
                ++pos;
            }
            return buf.toString();
        }
    }
}
