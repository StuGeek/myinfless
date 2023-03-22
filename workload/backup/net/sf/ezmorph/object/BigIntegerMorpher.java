// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import java.util.Locale;
import java.math.BigDecimal;
import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import java.math.BigInteger;

public final class BigIntegerMorpher extends AbstractObjectMorpher
{
    private BigInteger defaultValue;
    static /* synthetic */ Class class$0;
    
    public BigIntegerMorpher() {
    }
    
    public BigIntegerMorpher(final BigInteger defaultValue) {
        super(true);
        this.defaultValue = defaultValue;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BigIntegerMorpher)) {
            return false;
        }
        final BigIntegerMorpher other = (BigIntegerMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public BigInteger getDefaultValue() {
        return this.defaultValue;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        if (this.isUseDefault()) {
            builder.append(this.getDefaultValue());
        }
        return builder.toHashCode();
    }
    
    public Object morph(final Object value) {
        if (value instanceof BigInteger) {
            return value;
        }
        if (value == null) {
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            return null;
        }
        else {
            if (value instanceof Number) {
                if (value instanceof Float) {
                    final Float f = (Float)value;
                    if (f.isInfinite() || f.isNaN()) {
                        throw new MorphException("BigInteger can not be infinite or NaN");
                    }
                }
                else if (value instanceof Double) {
                    final Double d = (Double)value;
                    if (d.isInfinite() || d.isNaN()) {
                        throw new MorphException("BigInteger can not be infinite or NaN");
                    }
                }
                else if (value instanceof BigDecimal) {
                    return ((BigDecimal)value).toBigInteger();
                }
                return BigInteger.valueOf(((Number)value).longValue());
            }
            try {
                final String str = this.getIntegerValue(value);
                if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                    return null;
                }
                return new BigInteger(str);
            }
            catch (NumberFormatException nfe) {
                if (this.isUseDefault()) {
                    return this.defaultValue;
                }
                throw new MorphException(nfe);
            }
        }
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = BigIntegerMorpher.class$0) == null) {
            try {
                class$0 = (BigIntegerMorpher.class$0 = Class.forName("java.math.BigInteger"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
    
    protected String getIntegerValue(final Object obj) {
        final Locale defaultLocale = Locale.getDefault();
        String str = null;
        try {
            Locale.setDefault(Locale.US);
            str = String.valueOf(obj).trim();
        }
        finally {
            Locale.setDefault(defaultLocale);
        }
        Locale.setDefault(defaultLocale);
        final int index = str.indexOf(".");
        if (index != -1) {
            str = str.substring(0, index);
        }
        return str;
    }
}
