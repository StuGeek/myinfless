// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import java.io.File;

public final class FileConverter extends AbstractConverter
{
    public FileConverter() {
    }
    
    public FileConverter(final Object defaultValue) {
        super(defaultValue);
    }
    
    protected Class getDefaultType() {
        return File.class;
    }
    
    protected Object convertToType(final Class type, final Object value) throws Throwable {
        return new File(value.toString());
    }
}
