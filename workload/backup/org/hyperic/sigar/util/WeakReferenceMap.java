// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.util;

public class WeakReferenceMap extends ReferenceMap
{
    public Object put(final Object key, final Object value) {
        this.poll();
        return this.map.put(key, new WeakValue(key, value, this.queue));
    }
}
