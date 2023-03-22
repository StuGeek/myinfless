// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.array;

import net.sf.ezmorph.MorphException;
import java.lang.reflect.Array;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.lang.reflect.Method;
import net.sf.ezmorph.Morpher;

public final class ObjectArrayMorpher extends AbstractArrayMorpher
{
    private Morpher morpher;
    private Method morphMethod;
    private Class target;
    private Class targetArrayClass;
    static /* synthetic */ Class class$0;
    
    public ObjectArrayMorpher(final Morpher morpher) {
        super(false);
        this.setMorpher(morpher);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ObjectArrayMorpher)) {
            return false;
        }
        final ObjectArrayMorpher other = (ObjectArrayMorpher)obj;
        return this.morpher.equals(other.morpher);
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(this.morpher).toHashCode();
    }
    
    public Object morph(final Object array) {
        if (array == null) {
            return null;
        }
        if (array.getClass().isArray()) {
            final int length = Array.getLength(array);
            final int dims = this.getDimensions(array.getClass());
            final int[] dimensions = this.createDimensions(dims, length);
            final Object result = Array.newInstance(this.target, dimensions);
            if (dims == 1) {
                for (int index = 0; index < length; ++index) {
                    try {
                        final Object value = Array.get(array, index);
                        if (value != null && !this.morpher.supports(value.getClass())) {
                            throw new MorphException(value.getClass() + " is not supported");
                        }
                        final Object morphed = this.morphMethod.invoke(this.morpher, value);
                        Array.set(result, index, morphed);
                    }
                    catch (MorphException me) {
                        throw me;
                    }
                    catch (Exception e) {
                        throw new MorphException(e);
                    }
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
        return this.targetArrayClass;
    }
    
    public boolean supports(Class clazz) {
        if (clazz != null && !clazz.isArray()) {
            return false;
        }
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return this.morpher.supports(clazz);
    }
    
    private void setMorpher(final Morpher morpher) {
        if (morpher == null) {
            throw new IllegalArgumentException("morpher can not be null");
        }
        if (morpher.morphsTo().isArray()) {
            throw new IllegalArgumentException("morpher target class can not be an array");
        }
        this.morpher = morpher;
        this.targetArrayClass = Array.newInstance(morpher.morphsTo(), 1).getClass();
        this.target = morpher.morphsTo();
        try {
            final Class<? extends Morpher> class1 = morpher.getClass();
            final String name = "morph";
            final Class[] parameterTypes = { null };
            final int n = 0;
            Class class$0;
            if ((class$0 = ObjectArrayMorpher.class$0) == null) {
                try {
                    class$0 = (ObjectArrayMorpher.class$0 = Class.forName("java.lang.Object"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            parameterTypes[n] = class$0;
            this.morphMethod = class1.getDeclaredMethod(name, (Class[])parameterTypes);
        }
        catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(nsme.getMessage());
        }
    }
}
