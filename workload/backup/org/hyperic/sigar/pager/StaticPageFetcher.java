// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.pager;

import java.util.Collection;
import java.util.Arrays;
import java.util.List;

public class StaticPageFetcher extends PageFetcher
{
    private List data;
    
    public StaticPageFetcher(final String[] data) {
        this.data = Arrays.asList(data);
    }
    
    public StaticPageFetcher(final List data) {
        this.data = data;
    }
    
    public PageList getPage(final PageControl control) throws PageFetchException {
        final PageList res = new PageList();
        res.setTotalSize(this.data.size());
        if (control.getPagesize() == -1 || control.getPagenum() == -1) {
            res.addAll(this.data);
            return res;
        }
        final int startIdx = control.getPageEntityIndex();
        int endIdx = startIdx + control.getPagesize();
        if (startIdx >= this.data.size()) {
            return res;
        }
        if (endIdx > this.data.size()) {
            endIdx = this.data.size();
        }
        res.addAll(this.data.subList(startIdx, endIdx));
        return res;
    }
}
