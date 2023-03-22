// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

public final class ClassConverter extends AbstractConverter
{
    public ClassConverter() {
    }
    
    public ClassConverter(final Object defaultValue) {
        super(defaultValue);
    }
    
    protected Class getDefaultType() {
        return Class.class;
    }
    
    protected String convertToString(final Object value) {
        return (value instanceof Class) ? ((Class)value).getName() : value.toString();
    }
    
    protected Object convertToType(final Class type, final Object value) throws Throwable {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            try {
                return classLoader.loadClass(value.toString());
            }
            catch (ClassNotFoundException ex) {}
        }
        classLoader = ClassConverter.class.getClassLoader();
        return classLoader.loadClass(value.toString());
    }
}
