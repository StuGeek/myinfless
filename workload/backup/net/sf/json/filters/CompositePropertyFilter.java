// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.filters;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.util.PropertyFilter;

public class CompositePropertyFilter implements PropertyFilter
{
    private List filters;
    
    public CompositePropertyFilter() {
        this(null);
    }
    
    public CompositePropertyFilter(final List filters) {
        this.filters = new ArrayList();
        if (filters != null) {
            for (final Object filter : filters) {
                if (filter instanceof PropertyFilter) {
                    this.filters.add(filter);
                }
            }
        }
    }
    
    public void addPropertyFilter(final PropertyFilter filter) {
        if (filter != null) {
            this.filters.add(filter);
        }
    }
    
    public boolean apply(final Object source, final String name, final Object value) {
        for (final PropertyFilter filter : this.filters) {
            if (filter.apply(source, name, value)) {
                return true;
            }
        }
        return false;
    }
    
    public void removePropertyFilter(final PropertyFilter filter) {
        if (filter != null) {
            this.filters.remove(filter);
        }
    }
}
