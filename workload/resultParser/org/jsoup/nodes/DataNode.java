// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import java.io.IOException;

public class DataNode extends LeafNode
{
    public DataNode(final String data) {
        this.value = data;
    }
    
    @Deprecated
    public DataNode(final String data, final String baseUri) {
        this(data);
    }
    
    @Override
    public String nodeName() {
        return "#data";
    }
    
    public String getWholeData() {
        return this.coreValue();
    }
    
    public DataNode setWholeData(final String data) {
        this.coreValue(data);
        return this;
    }
    
    @Override
    void outerHtmlHead(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
        accum.append(this.getWholeData());
    }
    
    @Override
    void outerHtmlTail(final Appendable accum, final int depth, final Document.OutputSettings out) {
    }
    
    @Override
    public String toString() {
        return this.outerHtml();
    }
    
    public static DataNode createFromEncoded(final String encodedData, final String baseUri) {
        final String data = Entities.unescape(encodedData);
        return new DataNode(data);
    }
}
