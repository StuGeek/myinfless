// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class DoubleConverter extends NumberConverter
{
    public DoubleConverter() {
        super(true);
    }
    
    public DoubleConverter(final Object defaultValue) {
        super(true, defaultValue);
    }
    
    protected Class getDefaultType() {
        return Double.class;
    }
}
