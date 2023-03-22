// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import java.util.Collections;
import org.jsoup.helper.Validate;
import java.util.List;

abstract class LeafNode extends Node
{
    private static final List<Node> EmptyNodes;
    Object value;
    
    @Override
    protected final boolean hasAttributes() {
        return this.value instanceof Attributes;
    }
    
    @Override
    public final Attributes attributes() {
        this.ensureAttributes();
        return (Attributes)this.value;
    }
    
    private void ensureAttributes() {
        if (!this.hasAttributes()) {
            final Object coreValue = this.value;
            final Attributes attributes = new Attributes();
            this.value = attributes;
            if (coreValue != null) {
                attributes.put(this.nodeName(), (String)coreValue);
            }
        }
    }
    
    String coreValue() {
        return this.attr(this.nodeName());
    }
    
    void coreValue(final String value) {
        this.attr(this.nodeName(), value);
    }
    
    @Override
    public String attr(final String key) {
        Validate.notNull(key);
        if (!this.hasAttributes()) {
            return (String)(key.equals(this.nodeName()) ? this.value : "");
        }
        return super.attr(key);
    }
    
    @Override
    public Node attr(final String key, final String value) {
        if (!this.hasAttributes() && key.equals(this.nodeName())) {
            this.value = value;
        }
        else {
            this.ensureAttributes();
            super.attr(key, value);
        }
        return this;
    }
    
    @Override
    public boolean hasAttr(final String key) {
        this.ensureAttributes();
        return super.hasAttr(key);
    }
    
    @Override
    public Node removeAttr(final String key) {
        this.ensureAttributes();
        return super.removeAttr(key);
    }
    
    @Override
    public String absUrl(final String key) {
        this.ensureAttributes();
        return super.absUrl(key);
    }
    
    @Override
    public String baseUri() {
        return this.hasParent() ? this.parent().baseUri() : "";
    }
    
    @Override
    protected void doSetBaseUri(final String baseUri) {
    }
    
    @Override
    public int childNodeSize() {
        return 0;
    }
    
    @Override
    protected List<Node> ensureChildNodes() {
        return LeafNode.EmptyNodes;
    }
    
    static {
        EmptyNodes = Collections.emptyList();
    }
}
