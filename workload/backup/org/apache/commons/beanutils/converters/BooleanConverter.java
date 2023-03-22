// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;

public final class BooleanConverter extends AbstractConverter
{
    public static final Object NO_DEFAULT;
    private String[] trueStrings;
    private String[] falseStrings;
    
    public BooleanConverter() {
        this.trueStrings = new String[] { "true", "yes", "y", "on", "1" };
        this.falseStrings = new String[] { "false", "no", "n", "off", "0" };
    }
    
    public BooleanConverter(final Object defaultValue) {
        this.trueStrings = new String[] { "true", "yes", "y", "on", "1" };
        this.falseStrings = new String[] { "false", "no", "n", "off", "0" };
        if (defaultValue != BooleanConverter.NO_DEFAULT) {
            this.setDefaultValue(defaultValue);
        }
    }
    
    public BooleanConverter(final String[] trueStrings, final String[] falseStrings) {
        this.trueStrings = new String[] { "true", "yes", "y", "on", "1" };
        this.falseStrings = new String[] { "false", "no", "n", "off", "0" };
        this.trueStrings = copyStrings(trueStrings);
        this.falseStrings = copyStrings(falseStrings);
    }
    
    public BooleanConverter(final String[] trueStrings, final String[] falseStrings, final Object defaultValue) {
        this.trueStrings = new String[] { "true", "yes", "y", "on", "1" };
        this.falseStrings = new String[] { "false", "no", "n", "off", "0" };
        this.trueStrings = copyStrings(trueStrings);
        this.falseStrings = copyStrings(falseStrings);
        if (defaultValue != BooleanConverter.NO_DEFAULT) {
            this.setDefaultValue(defaultValue);
        }
    }
    
    protected Class getDefaultType() {
        return Boolean.class;
    }
    
    protected Object convertToType(final Class type, final Object value) throws Throwable {
        final String stringValue = value.toString().toLowerCase();
        for (int i = 0; i < this.trueStrings.length; ++i) {
            if (this.trueStrings[i].equals(stringValue)) {
                return Boolean.TRUE;
            }
        }
        for (int i = 0; i < this.falseStrings.length; ++i) {
            if (this.falseStrings[i].equals(stringValue)) {
                return Boolean.FALSE;
            }
        }
        throw new ConversionException("Cna't convert value '" + value + "' to a Boolean");
    }
    
    private static String[] copyStrings(final String[] src) {
        final String[] dst = new String[src.length];
        for (int i = 0; i < src.length; ++i) {
            dst[i] = src[i].toLowerCase();
        }
        return dst;
    }
    
    static {
        NO_DEFAULT = new Object();
    }
}
