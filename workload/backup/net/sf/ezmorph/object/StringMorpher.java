// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.ObjectMorpher;

public final class StringMorpher implements ObjectMorpher
{
    private static final StringMorpher INSTANCE;
    static /* synthetic */ Class class$0;
    
    static {
        INSTANCE = new StringMorpher();
    }
    
    public static StringMorpher getInstance() {
        return StringMorpher.INSTANCE;
    }
    
    private StringMorpher() {
    }
    
    public boolean equals(final Object obj) {
        return StringMorpher.INSTANCE == obj;
    }
    
    public int hashCode() {
        return 42 + this.getClass().hashCode();
    }
    
    public Object morph(final Object value) {
        if (value == null) {
            return null;
        }
        if (!this.supports(value.getClass())) {
            throw new MorphException("Class not supported. " + value.getClass());
        }
        Class class$0;
        if ((class$0 = StringMorpher.class$0) == null) {
            try {
                class$0 = (StringMorpher.class$0 = Class.forName("java.lang.String"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (class$0.isAssignableFrom(value.getClass())) {
            return value;
        }
        return String.valueOf(value);
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = StringMorpher.class$0) == null) {
            try {
                class$0 = (StringMorpher.class$0 = Class.forName("java.lang.String"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
    
    public boolean supports(final Class clazz) {
        return !clazz.isArray();
    }
}
