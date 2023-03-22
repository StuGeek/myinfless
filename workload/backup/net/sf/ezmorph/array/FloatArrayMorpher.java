// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.array;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.primitive.FloatMorpher;
import java.lang.reflect.Array;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class FloatArrayMorpher extends AbstractArrayMorpher
{
    private static final Class FLOAT_ARRAY_CLASS;
    private float defaultValue;
    static /* synthetic */ Class class$0;
    
    static {
        Class class$0;
        if ((class$0 = FloatArrayMorpher.class$0) == null) {
            try {
                class$0 = (FloatArrayMorpher.class$0 = Class.forName("[F"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        FLOAT_ARRAY_CLASS = class$0;
    }
    
    public FloatArrayMorpher() {
        super(false);
    }
    
    public FloatArrayMorpher(final float defaultValue) {
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
        if (!(obj instanceof FloatArrayMorpher)) {
            return false;
        }
        final FloatArrayMorpher other = (FloatArrayMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public float getDefaultValue() {
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
        if (FloatArrayMorpher.FLOAT_ARRAY_CLASS.isAssignableFrom(array.getClass())) {
            return array;
        }
        if (array.getClass().isArray()) {
            final int length = Array.getLength(array);
            final int dims = this.getDimensions(array.getClass());
            final int[] dimensions = this.createDimensions(dims, length);
            final Object result = Array.newInstance(Float.TYPE, dimensions);
            final FloatMorpher morpher = this.isUseDefault() ? new FloatMorpher(this.defaultValue) : new FloatMorpher();
            if (dims == 1) {
                for (int index = 0; index < length; ++index) {
                    Array.set(result, index, new Float(morpher.morph(Array.get(array, index))));
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
        return FloatArrayMorpher.FLOAT_ARRAY_CLASS;
    }
}
