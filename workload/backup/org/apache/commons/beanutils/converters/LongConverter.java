// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class LongConverter extends NumberConverter
{
    public LongConverter() {
        super(false);
    }
    
    public LongConverter(final Object defaultValue) {
        super(false, defaultValue);
    }
    
    protected Class getDefaultType() {
        return Long.class;
    }
}
