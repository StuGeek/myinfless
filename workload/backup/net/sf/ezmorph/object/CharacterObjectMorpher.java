// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class CharacterObjectMorpher extends AbstractObjectMorpher
{
    private Character defaultValue;
    static /* synthetic */ Class class$0;
    
    public CharacterObjectMorpher() {
    }
    
    public CharacterObjectMorpher(final Character defaultValue) {
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
        if (!(obj instanceof CharacterObjectMorpher)) {
            return false;
        }
        final CharacterObjectMorpher other = (CharacterObjectMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public Character getDefaultValue() {
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
            if (value instanceof Character) {
                return value;
            }
            final String s = String.valueOf(value);
            if (s.length() > 0) {
                return new Character(s.charAt(0));
            }
            if (this.isUseDefault()) {
                return this.defaultValue;
            }
            throw new MorphException("Can't morph value: " + value);
        }
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = CharacterObjectMorpher.class$0) == null) {
            try {
                class$0 = (CharacterObjectMorpher.class$0 = Class.forName("java.lang.Character"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
}
