// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.pager;

public abstract class PageFetcher
{
    public abstract PageList getPage(final PageControl p0) throws PageFetchException;
}
