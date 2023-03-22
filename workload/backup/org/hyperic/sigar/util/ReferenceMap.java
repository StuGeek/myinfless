// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.util;

import java.lang.ref.WeakReference;
import java.lang.ref.SoftReference;
import java.util.Set;
import java.lang.ref.Reference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.lang.ref.ReferenceQueue;
import java.util.AbstractMap;

public class ReferenceMap extends AbstractMap
{
    private static final boolean SOFTCACHE;
    protected ReferenceQueue queue;
    protected Map map;
    
    public ReferenceMap() {
        this(new HashMap());
    }
    
    public ReferenceMap(final Map map) {
        this.map = map;
        this.queue = new ReferenceQueue();
    }
    
    public static Map synchronizedMap() {
        final Map map = Collections.synchronizedMap(new HashMap<Object, Object>());
        return newInstance(map);
    }
    
    public static Map newInstance() {
        return newInstance(new HashMap());
    }
    
    public static Map newInstance(final Map map) {
        if (ReferenceMap.SOFTCACHE) {
            return new ReferenceMap(map);
        }
        return map;
    }
    
    public Object get(final Object key) {
        final Reference ref = this.map.get(key);
        if (ref == null) {
            return null;
        }
        final Object o = ref.get();
        if (o == null) {
            this.map.remove(key);
        }
        return o;
    }
    
    public Object put(final Object key, final Object value) {
        this.poll();
        return this.map.put(key, new SoftValue(key, value, this.queue));
    }
    
    public Object remove(final Object key) {
        this.poll();
        return this.map.remove(key);
    }
    
    public void clear() {
        this.poll();
        this.map.clear();
    }
    
    public int size() {
        this.poll();
        return this.map.size();
    }
    
    public Set entrySet() {
        throw new UnsupportedOperationException();
    }
    
    protected void poll() {
        MapReference ref;
        while ((ref = (MapReference)this.queue.poll()) != null) {
            this.map.remove(ref.getKey());
        }
    }
    
    static {
        SOFTCACHE = !"false".equals(System.getProperty("org.hyperic.sigar.softcache"));
    }
    
    protected static final class SoftValue extends SoftReference implements MapReference
    {
        private Object key;
        
        public Object getKey() {
            return this.key;
        }
        
        private SoftValue(final Object key, final Object value, final ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }
    }
    
    protected static final class WeakValue extends WeakReference implements MapReference
    {
        private Object key;
        
        public Object getKey() {
            return this.key;
        }
        
        protected WeakValue(final Object key, final Object value, final ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }
    }
    
    protected interface MapReference
    {
        Object getKey();
    }
}
