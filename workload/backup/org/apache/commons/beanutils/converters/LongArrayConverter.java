// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import java.util.List;
import org.apache.commons.beanutils.ConversionException;

public final class LongArrayConverter extends AbstractArrayConverter
{
    private static final long[] MODEL;
    
    public LongArrayConverter() {
        this.defaultValue = null;
        this.useDefault = false;
    }
    
    public LongArrayConverter(final Object defaultValue) {
        this.defaultValue = defaultValue;
        this.useDefault = true;
    }
    
    public Object convert(final Class type, final Object value) {
        if (value == null) {
            if (this.useDefault) {
                return this.defaultValue;
            }
            throw new ConversionException("No value specified");
        }
        else {
            if (LongArrayConverter.MODEL.getClass() == value.getClass()) {
                return value;
            }
            if (LongArrayConverter.strings.getClass() == value.getClass()) {
                try {
                    final String[] values = (String[])value;
                    final long[] results = new long[values.length];
                    for (int i = 0; i < values.length; ++i) {
                        results[i] = Long.parseLong(values[i]);
                    }
                    return results;
                }
                catch (Exception e) {
                    if (this.useDefault) {
                        return this.defaultValue;
                    }
                    throw new ConversionException(value.toString(), e);
                }
            }
            try {
                final List list = this.parseElements(value.toString());
                final long[] results = new long[list.size()];
                for (int i = 0; i < results.length; ++i) {
                    results[i] = Long.parseLong(list.get(i));
                }
                return results;
            }
            catch (Exception e) {
                if (this.useDefault) {
                    return this.defaultValue;
                }
                throw new ConversionException(value.toString(), e);
            }
        }
    }
    
    static {
        MODEL = new long[0];
    }
}
