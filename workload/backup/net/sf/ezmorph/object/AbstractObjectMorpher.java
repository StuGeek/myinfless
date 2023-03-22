// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import net.sf.ezmorph.ObjectMorpher;

public abstract class AbstractObjectMorpher implements ObjectMorpher
{
    private boolean useDefault;
    
    public AbstractObjectMorpher() {
    }
    
    public AbstractObjectMorpher(final boolean useDefault) {
        this.useDefault = useDefault;
    }
    
    public boolean isUseDefault() {
        return this.useDefault;
    }
    
    public void setUseDefault(final boolean useDefault) {
        this.useDefault = useDefault;
    }
    
    public boolean supports(final Class clazz) {
        return !clazz.isArray();
    }
    
    public abstract Class morphsTo();
    
    public abstract Object morph(final Object p0);
}
