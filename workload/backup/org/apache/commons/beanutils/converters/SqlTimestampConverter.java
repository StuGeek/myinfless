// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.TimeZone;
import java.util.Locale;

public final class SqlTimestampConverter extends DateTimeConverter
{
    public SqlTimestampConverter() {
    }
    
    public SqlTimestampConverter(final Object defaultValue) {
        super(defaultValue);
    }
    
    protected Class getDefaultType() {
        return Timestamp.class;
    }
    
    protected DateFormat getFormat(final Locale locale, final TimeZone timeZone) {
        DateFormat format = null;
        if (locale == null) {
            format = DateFormat.getDateTimeInstance(3, 3);
        }
        else {
            format = DateFormat.getDateTimeInstance(3, 3, locale);
        }
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        return format;
    }
}
