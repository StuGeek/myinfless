// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class ByteConverter extends NumberConverter
{
    public ByteConverter() {
        super(false);
    }
    
    public ByteConverter(final Object defaultValue) {
        super(false, defaultValue);
    }
    
    protected Class getDefaultType() {
        return Byte.class;
    }
}
