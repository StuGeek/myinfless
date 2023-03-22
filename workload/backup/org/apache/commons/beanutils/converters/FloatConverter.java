// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class FloatConverter extends NumberConverter
{
    public FloatConverter() {
        super(true);
    }
    
    public FloatConverter(final Object defaultValue) {
        super(true, defaultValue);
    }
    
    protected Class getDefaultType() {
        return Float.class;
    }
}
