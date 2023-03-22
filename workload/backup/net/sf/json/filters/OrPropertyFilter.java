// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.filters;

import net.sf.json.util.PropertyFilter;

public class OrPropertyFilter implements PropertyFilter
{
    private PropertyFilter filter1;
    private PropertyFilter filter2;
    
    public OrPropertyFilter(final PropertyFilter filter1, final PropertyFilter filter2) {
        this.filter1 = filter1;
        this.filter2 = filter2;
    }
    
    public boolean apply(final Object source, final String name, final Object value) {
        return (this.filter1 != null && this.filter1.apply(source, name, value)) || (this.filter2 != null && this.filter2.apply(source, name, value));
    }
}
