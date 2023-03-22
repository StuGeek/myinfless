// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.regexp;

import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;

public class Perl5RegexpMatcher implements RegexpMatcher
{
    private static final Perl5Compiler compiler;
    private Pattern pattern;
    
    public Perl5RegexpMatcher(final String pattern) {
        this(pattern, false);
    }
    
    public Perl5RegexpMatcher(final String pattern, final boolean multiline) {
        try {
            if (multiline) {
                this.pattern = Perl5RegexpMatcher.compiler.compile(pattern, 32776);
            }
            else {
                this.pattern = Perl5RegexpMatcher.compiler.compile(pattern, 32768);
            }
        }
        catch (MalformedPatternException mpe) {
            throw new NestableRuntimeException((Throwable)mpe);
        }
    }
    
    public String getGroupIfMatches(final String str, final int group) {
        final PatternMatcher matcher = (PatternMatcher)new Perl5Matcher();
        if (matcher.matches(str, this.pattern)) {
            return matcher.getMatch().group(1);
        }
        return "";
    }
    
    public boolean matches(final String str) {
        return new Perl5Matcher().matches(str, this.pattern);
    }
    
    static {
        compiler = new Perl5Compiler();
    }
}
