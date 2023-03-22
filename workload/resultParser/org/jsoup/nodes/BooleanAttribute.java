// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

public class BooleanAttribute extends Attribute
{
    public BooleanAttribute(final String key) {
        super(key, null);
    }
    
    @Override
    protected boolean isBooleanAttribute() {
        return true;
    }
}
