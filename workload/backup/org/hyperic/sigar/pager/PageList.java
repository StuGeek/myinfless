// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.pager;

import java.util.Collection;
import java.io.Serializable;
import java.util.ArrayList;

public class PageList extends ArrayList implements Serializable
{
    private int totalSize;
    private boolean isUnbounded;
    private Serializable metaData;
    
    public PageList() {
        this.totalSize = 0;
        this.isUnbounded = false;
    }
    
    public PageList(final Collection c, final int totalSize) {
        super(c);
        this.totalSize = 0;
        this.totalSize = totalSize;
        this.isUnbounded = false;
    }
    
    public String toString() {
        final StringBuffer s = new StringBuffer("{");
        s.append("totalSize=" + this.totalSize + " ");
        s.append("}");
        return super.toString() + s.toString();
    }
    
    public int getTotalSize() {
        return Math.max(this.size(), this.totalSize);
    }
    
    public void setTotalSize(final int totalSize) {
        this.totalSize = totalSize;
    }
    
    public void setMetaData(final Serializable metaData) {
        this.metaData = metaData;
    }
    
    public Serializable getMetaData() {
        return this.metaData;
    }
    
    public boolean isUnbounded() {
        return this.isUnbounded;
    }
    
    public void setUnbounded(final boolean isUnbounded) {
        this.isUnbounded = isUnbounded;
    }
}
