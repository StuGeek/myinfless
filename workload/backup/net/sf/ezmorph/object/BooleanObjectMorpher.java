// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class BooleanObjectMorpher extends AbstractObjectMorpher
{
    private Boolean defaultValue;
    static /* synthetic */ Class class$0;
    
    public BooleanObjectMorpher() {
    }
    
    public BooleanObjectMorpher(final Boolean defaultValue) {
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
        if (!(obj instanceof BooleanObjectMorpher)) {
            return false;
        }
        final BooleanObjectMorpher other = (BooleanObjectMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public Boolean getDefaultValue() {
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
        if (value == null) {
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            throw new MorphException("value is null");
        }
        else {
            if (value instanceof Boolean) {
                return value;
            }
            final String s = String.valueOf(value);
            if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("on")) {
                return Boolean.TRUE;
            }
            if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no") || s.equalsIgnoreCase("off")) {
                return Boolean.FALSE;
            }
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            throw new MorphException("Can't morph value: " + value);
        }
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = BooleanObjectMorpher.class$0) == null) {
            try {
                class$0 = (BooleanObjectMorpher.class$0 = Class.forName("java.lang.Boolean"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
}
