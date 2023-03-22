// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.pager;

import java.io.Serializable;

public class PageControl implements Serializable, Cloneable
{
    public static final int SIZE_UNLIMITED = -1;
    public static final int SORT_UNSORTED = 0;
    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = 2;
    private int pagenum;
    private int pagesize;
    private int sortorder;
    private int sortattribute;
    private Serializable metaData;
    
    public PageControl() {
        this.pagenum = 0;
        this.pagesize = -1;
        this.sortorder = 0;
        this.sortattribute = 0;
    }
    
    public PageControl(final int pagenum, final int pagesize) {
        this.pagenum = 0;
        this.pagesize = -1;
        this.sortorder = 0;
        this.sortattribute = 0;
        this.pagenum = pagenum;
        this.pagesize = pagesize;
    }
    
    public PageControl(final int pagenum, final int pagesize, final int sortorder, final int sortattribute) {
        this.pagenum = 0;
        this.pagesize = -1;
        this.sortorder = 0;
        this.sortattribute = 0;
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.sortorder = sortorder;
        this.sortattribute = sortattribute;
    }
    
    public boolean isAscending() {
        return this.sortorder == 1;
    }
    
    public boolean isDescending() {
        return this.sortorder == 2;
    }
    
    public static PageControl initDefaults(PageControl pc, final int defaultSortAttr) {
        if (pc == null) {
            pc = new PageControl();
        }
        else {
            pc = (PageControl)pc.clone();
        }
        if (pc.getSortattribute() == 0) {
            pc.setSortattribute(defaultSortAttr);
        }
        if (pc.getSortorder() == 0) {
            pc.setSortorder(1);
        }
        return pc;
    }
    
    public int getPagenum() {
        return this.pagenum;
    }
    
    public void setPagenum(final int pagenum) {
        this.pagenum = pagenum;
    }
    
    public int getPagesize() {
        return this.pagesize;
    }
    
    public void setPagesize(final int pagesize) {
        this.pagesize = pagesize;
    }
    
    public int getSortorder() {
        return this.sortorder;
    }
    
    public void setSortorder(final int sortorder) {
        this.sortorder = sortorder;
    }
    
    public int getSortattribute() {
        return this.sortattribute;
    }
    
    public void setSortattribute(final int attr) {
        this.sortattribute = attr;
    }
    
    public Serializable getMetaData() {
        return this.metaData;
    }
    
    public void getMetaData(final Serializable metaData) {
        this.metaData = metaData;
    }
    
    public int getPageEntityIndex() {
        return this.pagenum * this.pagesize;
    }
    
    public String toString() {
        final StringBuffer s = new StringBuffer("{");
        s.append("pn=" + this.pagenum + " ");
        s.append("ps=" + this.pagesize + " ");
        s.append("so=");
        switch (this.sortorder) {
            case 1: {
                s.append("asc ");
                break;
            }
            case 2: {
                s.append("desc");
                break;
            }
            case 0: {
                s.append("unsorted ");
                break;
            }
            default: {
                s.append(' ');
                break;
            }
        }
        s.append("sa=" + this.sortattribute + " ");
        s.append("}");
        return s.toString();
    }
    
    public Object clone() {
        final PageControl res = new PageControl(this.pagenum, this.pagesize, this.sortorder, this.sortattribute);
        res.metaData = this.metaData;
        return res;
    }
}
