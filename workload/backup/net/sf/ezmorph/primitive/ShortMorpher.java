// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.primitive;

import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class ShortMorpher extends AbstractIntegerMorpher
{
    private short defaultValue;
    
    public ShortMorpher() {
    }
    
    public ShortMorpher(final short defaultValue) {
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
        if (!(obj instanceof ShortMorpher)) {
            return false;
        }
        final ShortMorpher other = (ShortMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public short getDefaultValue() {
        return this.defaultValue;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        if (this.isUseDefault()) {
            builder.append(this.getDefaultValue());
        }
        return builder.toHashCode();
    }
    
    public short morph(final Object value) {
        if (value == null) {
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            throw new MorphException("value is null");
        }
        else {
            if (value instanceof Number) {
                return ((Number)value).shortValue();
            }
            short i = 0;
            try {
                i = Short.parseShort(this.getIntegerValue(value));
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
        return Short.TYPE;
    }
}
