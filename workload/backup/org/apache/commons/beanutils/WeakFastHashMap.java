// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.util.ConcurrentModificationException;
import java.util.WeakHashMap;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

class WeakFastHashMap extends HashMap
{
    private Map map;
    private boolean fast;
    
    public WeakFastHashMap() {
        this.map = null;
        this.fast = false;
        this.map = this.createMap();
    }
    
    public WeakFastHashMap(final int capacity) {
        this.map = null;
        this.fast = false;
        this.map = this.createMap(capacity);
    }
    
    public WeakFastHashMap(final int capacity, final float factor) {
        this.map = null;
        this.fast = false;
        this.map = this.createMap(capacity, factor);
    }
    
    public WeakFastHashMap(final Map map) {
        this.map = null;
        this.fast = false;
        this.map = this.createMap(map);
    }
    
    public boolean getFast() {
        return this.fast;
    }
    
    public void setFast(final boolean fast) {
        this.fast = fast;
    }
    
    public Object get(final Object key) {
        if (this.fast) {
            return this.map.get(key);
        }
        synchronized (this.map) {
            return this.map.get(key);
        }
    }
    
    public int size() {
        if (this.fast) {
            return this.map.size();
        }
        synchronized (this.map) {
            return this.map.size();
        }
    }
    
    public boolean isEmpty() {
        if (this.fast) {
            return this.map.isEmpty();
        }
        synchronized (this.map) {
            return this.map.isEmpty();
        }
    }
    
    public boolean containsKey(final Object key) {
        if (this.fast) {
            return this.map.containsKey(key);
        }
        synchronized (this.map) {
            return this.map.containsKey(key);
        }
    }
    
    public boolean containsValue(final Object value) {
        if (this.fast) {
            return this.map.containsValue(value);
        }
        synchronized (this.map) {
            return this.map.containsValue(value);
        }
    }
    
    public Object put(final Object key, final Object value) {
        if (this.fast) {
            synchronized (this) {
                final Map temp = this.cloneMap(this.map);
                final Object result = temp.put(key, value);
                this.map = temp;
                return result;
            }
        }
        synchronized (this.map) {
            return this.map.put(key, value);
        }
    }
    
    public void putAll(final Map in) {
        if (this.fast) {
            synchronized (this) {
                final Map temp = this.cloneMap(this.map);
                temp.putAll(in);
                this.map = temp;
            }
        }
        else {
            synchronized (this.map) {
                this.map.putAll(in);
            }
        }
    }
    
    public Object remove(final Object key) {
        if (this.fast) {
            synchronized (this) {
                final Map temp = this.cloneMap(this.map);
                final Object result = temp.remove(key);
                this.map = temp;
                return result;
            }
        }
        synchronized (this.map) {
            return this.map.remove(key);
        }
    }
    
    public void clear() {
        if (this.fast) {
            synchronized (this) {
                this.map = this.createMap();
            }
        }
        else {
            synchronized (this.map) {
                this.map.clear();
            }
        }
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        final Map mo = (Map)o;
        if (this.fast) {
            if (mo.size() != this.map.size()) {
                return false;
            }
            for (final Map.Entry e : this.map.entrySet()) {
                final Object key = e.getKey();
                final Object value = e.getValue();
                if (value == null) {
                    if (mo.get(key) != null || !mo.containsKey(key)) {
                        return false;
                    }
                    continue;
                }
                else {
                    if (!value.equals(mo.get(key))) {
                        return false;
                    }
                    continue;
                }
            }
            return true;
        }
        else {
            synchronized (this.map) {
                if (mo.size() != this.map.size()) {
                    return false;
                }
                for (final Map.Entry e2 : this.map.entrySet()) {
                    final Object key2 = e2.getKey();
                    final Object value2 = e2.getValue();
                    if (value2 == null) {
                        if (mo.get(key2) != null || !mo.containsKey(key2)) {
                            return false;
                        }
                        continue;
                    }
                    else {
                        if (!value2.equals(mo.get(key2))) {
                            return false;
                        }
                        continue;
                    }
                }
                return true;
            }
        }
    }
    
    public int hashCode() {
        if (this.fast) {
            int h = 0;
            final Iterator i = this.map.entrySet().iterator();
            while (i.hasNext()) {
                h += i.next().hashCode();
            }
            return h;
        }
        synchronized (this.map) {
            int h2 = 0;
            final Iterator j = this.map.entrySet().iterator();
            while (j.hasNext()) {
                h2 += j.next().hashCode();
            }
            return h2;
        }
    }
    
    public Object clone() {
        WeakFastHashMap results = null;
        if (this.fast) {
            results = new WeakFastHashMap(this.map);
        }
        else {
            synchronized (this.map) {
                results = new WeakFastHashMap(this.map);
            }
        }
        results.setFast(this.getFast());
        return results;
    }
    
    public Set entrySet() {
        return new EntrySet();
    }
    
    public Set keySet() {
        return new KeySet();
    }
    
    public Collection values() {
        return new Values();
    }
    
    protected Map createMap() {
        return new WeakHashMap();
    }
    
    protected Map createMap(final int capacity) {
        return new WeakHashMap(capacity);
    }
    
    protected Map createMap(final int capacity, final float factor) {
        return new WeakHashMap(capacity, factor);
    }
    
    protected Map createMap(final Map map) {
        return new WeakHashMap(map);
    }
    
    protected Map cloneMap(final Map map) {
        return this.createMap(map);
    }
    
    private abstract class CollectionView implements Collection
    {
        private final /* synthetic */ WeakFastHashMap this$0;
        
