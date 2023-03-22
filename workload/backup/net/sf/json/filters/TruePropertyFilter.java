// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.filters;

import net.sf.json.util.PropertyFilter;

public class TruePropertyFilter implements PropertyFilter
{
    public boolean apply(final Object source, final String name, final Object value) {
        return true;
    }
}
