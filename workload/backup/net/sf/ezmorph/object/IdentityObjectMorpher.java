// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import net.sf.ezmorph.ObjectMorpher;

public final class IdentityObjectMorpher implements ObjectMorpher
{
    private static final IdentityObjectMorpher INSTANCE;
    static /* synthetic */ Class class$0;
    
    static {
        INSTANCE = new IdentityObjectMorpher();
    }
    
    public static IdentityObjectMorpher getInstance() {
        return IdentityObjectMorpher.INSTANCE;
    }
    
    private IdentityObjectMorpher() {
    }
    
    public boolean equals(final Object obj) {
        return IdentityObjectMorpher.INSTANCE == obj;
    }
    
    public int hashCode() {
        return 42 + this.getClass().hashCode();
    }
    
    public Object morph(final Object value) {
        return value;
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = IdentityObjectMorpher.class$0) == null) {
            try {
                class$0 = (IdentityObjectMorpher.class$0 = Class.forName("java.lang.Object"));
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
