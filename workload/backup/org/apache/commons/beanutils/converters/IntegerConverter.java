// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class IntegerConverter extends NumberConverter
{
    public IntegerConverter() {
        super(false);
    }
    
    public IntegerConverter(final Object defaultValue) {
        super(false, defaultValue);
    }
    
    protected Class getDefaultType() {
        return Integer.class;
    }
}
