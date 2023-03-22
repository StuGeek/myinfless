// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import net.sf.ezmorph.ObjectMorpher;

public class EnumMorpher implements ObjectMorpher
{
    private Class enumClass;
    
    public EnumMorpher(final Class enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("enumClass is null");
        }
        if (!Enum.class.isAssignableFrom(enumClass)) {
            throw new IllegalArgumentException("enumClass is not an Enum class");
        }
        this.enumClass = enumClass;
    }
    
    public Object morph(final Object value) {
        if (value == null) {
            return this.enumClass.cast(null);
        }
        return Enum.valueOf((Class<Object>)this.enumClass, String.valueOf(value));
    }
    
    public Class morphsTo() {
        return this.enumClass;
    }
    
    public boolean supports(final Class clazz) {
        return String.class.isAssignableFrom(clazz);
    }
}
