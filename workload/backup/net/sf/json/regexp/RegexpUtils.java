// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.regexp;

public class RegexpUtils
{
    private static String javaVersion;
    
    public static RegexpMatcher getMatcher(final String pattern) {
        if (isJDK13()) {
            return new Perl5RegexpMatcher(pattern);
        }
        return new JdkRegexpMatcher(pattern);
    }
    
    public static RegexpMatcher getMatcher(final String pattern, final boolean multiline) {
        if (isJDK13()) {
            return new Perl5RegexpMatcher(pattern, true);
        }
        return new JdkRegexpMatcher(pattern, true);
    }
    
    public static boolean isJDK13() {
        return RegexpUtils.javaVersion.indexOf("1.3") != -1;
    }
    
    private RegexpUtils() {
    }
    
    static {
        RegexpUtils.javaVersion = "1.3.1";
        RegexpUtils.javaVersion = System.getProperty("java.version");
    }
}
