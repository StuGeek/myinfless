// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.primitive;

import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class IntMorpher extends AbstractIntegerMorpher
{
    private int defaultValue;
    
    public IntMorpher() {
    }
    
    public IntMorpher(final int defaultValue) {
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
        if (!(obj instanceof IntMorpher)) {
            return false;
        }
        final IntMorpher other = (IntMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public int getDefaultValue() {
        return this.defaultValue;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        if (this.isUseDefault()) {
            builder.append(this.getDefaultValue());
        }
        return builder.toHashCode();
    }
    
    public int morph(final Object value) {
        if (value == null) {
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            throw new MorphException("value is null");
        }
        else {
            if (value instanceof Number) {
                return ((Number)value).intValue();
            }
            int i = 0;
            try {
                i = Integer.parseInt(this.getIntegerValue(value));
                return i;
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
        return Integer.TYPE;
    }
}
