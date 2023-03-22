// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.pager;

import java.util.Comparator;
import java.util.Collections;
import java.util.ListIterator;
import java.util.List;

public class ListPageFetcher extends PageFetcher
{
    private List data;
    private int sortOrder;
    
    public ListPageFetcher(final List data) {
        this.data = data;
        this.sortOrder = 0;
    }
    
    public PageList getPage(final PageControl control) {
        final PageList res = new PageList();
        if (this.data.size() == 0) {
            return new PageList();
        }
        this.ensureSortOrder(control);
        res.setTotalSize(this.data.size());
        int curIdx;
        final int startIdx = curIdx = clamp(control.getPageEntityIndex(), 0, this.data.size() - 1);
        int endIdx;
        if (control.getPagesize() == -1) {
            endIdx = this.data.size();
        }
        else {
            endIdx = clamp(startIdx + control.getPagesize(), startIdx, this.data.size());
        }
        for (ListIterator i = this.data.listIterator(startIdx); i.hasNext() && curIdx < endIdx; ++curIdx) {
            res.add(i.next());
        }
        return res;
    }
    
    private void ensureSortOrder(final PageControl control) {
        if (control.getSortorder() == this.sortOrder) {
            return;
        }
        this.sortOrder = control.getSortorder();
        if (this.sortOrder == 0) {
            return;
        }
        if (this.sortOrder == 1) {
            Collections.sort((List<Comparable>)this.data);
        }
        else {
            if (this.sortOrder != 2) {
                throw new IllegalStateException("Unknown control sorting type: " + this.sortOrder);
            }
            Collections.sort((List<Object>)this.data, new DescSorter());
        }
    }
    
    private static int clamp(final int val, final int min, final int max) {
        return (int)clamp(val, min, (long)max);
    }
    
    private static long clamp(final long val, final long min, final long max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }
    
    private class DescSorter implements Comparator
    {
        public int compare(final Object o1, final Object o2) {
            return -((Comparable)o1).compareTo(o2);
        }
        
        public boolean equals(final Object other) {
            return false;
        }
    }
}
