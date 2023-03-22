// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class StringConverter extends AbstractConverter
{
    public StringConverter() {
    }
    
    public StringConverter(final Object defaultValue) {
        super(defaultValue);
    }
    
    protected Class getDefaultType() {
        return String.class;
    }
    
    protected Object convertToType(final Class type, final Object value) throws Throwable {
        return value.toString();
    }
}
