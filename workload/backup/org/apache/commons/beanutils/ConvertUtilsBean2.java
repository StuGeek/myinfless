// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

public class ConvertUtilsBean2 extends ConvertUtilsBean
{
    public String convert(final Object value) {
        return (String)this.convert(value, String.class);
    }
    
    public Object convert(final String value, final Class clazz) {
        return this.convert((Object)value, clazz);
    }
    
    public Object convert(final String[] value, final Class clazz) {
        return this.convert((Object)value, clazz);
    }
}
