// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.ObjectMorpher;

public final class ClassMorpher implements ObjectMorpher
{
    private static final ClassMorpher INSTANCE;
    static /* synthetic */ Class class$0;
    
    static {
        INSTANCE = new ClassMorpher();
    }
    
    public static ClassMorpher getInstance() {
        return ClassMorpher.INSTANCE;
    }
    
    private ClassMorpher() {
    }
    
    public boolean equals(final Object obj) {
        return ClassMorpher.INSTANCE == obj;
    }
    
    public int hashCode() {
        return 42 + this.getClass().hashCode();
    }
    
    public Object morph(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Class) {
            return value;
        }
        if ("null".equals(value)) {
            return null;
        }
        try {
            return Class.forName(value.toString());
        }
        catch (Exception e) {
            throw new MorphException(e);
        }
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = ClassMorpher.class$0) == null) {
            try {
                class$0 = (ClassMorpher.class$0 = Class.forName("java.lang.Class"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
    
    public boolean supports(final Class clazz) {
        return true;
    }
}
