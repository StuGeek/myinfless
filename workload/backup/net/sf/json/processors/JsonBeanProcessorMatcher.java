// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.processors;

import java.util.Set;

public abstract class JsonBeanProcessorMatcher
{
    public static final JsonBeanProcessorMatcher DEFAULT;
    
    public abstract Object getMatch(final Class p0, final Set p1);
    
    static {
        DEFAULT = new DefaultJsonBeanProcessorMatcher();
    }
    
    private static final class DefaultJsonBeanProcessorMatcher extends JsonBeanProcessorMatcher
    {
        public Object getMatch(final Class target, final Set set) {
            if (target != null && set != null && set.contains(target)) {
                return target;
            }
            return null;
        }
    }
}
