// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import java.util.Iterator;
import java.io.IOException;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;

public class XmlDeclaration extends LeafNode
{
    private final boolean isProcessingInstruction;
    
    public XmlDeclaration(final String name, final boolean isProcessingInstruction) {
        Validate.notNull(name);
        this.value = name;
        this.isProcessingInstruction = isProcessingInstruction;
    }
    
    @Deprecated
    public XmlDeclaration(final String name, final String baseUri, final boolean isProcessingInstruction) {
        this(name, isProcessingInstruction);
    }
    
    @Override
    public String nodeName() {
        return "#declaration";
    }
    
    public String name() {
        return this.coreValue();
    }
    
    public String getWholeDeclaration() {
        final StringBuilder sb = new StringBuilder();
        try {
            this.getWholeDeclaration(sb, new Document.OutputSettings());
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
        return sb.toString().trim();
    }
    
    private void getWholeDeclaration(final Appendable accum, final Document.OutputSettings out) throws IOException {
        for (final Attribute attribute : this.attributes()) {
            if (!attribute.getKey().equals(this.nodeName())) {
                accum.append(' ');
                attribute.html(accum, out);
            }
        }
    }
    
    @Override
    void outerHtmlHead(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
        accum.append("<").append(this.isProcessingInstruction ? "!" : "?").append(this.coreValue());
        this.getWholeDeclaration(accum, out);
        accum.append(this.isProcessingInstruction ? "!" : "?").append(">");
    }
    
    @Override
    void outerHtmlTail(final Appendable accum, final int depth, final Document.OutputSettings out) {
    }
    
    @Override
    public String toString() {
        return this.outerHtml();
    }
}
