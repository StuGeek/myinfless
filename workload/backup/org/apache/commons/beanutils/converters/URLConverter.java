// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import java.net.URL;

public final class URLConverter extends AbstractConverter
{
    public URLConverter() {
    }
    
    public URLConverter(final Object defaultValue) {
        super(defaultValue);
    }
    
    protected Class getDefaultType() {
        return URL.class;
    }
    
    protected Object convertToType(final Class type, final Object value) throws Throwable {
        return new URL(value.toString());
    }
}
