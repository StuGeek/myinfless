// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.primitive;

import net.sf.ezmorph.Morpher;

public abstract class AbstractPrimitiveMorpher implements Morpher
{
    private boolean useDefault;
    
    public AbstractPrimitiveMorpher() {
        this.useDefault = false;
    }
    
    public AbstractPrimitiveMorpher(final boolean useDefault) {
        this.useDefault = false;
        this.useDefault = useDefault;
    }
    
    public boolean isUseDefault() {
        return this.useDefault;
    }
    
    public boolean supports(final Class clazz) {
        return !clazz.isArray();
    }
    
    public abstract Class morphsTo();
}
