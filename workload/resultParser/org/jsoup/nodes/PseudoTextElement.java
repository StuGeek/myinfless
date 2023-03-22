// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.parser.Tag;

public class PseudoTextElement extends Element
{
    public PseudoTextElement(final Tag tag, final String baseUri, final Attributes attributes) {
        super(tag, baseUri, attributes);
    }
    
    @Override
    void outerHtmlHead(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
    }
    
    @Override
    void outerHtmlTail(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
    }
}
