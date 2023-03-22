// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.processors;

import java.util.Set;

public abstract class DefaultValueProcessorMatcher
{
    public static final DefaultValueProcessorMatcher DEFAULT;
    
    public abstract Object getMatch(final Class p0, final Set p1);
    
    static {
        DEFAULT = new DefaultDefaultValueProcessorMatcher();
    }
    
    private static final class DefaultDefaultValueProcessorMatcher extends DefaultValueProcessorMatcher
    {
        public Object getMatch(final Class target, final Set set) {
            if (target != null && set != null && set.contains(target)) {
                return target;
            }
            return null;
        }
    }
}
