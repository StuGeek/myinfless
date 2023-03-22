// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.filters;

import net.sf.json.util.PropertyFilter;

public class NotPropertyFilter implements PropertyFilter
{
    private PropertyFilter filter;
    
    public NotPropertyFilter(final PropertyFilter filter) {
        this.filter = filter;
    }
    
    public boolean apply(final Object source, final String name, final Object value) {
        return this.filter != null && !this.filter.apply(source, name, value);
    }
}
