// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.processors;

import java.util.Set;

public abstract class JsonValueProcessorMatcher
{
    public static final JsonValueProcessorMatcher DEFAULT;
    
    public abstract Object getMatch(final Class p0, final Set p1);
    
    static {
        DEFAULT = new DefaultJsonValueProcessorMatcher();
    }
    
    private static final class DefaultJsonValueProcessorMatcher extends JsonValueProcessorMatcher
    {
        public Object getMatch(final Class target, final Set set) {
            if (target != null && set != null && set.contains(target)) {
                return target;
            }
            return null;
        }
    }
}
