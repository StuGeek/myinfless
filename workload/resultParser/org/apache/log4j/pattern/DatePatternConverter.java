// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import java.text.ParsePosition;
import java.text.FieldPosition;
import java.util.Date;
import org.apache.log4j.spi.LoggingEvent;
import java.text.DateFormat;
import java.util.TimeZone;
import org.apache.log4j.helpers.LogLog;
import java.text.SimpleDateFormat;

public final class DatePatternConverter extends LoggingEventPatternConverter
{
    private static final String ABSOLUTE_FORMAT = "ABSOLUTE";
    private static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";
    private static final String DATE_AND_TIME_FORMAT = "DATE";
    private static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";
    private static final String ISO8601_FORMAT = "ISO8601";
    private static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
    private final CachedDateFormat df;
    
    private DatePatternConverter(final String[] options) {
        super("Date", "date");
        String patternOption;
        if (options == null || options.length == 0) {
            patternOption = null;
        }
        else {
            patternOption = options[0];
        }
        String pattern;
        if (patternOption == null || patternOption.equalsIgnoreCase("ISO8601")) {
            pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        }
        else if (patternOption.equalsIgnoreCase("ABSOLUTE")) {
            pattern = "HH:mm:ss,SSS";
        }
        else if (patternOption.equalsIgnoreCase("DATE")) {
            pattern = "dd MMM yyyy HH:mm:ss,SSS";
        }
        else {
            pattern = patternOption;
        }
        int maximumCacheValidity = 1000;
        DateFormat simpleFormat = null;
        try {
            simpleFormat = new SimpleDateFormat(pattern);
            maximumCacheValidity = CachedDateFormat.getMaximumCacheValidity(pattern);
        }
        catch (IllegalArgumentException e) {
            LogLog.warn("Could not instantiate SimpleDateFormat with pattern " + patternOption, e);
            simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        }
        if (options != null && options.length > 1) {
            final TimeZone tz = TimeZone.getTimeZone(options[1]);
            simpleFormat.setTimeZone(tz);
        }
        else {
            simpleFormat = new DefaultZoneDateFormat(simpleFormat);
        }
        this.df = new CachedDateFormat(simpleFormat, maximumCacheValidity);
    }
    
    public static DatePatternConverter newInstance(final String[] options) {
        return new DatePatternConverter(options);
    }
    
    public void format(final LoggingEvent event, final StringBuffer output) {
        synchronized (this) {
            this.df.format(event.timeStamp, output);
        }
    }
    
    public void format(final Object obj, final StringBuffer output) {
        if (obj instanceof Date) {
            this.format((Date)obj, output);
        }
        super.format(obj, output);
    }
    
    public void format(final Date date, final StringBuffer toAppendTo) {
        synchronized (this) {
            this.df.format(date.getTime(), toAppendTo);
        }
    }
    
    private static class DefaultZoneDateFormat extends DateFormat
    {
        private static final long serialVersionUID = 1L;
        private final DateFormat dateFormat;
        
        public DefaultZoneDateFormat(final DateFormat format) {
            this.dateFormat = format;
        }
        
        public StringBuffer format(final Date date, final StringBuffer toAppendTo, final FieldPosition fieldPosition) {
            this.dateFormat.setTimeZone(TimeZone.getDefault());
            return this.dateFormat.format(date, toAppendTo, fieldPosition);
        }
        
        public Date parse(final String source, final ParsePosition pos) {
            this.dateFormat.setTimeZone(TimeZone.getDefault());
            return this.dateFormat.parse(source, pos);
        }
    }
}
