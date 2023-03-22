// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.processors;

import net.sf.json.JsonConfig;

public interface JsonValueProcessor
{
    Object processArrayValue(final Object p0, final JsonConfig p1);
    
    Object processObjectValue(final String p0, final Object p1, final JsonConfig p2);
}
