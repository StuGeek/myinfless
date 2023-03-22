// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import java.math.BigInteger;
import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import java.math.BigDecimal;

public final class BigDecimalMorpher extends AbstractObjectMorpher
{
    private BigDecimal defaultValue;
    static /* synthetic */ Class class$0;
    
    public BigDecimalMorpher() {
    }
    
    public BigDecimalMorpher(final BigDecimal defaultValue) {
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
        if (!(obj instanceof BigDecimalMorpher)) {
            return false;
        }
        final BigDecimalMorpher other = (BigDecimalMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public BigDecimal getDefaultValue() {
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
        if (value instanceof BigDecimal) {
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
                        throw new MorphException("BigDecimal can not be infinite or NaN");
                    }
                }
                else if (value instanceof Double) {
                    final Double d = (Double)value;
                    if (d.isInfinite() || d.isNaN()) {
                        throw new MorphException("BigDecimal can not be infinite or NaN");
                    }
                }
                else if (value instanceof BigInteger) {
                    return new BigDecimal((BigInteger)value);
                }
                return new BigDecimal(((Number)value).doubleValue());
            }
            try {
                final String str = String.valueOf(value).trim();
                if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                    return null;
                }
                return new BigDecimal(str);
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
        if ((class$0 = BigDecimalMorpher.class$0) == null) {
            try {
                class$0 = (BigDecimalMorpher.class$0 = Class.forName("java.math.BigDecimal"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
}
