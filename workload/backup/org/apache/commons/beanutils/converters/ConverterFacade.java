// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.Converter;

public final class ConverterFacade implements Converter
{
    private final Converter converter;
    
    public ConverterFacade(final Converter converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter is missing");
        }
        this.converter = converter;
    }
    
    public Object convert(final Class type, final Object value) {
        return this.converter.convert(type, value);
    }
    
    public String toString() {
        return "ConverterFacade[" + this.converter.toString() + "]";
    }
}
