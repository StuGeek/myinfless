// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class ShortConverter extends NumberConverter
{
    public ShortConverter() {
        super(false);
    }
    
    public ShortConverter(final Object defaultValue) {
        super(false, defaultValue);
    }
    
    protected Class getDefaultType() {
        return Short.class;
    }
}
