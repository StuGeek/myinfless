// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.array;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.primitive.CharMorpher;
import java.lang.reflect.Array;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class CharArrayMorpher extends AbstractArrayMorpher
{
    private static final Class CHAR_ARRAY_CLASS;
    private char defaultValue;
    static /* synthetic */ Class class$0;
    
    static {
        Class class$0;
        if ((class$0 = CharArrayMorpher.class$0) == null) {
            try {
                class$0 = (CharArrayMorpher.class$0 = Class.forName("[C"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        CHAR_ARRAY_CLASS = class$0;
    }
    
    public CharArrayMorpher() {
        super(false);
    }
    
    public CharArrayMorpher(final char defaultValue) {
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
        if (!(obj instanceof CharArrayMorpher)) {
            return false;
        }
        final CharArrayMorpher other = (CharArrayMorpher)obj;
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
    
    public Object morph(final Object array) {
        if (array == null) {
            return null;
        }
        if (CharArrayMorpher.CHAR_ARRAY_CLASS.isAssignableFrom(array.getClass())) {
            return array;
        }
        if (array.getClass().isArray()) {
            final int length = Array.getLength(array);
            final int dims = this.getDimensions(array.getClass());
            final int[] dimensions = this.createDimensions(dims, length);
            final Object result = Array.newInstance(Character.TYPE, dimensions);
            final CharMorpher morpher = this.isUseDefault() ? new CharMorpher(this.defaultValue) : new CharMorpher();
            if (dims == 1) {
                for (int index = 0; index < length; ++index) {
                    Array.set(result, index, new Character(morpher.morph(Array.get(array, index))));
                }
            }
            else {
                for (int index = 0; index < length; ++index) {
                    Array.set(result, index, this.morph(Array.get(array, index)));
                }
            }
            return result;
        }
        throw new MorphException("argument is not an array: " + array.getClass());
    }
    
    public Class morphsTo() {
        return CharArrayMorpher.CHAR_ARRAY_CLASS;
    }
}
