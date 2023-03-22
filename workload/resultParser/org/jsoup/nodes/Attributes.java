// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import java.util.AbstractSet;
import java.util.Set;
import java.util.AbstractMap;
import org.jsoup.internal.Normalizer;
import java.util.Arrays;
import java.io.IOException;
import org.jsoup.SerializationException;
import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.jsoup.helper.Validate;

public class Attributes implements Iterable<Attribute>, Cloneable
{
    protected static final String dataPrefix = "data-";
    private static final int InitialCapacity = 4;
    private static final int GrowthFactor = 2;
    private static final String[] Empty;
    static final int NotFound = -1;
    private static final String EmptyString = "";
    private int size;
    String[] keys;
    String[] vals;
    
    public Attributes() {
        this.size = 0;
        this.keys = Attributes.Empty;
        this.vals = Attributes.Empty;
    }
    
    private void checkCapacity(final int minNewSize) {
        Validate.isTrue(minNewSize >= this.size);
        final int curSize = this.keys.length;
        if (curSize >= minNewSize) {
            return;
        }
        int newSize = (curSize >= 4) ? (this.size * 2) : 4;
        if (minNewSize > newSize) {
            newSize = minNewSize;
        }
        this.keys = copyOf(this.keys, newSize);
        this.vals = copyOf(this.vals, newSize);
    }
    
    private static String[] copyOf(final String[] orig, final int size) {
        final String[] copy = new String[size];
        System.arraycopy(orig, 0, copy, 0, Math.min(orig.length, size));
        return copy;
    }
    
    int indexOfKey(final String key) {
        Validate.notNull(key);
        for (int i = 0; i < this.size; ++i) {
            if (key.equals(this.keys[i])) {
                return i;
            }
        }
        return -1;
    }
    
    private int indexOfKeyIgnoreCase(final String key) {
        Validate.notNull(key);
        for (int i = 0; i < this.size; ++i) {
            if (key.equalsIgnoreCase(this.keys[i])) {
                return i;
            }
        }
        return -1;
    }
    
    static String checkNotNull(final String val) {
        return (val == null) ? "" : val;
    }
    
    public String get(final String key) {
        final int i = this.indexOfKey(key);
        return (i == -1) ? "" : checkNotNull(this.vals[i]);
    }
    
    public String getIgnoreCase(final String key) {
        final int i = this.indexOfKeyIgnoreCase(key);
        return (i == -1) ? "" : checkNotNull(this.vals[i]);
    }
    
    private void add(final String key, final String value) {
        this.checkCapacity(this.size + 1);
        this.keys[this.size] = key;
        this.vals[this.size] = value;
        ++this.size;
    }
    
    public Attributes put(final String key, final String value) {
        final int i = this.indexOfKey(key);
        if (i != -1) {
            this.vals[i] = value;
        }
        else {
            this.add(key, value);
        }
        return this;
    }
    
    void putIgnoreCase(final String key, final String value) {
        final int i = this.indexOfKeyIgnoreCase(key);
        if (i != -1) {
            this.vals[i] = value;
            if (!this.keys[i].equals(key)) {
                this.keys[i] = key;
            }
        }
        else {
            this.add(key, value);
        }
    }
    
    public Attributes put(final String key, final boolean value) {
        if (value) {
            this.putIgnoreCase(key, null);
        }
        else {
            this.remove(key);
        }
        return this;
    }
    
    public Attributes put(final Attribute attribute) {
        Validate.notNull(attribute);
        this.put(attribute.getKey(), attribute.getValue());
        return attribute.parent = this;
    }
    
    private void remove(final int index) {
        Validate.isFalse(index >= this.size);
        final int shifted = this.size - index - 1;
        if (shifted > 0) {
            System.arraycopy(this.keys, index + 1, this.keys, index, shifted);
            System.arraycopy(this.vals, index + 1, this.vals, index, shifted);
        }
        --this.size;
        this.keys[this.size] = null;
        this.vals[this.size] = null;
    }
    
    public void remove(final String key) {
        final int i = this.indexOfKey(key);
        if (i != -1) {
            this.remove(i);
        }
    }
    
    public void removeIgnoreCase(final String key) {
        final int i = this.indexOfKeyIgnoreCase(key);
        if (i != -1) {
            this.remove(i);
        }
    }
    
    public boolean hasKey(final String key) {
        return this.indexOfKey(key) != -1;
    }
    
    public boolean hasKeyIgnoreCase(final String key) {
        return this.indexOfKeyIgnoreCase(key) != -1;
    }
    
    public int size() {
        return this.size;
    }
    
