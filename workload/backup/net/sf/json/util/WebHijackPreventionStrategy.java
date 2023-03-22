// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

public abstract class WebHijackPreventionStrategy
{
    public static final WebHijackPreventionStrategy COMMENTS;
    public static final WebHijackPreventionStrategy INFINITE_LOOP;
    
    public abstract String protect(final String p0);
    
    static {
        COMMENTS = new CommentWebHijackPreventionStrategy();
        INFINITE_LOOP = new InfiniteLoopWebHijackPreventionStrategy();
    }
    
    private static final class CommentWebHijackPreventionStrategy extends WebHijackPreventionStrategy
    {
        public String protect(final String str) {
            return "/*" + str + "*/";
        }
    }
    
    private static final class InfiniteLoopWebHijackPreventionStrategy extends WebHijackPreventionStrategy
    {
        public String protect(final String str) {
            return "while(1);" + str;
        }
    }
}
