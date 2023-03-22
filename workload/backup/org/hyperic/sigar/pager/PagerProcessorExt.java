// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.pager;

public interface PagerProcessorExt extends PagerProcessor
{
    PagerEventHandler getEventHandler();
    
    boolean skipNulls();
    
    Object processElement(final Object p0, final Object p1);
}
