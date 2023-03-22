// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import java.sql.Time;
import java.text.DateFormat;
import java.util.TimeZone;
import java.util.Locale;

public final class SqlTimeConverter extends DateTimeConverter
{
    public SqlTimeConverter() {
    }
    
    public SqlTimeConverter(final Object defaultValue) {
        super(defaultValue);
    }
    
    protected Class getDefaultType() {
        return Time.class;
    }
    
    protected DateFormat getFormat(final Locale locale, final TimeZone timeZone) {
        DateFormat format = null;
        if (locale == null) {
            format = DateFormat.getTimeInstance(3);
        }
        else {
            format = DateFormat.getTimeInstance(3, locale);
        }
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        return format;
    }
}
