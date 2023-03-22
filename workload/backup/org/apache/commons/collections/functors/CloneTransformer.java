// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.collections.functors;

import java.io.Serializable;
import org.apache.commons.collections.Transformer;

public class CloneTransformer implements Transformer, Serializable
{
    private static final long serialVersionUID = -8188742709499652567L;
    public static final Transformer INSTANCE;
    
    public static Transformer getInstance() {
        return CloneTransformer.INSTANCE;
    }
    
    private CloneTransformer() {
    }
    
    public Object transform(final Object input) {
        if (input == null) {
            return null;
        }
        return PrototypeFactory.getInstance(input).create();
    }
    
    static {
        INSTANCE = new CloneTransformer();
    }
}
