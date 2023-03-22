// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import java.text.ParsePosition;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import java.math.BigInteger;
import java.math.BigDecimal;
import org.apache.commons.beanutils.ConversionException;
import java.util.Calendar;
import java.util.Date;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class NumberConverter extends AbstractConverter
{
    private static final Integer ZERO;
    private static final Integer ONE;
    private String pattern;
    private boolean allowDecimals;
    private boolean useLocaleFormat;
    private Locale locale;
    
    public NumberConverter(final boolean allowDecimals) {
        this.allowDecimals = allowDecimals;
    }
    
    public NumberConverter(final boolean allowDecimals, final Object defaultValue) {
        this.allowDecimals = allowDecimals;
        this.setDefaultValue(defaultValue);
    }
    
    public boolean isAllowDecimals() {
        return this.allowDecimals;
    }
    
    public void setUseLocaleFormat(final boolean useLocaleFormat) {
        this.useLocaleFormat = useLocaleFormat;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    
    public void setPattern(final String pattern) {
        this.pattern = pattern;
        this.setUseLocaleFormat(true);
    }
    
    public Locale getLocale() {
        return this.locale;
    }
    
    public void setLocale(final Locale locale) {
        this.locale = locale;
        this.setUseLocaleFormat(true);
    }
    
    protected String convertToString(final Object value) throws Throwable {
        String result = null;
        if (this.useLocaleFormat && value instanceof Number) {
            final NumberFormat format = this.getFormat();
            format.setGroupingUsed(false);
            result = format.format(value);
            if (this.log().isDebugEnabled()) {
                this.log().debug("    Converted  to String using format '" + result + "'");
            }
        }
        else {
            result = value.toString();
            if (this.log().isDebugEnabled()) {
                this.log().debug("    Converted  to String using toString() '" + result + "'");
            }
        }
        return result;
    }
    
    protected Object convertToType(final Class targetType, final Object value) throws Throwable {
        final Class sourceType = value.getClass();
        if (value instanceof Number) {
            return this.toNumber(sourceType, targetType, (Number)value);
        }
        if (value instanceof Boolean) {
            return this.toNumber(sourceType, targetType, value ? NumberConverter.ONE : NumberConverter.ZERO);
        }
        if (value instanceof Date && Long.class.equals(targetType)) {
            return new Long(((Date)value).getTime());
        }
        if (value instanceof Calendar && Long.class.equals(targetType)) {
            return new Long(((Calendar)value).getTime().getTime());
        }
        final String stringValue = value.toString().trim();
        if (stringValue.length() == 0) {
            return this.handleMissing(targetType);
        }
        Number number = null;
        if (this.useLocaleFormat) {
            final NumberFormat format = this.getFormat();
            number = this.parse(sourceType, targetType, stringValue, format);
        }
        else {
            if (this.log().isDebugEnabled()) {
                this.log().debug("    No NumberFormat, using default conversion");
            }
            number = this.toNumber(sourceType, targetType, stringValue);
        }
        return this.toNumber(sourceType, targetType, number);
    }
    
    private Number toNumber(final Class sourceType, final Class targetType, final Number value) {
        if (targetType.equals(value.getClass())) {
            return value;
        }
        if (targetType.equals(Byte.class)) {
            final long longValue = value.longValue();
            if (longValue > 127L) {
                throw new ConversionException(this.toString(sourceType) + " value '" + value + "' is too large for " + this.toString(targetType));
            }
            if (longValue < -128L) {
                throw new ConversionException(this.toString(sourceType) + " value '" + value + "' is too small " + this.toString(targetType));
            }
            return new Byte(value.byteValue());
        }
        else if (targetType.equals(Short.class)) {
            final long longValue = value.longValue();
            if (longValue > 32767L) {
                throw new ConversionException(this.toString(sourceType) + " value '" + value + "' is too large for " + this.toString(targetType));
            }
            if (longValue < -32768L) {
                throw new ConversionException(this.toString(sourceType) + " value '" + value + "' is too small " + this.toString(targetType));
            }
            return new Short(value.shortValue());
        }
        else if (targetType.equals(Integer.class)) {
            final long longValue = value.longValue();
            if (longValue > 2147483647L) {
                throw new ConversionException(this.toString(sourceType) + " value '" + value + "' is too large for " + this.toString(targetType));
            }
            if (longValue < -2147483648L) {
                throw new ConversionException(this.toString(sourceType) + " value '" + value + "' is too small " + this.toString(targetType));
            }
            return new Integer(value.intValue());
        }
        else {
            if (targetType.equals(Long.class)) {
                return new Long(value.longValue());
            }
            if (targetType.equals(Float.class)) {
                if (value.doubleValue() > 3.4028234663852886E38) {
                    throw new ConversionException(this.toString(sourceType) + " value '" + value + "' is too large for " + this.toString(targetType));
                }
                return new Float(value.floatValue());
            }
            else {
                if (targetType.equals(Double.class)) {
                    return new Double(value.doubleValue());
                }
                if (targetType.equals(BigDecimal.class)) {
                    if (value instanceof Float || value instanceof Double) {
                        return new BigDecimal(value.toString());
                    }
                    if (value instanceof BigInteger) {
                        return new BigDecimal((BigInteger)value);
                    }
                    return BigDecimal.valueOf(value.longValue());
                }
                else {
                    if (!targetType.equals(BigInteger.class)) {
                        final String msg = this.toString(this.getClass()) + " cannot handle conversion to '" + this.toString(targetType) + "'";
                        if (this.log().isWarnEnabled()) {
                            this.log().warn("    " + msg);
                        }
                        throw new ConversionException(msg);
                    }
                    if (value instanceof BigDecimal) {
                        return ((BigDecimal)value).toBigInteger();
                    }
                    return BigInteger.valueOf(value.longValue());
                }
            }
        }
    }
    
    private Number toNumber(final Class sourceType, final Class targetType, final String value) {
        if (targetType.equals(Byte.class)) {
            return new Byte(value);
        }
        if (targetType.equals(Short.class)) {
            return new Short(value);
        }
        if (targetType.equals(Integer.class)) {
            return new Integer(value);
        }
        if (targetType.equals(Long.class)) {
            return new Long(value);
        }
        if (targetType.equals(Float.class)) {
            return new Float(value);
        }
        if (targetType.equals(Double.class)) {
            return new Double(value);
        }
        if (targetType.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }
        if (targetType.equals(BigInteger.class)) {
            return new BigInteger(value);
        }
        final String msg = this.toString(this.getClass()) + " cannot handle conversion from '" + this.toString(sourceType) + "' to '" + this.toString(targetType) + "'";
        if (this.log().isWarnEnabled()) {
            this.log().warn("    " + msg);
        }
        throw new ConversionException(msg);
    }
    
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.toString(this.getClass()));
        buffer.append("[UseDefault=");
        buffer.append(this.isUseDefault());
        buffer.append(", UseLocaleFormat=");
        buffer.append(this.useLocaleFormat);
        if (this.pattern != null) {
            buffer.append(", Pattern=");
            buffer.append(this.pattern);
        }
        if (this.locale != null) {
            buffer.append(", Locale=");
            buffer.append(this.locale);
        }
        buffer.append(']');
        return buffer.toString();
    }
    
    private NumberFormat getFormat() {
        NumberFormat format = null;
        if (this.pattern != null) {
            if (this.locale == null) {
                if (this.log().isDebugEnabled()) {
                    this.log().debug("    Using pattern '" + this.pattern + "'");
                }
                format = new DecimalFormat(this.pattern);
            }
            else {
                if (this.log().isDebugEnabled()) {
                    this.log().debug("    Using pattern '" + this.pattern + "'" + " with Locale[" + this.locale + "]");
                }
                final DecimalFormatSymbols symbols = new DecimalFormatSymbols(this.locale);
                format = new DecimalFormat(this.pattern, symbols);
            }
        }
        else if (this.locale == null) {
            if (this.log().isDebugEnabled()) {
                this.log().debug("    Using default Locale format");
            }
            format = NumberFormat.getInstance();
        }
        else {
            if (this.log().isDebugEnabled()) {
                this.log().debug("    Using Locale[" + this.locale + "] format");
            }
            format = NumberFormat.getInstance(this.locale);
        }
        if (!this.allowDecimals) {
            format.setParseIntegerOnly(true);
        }
        return format;
    }
    
    private Number parse(final Class sourceType, final Class targetType, final String value, final NumberFormat format) {
        final ParsePosition pos = new ParsePosition(0);
        final Number parsedNumber = format.parse(value, pos);
        if (pos.getErrorIndex() >= 0 || pos.getIndex() != value.length() || parsedNumber == null) {
            String msg = "Error converting from '" + this.toString(sourceType) + "' to '" + this.toString(targetType) + "'";
            if (format instanceof DecimalFormat) {
                msg = msg + " using pattern '" + ((DecimalFormat)format).toPattern() + "'";
            }
            if (this.locale != null) {
                msg = msg + " for locale=[" + this.locale + "]";
            }
            if (this.log().isDebugEnabled()) {
                this.log().debug("    " + msg);
            }
            throw new ConversionException(msg);
        }
        return parsedNumber;
    }
    
    static {
        ZERO = new Integer(0);
        ONE = new Integer(1);
    }
}
