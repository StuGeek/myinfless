// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.primitive;

import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class BooleanMorpher extends AbstractPrimitiveMorpher
{
    private boolean defaultValue;
    
    public BooleanMorpher() {
    }
    
    public BooleanMorpher(final boolean defaultValue) {
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
        if (!(obj instanceof BooleanMorpher)) {
            return false;
        }
        final BooleanMorpher other = (BooleanMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public boolean getDefaultValue() {
        return this.defaultValue;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        if (this.isUseDefault()) {
            builder.append(this.getDefaultValue());
        }
        return builder.toHashCode();
    }
    
    public boolean morph(final Object value) {
        if (value == null) {
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            throw new MorphException("value is null");
        }
        else {
            if (value instanceof Boolean) {
                return (boolean)value;
            }
            if (value instanceof Number) {
                if (value instanceof Double && (Double.isInfinite(((Number)value).doubleValue()) || Double.isNaN(((Number)value).doubleValue()))) {
                    return true;
                }
                if (value instanceof Float && (Float.isInfinite(((Number)value).floatValue()) || Float.isNaN(((Number)value).floatValue()))) {
                    return true;
                }
                final long l = ((Number)value).longValue();
                return l != 0L;
            }
            else {
                final String s = String.valueOf(value);
                if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("on")) {
                    return true;
                }
                if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no") || s.equalsIgnoreCase("off")) {
                    return false;
                }
                if (this.isUseDefault()) {
                    return this.defaultValue;
                }
                throw new MorphException("Can't morph value: " + value);
            }
        }
    }
    
    public Class morphsTo() {
        return Boolean.TYPE;
    }
}
