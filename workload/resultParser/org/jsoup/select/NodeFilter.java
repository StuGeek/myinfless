// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.select;

import org.jsoup.nodes.Node;

public interface NodeFilter
{
    FilterResult head(final Node p0, final int p1);
    
    FilterResult tail(final Node p0, final int p1);
    
    public enum FilterResult
    {
        CONTINUE, 
        SKIP_CHILDREN, 
        SKIP_ENTIRELY, 
        REMOVE, 
        STOP;
    }
}
