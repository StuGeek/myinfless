// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.filters;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.util.PropertyFilter;

public abstract class MappingPropertyFilter implements PropertyFilter
{
    private Map filters;
    
    public MappingPropertyFilter() {
        this(null);
    }
    
    public MappingPropertyFilter(final Map filters) {
        this.filters = new HashMap();
        if (filters != null) {
            for (final Map.Entry entry : filters.entrySet()) {
                final Object key = entry.getKey();
                final Object filter = entry.getValue();
                if (filter instanceof PropertyFilter) {
                    this.filters.put(key, filter);
                }
            }
        }
    }
    
    public void addPropertyFilter(final Object target, final PropertyFilter filter) {
        if (filter != null) {
            this.filters.put(target, filter);
        }
    }
    
    public boolean apply(final Object source, final String name, final Object value) {
        for (final Map.Entry entry : this.filters.entrySet()) {
            final Object key = entry.getKey();
            if (this.keyMatches(key, source, name, value)) {
                final PropertyFilter filter = entry.getValue();
                return filter.apply(source, name, value);
            }
        }
        return false;
    }
    
    public void removePropertyFilter(final Object target) {
        if (target != null) {
            this.filters.remove(target);
        }
    }
    
    protected abstract boolean keyMatches(final Object p0, final Object p1, final String p2, final Object p3);
}
