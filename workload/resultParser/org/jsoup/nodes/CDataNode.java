// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class CDataNode extends TextNode
{
    public CDataNode(final String text) {
        super(text);
    }
    
    @Override
    public String nodeName() {
        return "#cdata";
    }
    
    @Override
    public String text() {
        return this.getWholeText();
    }
    
    @Override
    void outerHtmlHead(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
        accum.append("<![CDATA[").append(this.getWholeText());
    }
    
    @Override
    void outerHtmlTail(final Appendable accum, final int depth, final Document.OutputSettings out) {
        try {
            accum.append("]]>");
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
