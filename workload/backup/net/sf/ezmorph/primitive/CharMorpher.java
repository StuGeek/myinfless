// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.primitive;

import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class CharMorpher extends AbstractPrimitiveMorpher
{
    private char defaultValue;
    
    public CharMorpher() {
    }
    
    public CharMorpher(final char defaultValue) {
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
        if (!(obj instanceof CharMorpher)) {
            return false;
        }
        final CharMorpher other = (CharMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public char getDefaultValue() {
        return this.defaultValue;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        if (this.isUseDefault()) {
            builder.append(this.getDefaultValue());
        }
        return builder.toHashCode();
    }
    
    public char morph(final Object value) {
        if (value == null) {
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            throw new MorphException("value is null");
        }
        else {
            if (value instanceof Character) {
                return (char)value;
            }
            final String s = String.valueOf(value);
            if (s.length() > 0) {
                return s.charAt(0);
            }
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            throw new MorphException("Can't morph value: " + value);
        }
    }
    
    public Class morphsTo() {
        return Character.TYPE;
    }
}
