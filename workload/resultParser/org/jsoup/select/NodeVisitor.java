// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.select;

import org.jsoup.nodes.Node;

public interface NodeVisitor
{
    void head(final Node p0, final int p1);
    
    void tail(final Node p0, final int p1);
}
