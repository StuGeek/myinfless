// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import java.io.Writer;
import java.io.Serializable;

public interface JSON extends Serializable
{
    boolean isArray();
    
    boolean isEmpty();
    
    int size();
    
    String toString(final int p0);
    
    String toString(final int p0, final int p1);
    
    Writer write(final Writer p0);
}
