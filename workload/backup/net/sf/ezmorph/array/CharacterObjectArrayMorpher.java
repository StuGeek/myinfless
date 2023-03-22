// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.array;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.primitive.CharMorpher;
import java.lang.reflect.Array;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class CharacterObjectArrayMorpher extends AbstractArrayMorpher
{
    private static final Class CHARACTER_OBJECT_ARRAY_CLASS;
    private Character defaultValue;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    
    static {
        Class class$0;
        if ((class$0 = CharacterObjectArrayMorpher.class$0) == null) {
            try {
                class$0 = (CharacterObjectArrayMorpher.class$0 = Class.forName("[Ljava.lang.Character;"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        CHARACTER_OBJECT_ARRAY_CLASS = class$0;
    }
    
    public CharacterObjectArrayMorpher() {
        super(false);
    }
    
    public CharacterObjectArrayMorpher(final Character defaultValue) {
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
        if (!(obj instanceof CharacterObjectArrayMorpher)) {
            return false;
        }
        final CharacterObjectArrayMorpher other = (CharacterObjectArrayMorpher)obj;
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
    
    public Object morph(final Object array) {
        if (array == null) {
            return null;
        }
        if (CharacterObjectArrayMorpher.CHARACTER_OBJECT_ARRAY_CLASS.isAssignableFrom(array.getClass())) {
            return array;
        }
        if (array.getClass().isArray()) {
            final int length = Array.getLength(array);
            final int dims = this.getDimensions(array.getClass());
            final int[] dimensions = this.createDimensions(dims, length);
            Class class$1;
            if ((class$1 = CharacterObjectArrayMorpher.class$1) == null) {
                try {
                    class$1 = (CharacterObjectArrayMorpher.class$1 = Class.forName("java.lang.Character"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            final Object result = Array.newInstance(class$1, dimensions);
            if (dims == 1) {
                CharMorpher morpher = null;
                if (this.isUseDefault()) {
                    if (this.defaultValue == null) {
                        for (int index = 0; index < length; ++index) {
                            Array.set(result, index, null);
                        }
                        return result;
                    }
                    morpher = new CharMorpher(this.defaultValue);
                }
                else {
                    morpher = new CharMorpher();
                }
                for (int index = 0; index < length; ++index) {
                    Array.set(result, index, new Character(morpher.morph(Array.get(array, index))));
                }
            }
            else {
                for (int index2 = 0; index2 < length; ++index2) {
                    Array.set(result, index2, this.morph(Array.get(array, index2)));
                }
            }
            return result;
        }
        throw new MorphException("argument is not an array: " + array.getClass());
    }
    
    public Class morphsTo() {
        return CharacterObjectArrayMorpher.CHARACTER_OBJECT_ARRAY_CLASS;
    }
}