        public CollectionView() {
        }
        
        protected abstract Collection get(final Map p0);
        
        protected abstract Object iteratorNext(final Map.Entry p0);
        
        public void clear() {
            if (WeakFastHashMap.this.fast) {
                synchronized (WeakFastHashMap.this) {
                    WeakFastHashMap.this.map = WeakFastHashMap.this.createMap();
                }
            }
            else {
                synchronized (WeakFastHashMap.this.map) {
                    this.get(WeakFastHashMap.this.map).clear();
                }
            }
        }
        
        public boolean remove(final Object o) {
            if (WeakFastHashMap.this.fast) {
                synchronized (WeakFastHashMap.this) {
                    final Map temp = WeakFastHashMap.this.cloneMap(WeakFastHashMap.this.map);
                    final boolean r = this.get(temp).remove(o);
                    WeakFastHashMap.this.map = temp;
                    return r;
                }
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).remove(o);
            }
        }
        
        public boolean removeAll(final Collection o) {
            if (WeakFastHashMap.this.fast) {
                synchronized (WeakFastHashMap.this) {
                    final Map temp = WeakFastHashMap.this.cloneMap(WeakFastHashMap.this.map);
                    final boolean r = this.get(temp).removeAll(o);
                    WeakFastHashMap.this.map = temp;
                    return r;
                }
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).removeAll(o);
            }
        }
        
        public boolean retainAll(final Collection o) {
            if (WeakFastHashMap.this.fast) {
                synchronized (WeakFastHashMap.this) {
                    final Map temp = WeakFastHashMap.this.cloneMap(WeakFastHashMap.this.map);
                    final boolean r = this.get(temp).retainAll(o);
                    WeakFastHashMap.this.map = temp;
                    return r;
                }
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).retainAll(o);
            }
        }
        
        public int size() {
            if (WeakFastHashMap.this.fast) {
                return this.get(WeakFastHashMap.this.map).size();
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).size();
            }
        }
        
        public boolean isEmpty() {
            if (WeakFastHashMap.this.fast) {
                return this.get(WeakFastHashMap.this.map).isEmpty();
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).isEmpty();
            }
        }
        
        public boolean contains(final Object o) {
            if (WeakFastHashMap.this.fast) {
                return this.get(WeakFastHashMap.this.map).contains(o);
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).contains(o);
            }
        }
        
        public boolean containsAll(final Collection o) {
            if (WeakFastHashMap.this.fast) {
                return this.get(WeakFastHashMap.this.map).containsAll(o);
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).containsAll(o);
            }
        }
        
        public Object[] toArray(final Object[] o) {
            if (WeakFastHashMap.this.fast) {
                return this.get(WeakFastHashMap.this.map).toArray(o);
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).toArray(o);
            }
        }
        
        public Object[] toArray() {
            if (WeakFastHashMap.this.fast) {
                return this.get(WeakFastHashMap.this.map).toArray();
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).toArray();
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (WeakFastHashMap.this.fast) {
                return this.get(WeakFastHashMap.this.map).equals(o);
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).equals(o);
            }
        }
        
        public int hashCode() {
            if (WeakFastHashMap.this.fast) {
                return this.get(WeakFastHashMap.this.map).hashCode();
            }
            synchronized (WeakFastHashMap.this.map) {
                return this.get(WeakFastHashMap.this.map).hashCode();
            }
        }
        
        public boolean add(final Object o) {
            throw new UnsupportedOperationException();
        }
        
        public boolean addAll(final Collection c) {
            throw new UnsupportedOperationException();
        }
        
        public Iterator iterator() {
            return new CollectionViewIterator();
        }
        
        private class CollectionViewIterator implements Iterator
        {
            private Map expected;
            private Map.Entry lastReturned;
            private Iterator iterator;
            
            public CollectionViewIterator() {
                this.lastReturned = null;
                this.expected = CollectionView.this.this$0.map;
                this.iterator = this.expected.entrySet().iterator();
            }
            
            public boolean hasNext() {
                if (this.expected != WeakFastHashMap.this.map) {
                    throw new ConcurrentModificationException();
                }
                return this.iterator.hasNext();
            }
            
            public Object next() {
                if (this.expected != WeakFastHashMap.this.map) {
                    throw new ConcurrentModificationException();
                }
                this.lastReturned = this.iterator.next();
                return CollectionView.this.iteratorNext(this.lastReturned);
            }
            
            public void remove() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                }
                if (WeakFastHashMap.this.fast) {
                    synchronized (WeakFastHashMap.this) {
                        if (this.expected != WeakFastHashMap.this.map) {
                            throw new ConcurrentModificationException();
                        }
                        WeakFastHashMap.this.remove(this.lastReturned.getKey());
                        this.lastReturned = null;
                        this.expected = WeakFastHashMap.this.map;
                    }
                }
                else {
                    this.iterator.remove();
                    this.lastReturned = null;
                }
            }
        }
    }
    
    private class KeySet extends CollectionView implements Set
    {
        protected Collection get(final Map map) {
            return map.keySet();
        }
        
        protected Object iteratorNext(final Map.Entry entry) {
            return entry.getKey();
        }
    }
    
    private class Values extends CollectionView
    {
        protected Collection get(final Map map) {
            return map.values();
        }
        
        protected Object iteratorNext(final Map.Entry entry) {
            return entry.getValue();
        }
    }
    
    private class EntrySet extends CollectionView implements Set
    {
        protected Collection get(final Map map) {
            return map.entrySet();
        }
        
        protected Object iteratorNext(final Map.Entry entry) {
            return entry;
        }
    }
}
