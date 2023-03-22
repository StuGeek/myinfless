// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdkRegexpMatcher implements RegexpMatcher
{
    private final Pattern pattern;
    
    public JdkRegexpMatcher(final String pattern) {
        this(pattern, false);
    }
    
    public JdkRegexpMatcher(final String pattern, final boolean multiline) {
        if (multiline) {
            this.pattern = Pattern.compile(pattern, 8);
        }
        else {
            this.pattern = Pattern.compile(pattern);
        }
    }
    
    public String getGroupIfMatches(final String str, final int group) {
        final Matcher matcher = this.pattern.matcher(str);
        if (matcher.matches()) {
            return matcher.group(group);
        }
        return "";
    }
    
    public boolean matches(final String str) {
        return this.pattern.matcher(str).matches();
    }
}
