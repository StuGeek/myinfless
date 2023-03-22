// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import net.sf.json.JSONException;

public interface JsonEventListener
{
    void onArrayEnd();
    
    void onArrayStart();
    
    void onElementAdded(final int p0, final Object p1);
    
    void onError(final JSONException p0);
    
    void onObjectEnd();
    
    void onObjectStart();
    
    void onPropertySet(final String p0, final Object p1, final boolean p2);
    
    void onWarning(final String p0);
}
