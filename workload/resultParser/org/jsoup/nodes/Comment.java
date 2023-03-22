// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import java.io.IOException;

public class Comment extends LeafNode
{
    private static final String COMMENT_KEY = "comment";
    
    public Comment(final String data) {
        this.value = data;
    }
    
    @Deprecated
    public Comment(final String data, final String baseUri) {
        this(data);
    }
    
    @Override
    public String nodeName() {
        return "#comment";
    }
    
    public String getData() {
        return this.coreValue();
    }
    
    @Override
    void outerHtmlHead(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
        if (out.prettyPrint()) {
            this.indent(accum, depth, out);
        }
        accum.append("<!--").append(this.getData()).append("-->");
    }
    
    @Override
    void outerHtmlTail(final Appendable accum, final int depth, final Document.OutputSettings out) {
    }
    
    @Override
    public String toString() {
        return this.outerHtml();
    }
}
