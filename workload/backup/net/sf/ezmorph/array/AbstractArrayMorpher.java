// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.array;

import java.lang.reflect.Array;
import net.sf.ezmorph.ObjectMorpher;

public abstract class AbstractArrayMorpher implements ObjectMorpher
{
    private boolean useDefault;
    
    public AbstractArrayMorpher() {
        this.useDefault = false;
    }
    
    public AbstractArrayMorpher(final boolean useDefault) {
        this.useDefault = false;
        this.useDefault = useDefault;
    }
    
    public boolean isUseDefault() {
        return this.useDefault;
    }
    
    public void setUseDefault(final boolean useDefault) {
        this.useDefault = useDefault;
    }
    
    public boolean supports(final Class clazz) {
        return clazz.isArray();
    }
    
    protected int[] createDimensions(final int length, final int initial) {
        final Object dims = Array.newInstance(Integer.TYPE, length);
        Array.set(dims, 0, new Integer(initial));
        return (int[])dims;
    }
    
    protected int getDimensions(final Class arrayClass) {
        if (arrayClass == null || !arrayClass.isArray()) {
            return 0;
        }
        return 1 + this.getDimensions(arrayClass.getComponentType());
    }
    
    public abstract Class morphsTo();
    
    public abstract Object morph(final Object p0);
}