    public void addAll(final Attributes incoming) {
        if (incoming.size() == 0) {
            return;
        }
        this.checkCapacity(this.size + incoming.size);
        for (final Attribute attr : incoming) {
            this.put(attr);
        }
    }
    
    @Override
    public Iterator<Attribute> iterator() {
        return new Iterator<Attribute>() {
            int i = 0;
            
            @Override
            public boolean hasNext() {
                return this.i < Attributes.this.size;
            }
            
            @Override
            public Attribute next() {
                final Attribute attr = new Attribute(Attributes.this.keys[this.i], Attributes.this.vals[this.i], Attributes.this);
                ++this.i;
                return attr;
            }
            
            @Override
            public void remove() {
                Attributes.this.remove(--this.i);
            }
        };
    }
    
    public List<Attribute> asList() {
        final ArrayList<Attribute> list = new ArrayList<Attribute>(this.size);
        for (int i = 0; i < this.size; ++i) {
            final Attribute attr = (this.vals[i] == null) ? new BooleanAttribute(this.keys[i]) : new Attribute(this.keys[i], this.vals[i], this);
            list.add(attr);
        }
        return Collections.unmodifiableList((List<? extends Attribute>)list);
    }
    
    public Map<String, String> dataset() {
        return new Dataset(this);
    }
    
    public String html() {
        final StringBuilder accum = new StringBuilder();
        try {
            this.html(accum, new Document("").outputSettings());
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
        return accum.toString();
    }
    
    final void html(final Appendable accum, final Document.OutputSettings out) throws IOException {
        for (int sz = this.size, i = 0; i < sz; ++i) {
            final String key = this.keys[i];
            final String val = this.vals[i];
            accum.append(' ').append(key);
            if (!Attribute.shouldCollapseAttribute(key, val, out)) {
                accum.append("=\"");
                Entities.escape(accum, (val == null) ? "" : val, out, true, false, false);
                accum.append('\"');
            }
        }
    }
    
    @Override
    public String toString() {
        return this.html();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Attributes that = (Attributes)o;
        return this.size == that.size && Arrays.equals(this.keys, that.keys) && Arrays.equals(this.vals, that.vals);
    }
    
    @Override
    public int hashCode() {
        int result = this.size;
        result = 31 * result + Arrays.hashCode(this.keys);
        result = 31 * result + Arrays.hashCode(this.vals);
        return result;
    }
    
    public Attributes clone() {
        Attributes clone;
        try {
            clone = (Attributes)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        clone.size = this.size;
        this.keys = copyOf(this.keys, this.size);
        this.vals = copyOf(this.vals, this.size);
        return clone;
    }
    
    public void normalize() {
        for (int i = 0; i < this.size; ++i) {
            this.keys[i] = Normalizer.lowerCase(this.keys[i]);
        }
    }
    
    private static String dataKey(final String key) {
        return "data-" + key;
    }
    
    static {
        Empty = new String[0];
    }
    
    private static class Dataset extends AbstractMap<String, String>
    {
        private final Attributes attributes;
        
        private Dataset(final Attributes attributes) {
            this.attributes = attributes;
        }
        
        @Override
        public Set<Map.Entry<String, String>> entrySet() {
            return new EntrySet();
        }
        
        @Override
        public String put(final String key, final String value) {
            final String dataKey = dataKey(key);
            final String oldValue = this.attributes.hasKey(dataKey) ? this.attributes.get(dataKey) : null;
            this.attributes.put(dataKey, value);
            return oldValue;
        }
        
        private class EntrySet extends AbstractSet<Map.Entry<String, String>>
        {
            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                return new DatasetIterator();
            }
            
            @Override
            public int size() {
                int count = 0;
                final Iterator iter = new DatasetIterator();
                while (iter.hasNext()) {
                    ++count;
                }
                return count;
            }
        }
        
        private class DatasetIterator implements Iterator<Map.Entry<String, String>>
        {
            private Iterator<Attribute> attrIter;
            private Attribute attr;
            
            private DatasetIterator() {
                this.attrIter = Dataset.this.attributes.iterator();
            }
            
            @Override
            public boolean hasNext() {
                while (this.attrIter.hasNext()) {
                    this.attr = this.attrIter.next();
                    if (this.attr.isDataAttribute()) {
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public Map.Entry<String, String> next() {
                return new Attribute(this.attr.getKey().substring("data-".length()), this.attr.getValue());
            }
            
            @Override
            public void remove() {
                Dataset.this.attributes.remove(this.attr.getKey());
            }
        }
    }
}
