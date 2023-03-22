// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.util;

import java.util.ArrayList;
import java.util.Iterator;

public class IteratorIterator implements Iterator
{
    private int ix;
    private Iterator curr;
    private ArrayList iterators;
    
    public IteratorIterator() {
        this.ix = 0;
        this.curr = null;
        this.iterators = new ArrayList();
    }
    
    public void add(final Iterator iterator) {
        this.iterators.add(iterator);
    }
    
    public boolean hasNext() {
        final int size = this.iterators.size();
        if (this.curr == null) {
            if (size == 0) {
                return false;
            }
            this.curr = this.iterators.get(0);
        }
        if (this.curr.hasNext()) {
            return true;
        }
        ++this.ix;
        if (this.ix >= size) {
            return false;
        }
        this.curr = this.iterators.get(this.ix);
        return this.hasNext();
    }
    
    public Object next() {
        return this.curr.next();
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
