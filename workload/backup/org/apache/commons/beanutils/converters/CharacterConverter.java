// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class CharacterConverter extends AbstractConverter
{
    public CharacterConverter() {
    }
    
    public CharacterConverter(final Object defaultValue) {
        super(defaultValue);
    }
    
    protected Class getDefaultType() {
        return Character.class;
    }
    
    protected String convertToString(final Object value) {
        final String strValue = value.toString();
        return (strValue.length() == 0) ? "" : strValue.substring(0, 1);
    }
    
    protected Object convertToType(final Class type, final Object value) throws Exception {
        return new Character(value.toString().charAt(0));
    }
}
