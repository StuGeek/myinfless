// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.collections.map;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import org.apache.commons.collections.KeyValue;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;

public final class StaticBucketMap implements Map
{
    private static final int DEFAULT_BUCKETS = 255;
    private Node[] buckets;
    private Lock[] locks;
    
    public StaticBucketMap() {
        this(255);
    }
    
    public StaticBucketMap(final int numBuckets) {
        int size = Math.max(17, numBuckets);
        if (size % 2 == 0) {
            --size;
        }
        this.buckets = new Node[size];
        this.locks = new Lock[size];
        for (int i = 0; i < size; ++i) {
            this.locks[i] = new Lock();
        }
    }
    
    private final int getHash(final Object key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        hash += ~(hash << 15);
        hash ^= hash >>> 10;
        hash += hash << 3;
        hash ^= hash >>> 6;
        hash += ~(hash << 11);
        hash ^= hash >>> 16;
        hash %= this.buckets.length;
        return (hash < 0) ? (hash * -1) : hash;
    }
    
    public int size() {
        int cnt = 0;
        for (int i = 0; i < this.buckets.length; ++i) {
            cnt += this.locks[i].size;
        }
        return cnt;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public Object get(final Object key) {
        final int hash = this.getHash(key);
        synchronized (this.locks[hash]) {
            for (Node n = this.buckets[hash]; n != null; n = n.next) {
                if (n.key == key || (n.key != null && n.key.equals(key))) {
                    return n.value;
                }
            }
        }
        return null;
    }
    
    public boolean containsKey(final Object key) {
        final int hash = this.getHash(key);
        synchronized (this.locks[hash]) {
            for (Node n = this.buckets[hash]; n != null; n = n.next) {
                if (n.key == key || (n.key != null && n.key.equals(key))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean containsValue(final Object value) {
        for (int i = 0; i < this.buckets.length; ++i) {
            synchronized (this.locks[i]) {
                for (Node n = this.buckets[i]; n != null; n = n.next) {
                    if (n.value == value || (n.value != null && n.value.equals(value))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public Object put(final Object key, final Object value) {
        final int hash = this.getHash(key);
        synchronized (this.locks[hash]) {
            Node n = this.buckets[hash];
            if (n == null) {
                n = new Node();
                n.key = key;
                n.value = value;
                this.buckets[hash] = n;
                final Lock lock = this.locks[hash];
                ++lock.size;
                return null;
            }
            for (Node next = n; next != null; next = next.next) {
                n = next;
                if (n.key == key || (n.key != null && n.key.equals(key))) {
                    final Object returnVal = n.value;
                    n.value = value;
                    return returnVal;
                }
            }
            final Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            n.next = newNode;
            final Lock lock2 = this.locks[hash];
            ++lock2.size;
        }
        return null;
    }
    
    public Object remove(final Object key) {
        final int hash = this.getHash(key);
        synchronized (this.locks[hash]) {
            Node n = this.buckets[hash];
            Node prev = null;
            while (n != null) {
                if (n.key == key || (n.key != null && n.key.equals(key))) {
                    if (null == prev) {
                        this.buckets[hash] = n.next;
                    }
                    else {
                        prev.next = n.next;
                    }
                    final Lock lock = this.locks[hash];
                    --lock.size;
                    return n.value;
                }
                prev = n;
                n = n.next;
            }
        }
        return null;
    }
    
    public Set keySet() {
        return new KeySet();
    }
    
    public Collection values() {
        return new Values();
    }
    
    public Set entrySet() {
        return new EntrySet();
    }
    
    public void putAll(final Map map) {
        for (final Object key : map.keySet()) {
            this.put(key, map.get(key));
        }
    }
    
    public void clear() {
        for (int i = 0; i < this.buckets.length; ++i) {
            final Lock lock = this.locks[i];
            synchronized (lock) {
                this.buckets[i] = null;
                lock.size = 0;
            }
        }
    }
    
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        final Map other = (Map)obj;
        return this.entrySet().equals(other.entrySet());
    }
    
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < this.buckets.length; ++i) {
            synchronized (this.locks[i]) {
                for (Node n = this.buckets[i]; n != null; n = n.next) {
                    hashCode += n.hashCode();
                }
            }
        }
        return hashCode;
    }
    
    public void atomic(final Runnable r) {
        if (r == null) {
            throw new NullPointerException();
        }
        this.atomic(r, 0);
    }
    
    private void atomic(final Runnable r, final int bucket) {
        if (bucket >= this.buckets.length) {
            r.run();
            return;
        }
        synchronized (this.locks[bucket]) {
            this.atomic(r, bucket + 1);
        }
    }
    
    private static final class Node implements Entry, KeyValue
    {
        protected Object key;
        protected Object value;
        protected Node next;
        
        public Object getKey() {
            return this.key;
        }
        
        public Object getValue() {
            return this.value;
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            final Entry e2 = (Entry)obj;
            if (this.key == null) {
                if (e2.getKey() != null) {
                    return false;
                }
            }
            else if (!this.key.equals(e2.getKey())) {
                return false;
            }
            if ((this.value != null) ? this.value.equals(e2.getValue()) : (e2.getValue() == null)) {
                return true;
            }
            return false;
        }
        
        public Object setValue(final Object obj) {
            final Object retVal = this.value;
            this.value = obj;
            return retVal;
        }
    }
    
    private static final class Lock
    {
        public int size;
    }
    
    private class EntryIterator implements Iterator
    {
        private ArrayList current;
        private int bucket;
        private Entry last;
        
        private EntryIterator() {
            this.current = new ArrayList();
        }
        
        public boolean hasNext() {
            if (this.current.size() > 0) {
                return true;
            }
            while (this.bucket < StaticBucketMap.this.buckets.length) {
                synchronized (StaticBucketMap.this.locks[this.bucket]) {
                    for (Node n = StaticBucketMap.this.buckets[this.bucket]; n != null; n = n.next) {
                        this.current.add(n);
                    }
                    ++this.bucket;
                    if (this.current.size() > 0) {
                        return true;
                    }
                    continue;
                }
            }
            return false;
        }
        
        protected Entry nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.last = this.current.remove(this.current.size() - 1);
        }
        
        public Object next() {
            return this.nextEntry();
        }
        
        public void remove() {
            if (this.last == null) {
                throw new IllegalStateException();
            }
            StaticBucketMap.this.remove(this.last.getKey());
            this.last = null;
        }
    }
    
    private class ValueIterator extends EntryIterator
    {
        public Object next() {
            return this.nextEntry().getValue();
        }
    }
    
    private class KeyIterator extends EntryIterator
    {
        public Object next() {
            return this.nextEntry().getKey();
        }
    }
    
    private class EntrySet extends AbstractSet
    {
        public int size() {
            return StaticBucketMap.this.size();
        }
        
        public void clear() {
            StaticBucketMap.this.clear();
        }
        
        public Iterator iterator() {
            return new EntryIterator();
        }
        
        public boolean contains(final Object obj) {
            final Entry entry = (Entry)obj;
            final int hash = StaticBucketMap.this.getHash(entry.getKey());
            synchronized (StaticBucketMap.this.locks[hash]) {
                for (Node n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
                    if (n.equals(entry)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        public boolean remove(final Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            final Entry entry = (Entry)obj;
            final int hash = StaticBucketMap.this.getHash(entry.getKey());
            synchronized (StaticBucketMap.this.locks[hash]) {
                for (Node n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
                    if (n.equals(entry)) {
                        StaticBucketMap.this.remove(n.getKey());
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    private class KeySet extends AbstractSet
    {
        public int size() {
            return StaticBucketMap.this.size();
        }
        
        public void clear() {
            StaticBucketMap.this.clear();
        }
        
        public Iterator iterator() {
            return new KeyIterator();
        }
        
        public boolean contains(final Object obj) {
            return StaticBucketMap.this.containsKey(obj);
        }
        
        public boolean remove(final Object obj) {
            final int hash = StaticBucketMap.this.getHash(obj);
            synchronized (StaticBucketMap.this.locks[hash]) {
                for (Node n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
                    final Object k = n.getKey();
                    if (k == obj || (k != null && k.equals(obj))) {
                        StaticBucketMap.this.remove(k);
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    private class Values extends AbstractCollection
    {
        public int size() {
            return StaticBucketMap.this.size();
        }
        
        public void clear() {
            StaticBucketMap.this.clear();
        }
        
        public Iterator iterator() {
            return new ValueIterator();
        }
    }
}
