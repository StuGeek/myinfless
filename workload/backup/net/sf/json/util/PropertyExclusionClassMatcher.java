// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import java.util.Set;

public abstract class PropertyExclusionClassMatcher
{
    public static final PropertyExclusionClassMatcher DEFAULT;
    
    public abstract Object getMatch(final Class p0, final Set p1);
    
    static {
        DEFAULT = new DefaultPropertyExclusionClassMatcher();
    }
    
    private static final class DefaultPropertyExclusionClassMatcher extends PropertyExclusionClassMatcher
    {
        public Object getMatch(final Class target, final Set set) {
            if (target != null && set != null && set.contains(target)) {
                return target;
            }
            return null;
        }
    }
}
