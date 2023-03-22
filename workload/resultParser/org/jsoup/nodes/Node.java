// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import org.jsoup.SerializationException;
import java.util.LinkedList;
import java.io.IOException;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;
import java.util.Collection;
import java.util.Arrays;
import org.jsoup.parser.Parser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.NodeVisitor;
import java.util.Iterator;
import org.jsoup.helper.Validate;

public abstract class Node implements Cloneable
{
    static final String EmptyString = "";
    Node parentNode;
    int siblingIndex;
    
    protected Node() {
    }
    
    public abstract String nodeName();
    
    protected abstract boolean hasAttributes();
    
    public boolean hasParent() {
        return this.parentNode != null;
    }
    
    public String attr(final String attributeKey) {
        Validate.notNull(attributeKey);
        if (!this.hasAttributes()) {
            return "";
        }
        final String val = this.attributes().getIgnoreCase(attributeKey);
        if (val.length() > 0) {
            return val;
        }
        if (attributeKey.startsWith("abs:")) {
            return this.absUrl(attributeKey.substring("abs:".length()));
        }
        return "";
    }
    
    public abstract Attributes attributes();
    
    public Node attr(final String attributeKey, final String attributeValue) {
        this.attributes().putIgnoreCase(attributeKey, attributeValue);
        return this;
    }
    
    public boolean hasAttr(final String attributeKey) {
        Validate.notNull(attributeKey);
        if (attributeKey.startsWith("abs:")) {
            final String key = attributeKey.substring("abs:".length());
            if (this.attributes().hasKeyIgnoreCase(key) && !this.absUrl(key).equals("")) {
                return true;
            }
        }
        return this.attributes().hasKeyIgnoreCase(attributeKey);
    }
    
    public Node removeAttr(final String attributeKey) {
        Validate.notNull(attributeKey);
        this.attributes().removeIgnoreCase(attributeKey);
        return this;
    }
    
    public Node clearAttributes() {
        final Iterator<Attribute> it = this.attributes().iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        return this;
    }
    
    public abstract String baseUri();
    
    protected abstract void doSetBaseUri(final String p0);
    
    public void setBaseUri(final String baseUri) {
        Validate.notNull(baseUri);
        this.traverse(new NodeVisitor() {
            @Override
            public void head(final Node node, final int depth) {
                node.doSetBaseUri(baseUri);
            }
            
            @Override
            public void tail(final Node node, final int depth) {
            }
        });
    }
    
    public String absUrl(final String attributeKey) {
        Validate.notEmpty(attributeKey);
        if (!this.hasAttr(attributeKey)) {
            return "";
        }
        return StringUtil.resolve(this.baseUri(), this.attr(attributeKey));
    }
    
    protected abstract List<Node> ensureChildNodes();
    
    public Node childNode(final int index) {
        return this.ensureChildNodes().get(index);
    }
    
    public List<Node> childNodes() {
        return Collections.unmodifiableList((List<? extends Node>)this.ensureChildNodes());
    }
    
    public List<Node> childNodesCopy() {
        final List<Node> nodes = this.ensureChildNodes();
        final ArrayList<Node> children = new ArrayList<Node>(nodes.size());
        for (final Node node : nodes) {
            children.add(node.clone());
        }
        return children;
    }
    
    public abstract int childNodeSize();
    
    protected Node[] childNodesAsArray() {
        return this.ensureChildNodes().toArray(new Node[this.childNodeSize()]);
    }
    
    public Node parent() {
        return this.parentNode;
    }
    
    public final Node parentNode() {
        return this.parentNode;
    }
    
    public Node root() {
        Node node;
        for (node = this; node.parentNode != null; node = node.parentNode) {}
        return node;
    }
    
    public Document ownerDocument() {
        final Node root = this.root();
        return (root instanceof Document) ? ((Document)root) : null;
    }
    
    public void remove() {
        Validate.notNull(this.parentNode);
        this.parentNode.removeChild(this);
    }
    
    public Node before(final String html) {
        this.addSiblingHtml(this.siblingIndex, html);
        return this;
    }
    
    public Node before(final Node node) {
        Validate.notNull(node);
        Validate.notNull(this.parentNode);
        this.parentNode.addChildren(this.siblingIndex, node);
        return this;
    }
    
    public Node after(final String html) {
        this.addSiblingHtml(this.siblingIndex + 1, html);
        return this;
    }
    
    public Node after(final Node node) {
        Validate.notNull(node);
        Validate.notNull(this.parentNode);
        this.parentNode.addChildren(this.siblingIndex + 1, node);
        return this;
    }
    
    private void addSiblingHtml(final int index, final String html) {
        Validate.notNull(html);
        Validate.notNull(this.parentNode);
        final Element context = (this.parent() instanceof Element) ? ((Element)this.parent()) : null;
        final List<Node> nodes = Parser.parseFragment(html, context, this.baseUri());
        this.parentNode.addChildren(index, (Node[])nodes.toArray(new Node[nodes.size()]));
    }
    
    public Node wrap(final String html) {
        Validate.notEmpty(html);
        final Element context = (this.parent() instanceof Element) ? ((Element)this.parent()) : null;
        final List<Node> wrapChildren = Parser.parseFragment(html, context, this.baseUri());
        final Node wrapNode = wrapChildren.get(0);
        if (wrapNode == null || !(wrapNode instanceof Element)) {
            return null;
        }
        final Element wrap = (Element)wrapNode;
        final Element deepest = this.getDeepChild(wrap);
        this.parentNode.replaceChild(this, wrap);
        deepest.addChildren(this);
        if (wrapChildren.size() > 0) {
            for (int i = 0; i < wrapChildren.size(); ++i) {
                final Node remainder = wrapChildren.get(i);
                remainder.parentNode.removeChild(remainder);
                wrap.appendChild(remainder);
            }
        }
        return this;
    }
    
    public Node unwrap() {
        Validate.notNull(this.parentNode);
        final List<Node> childNodes = this.ensureChildNodes();
        final Node firstChild = (childNodes.size() > 0) ? childNodes.get(0) : null;
        this.parentNode.addChildren(this.siblingIndex, this.childNodesAsArray());
        this.remove();
        return firstChild;
    }
    
    private Element getDeepChild(final Element el) {
        final List<Element> children = el.children();
        if (children.size() > 0) {
            return this.getDeepChild(children.get(0));
        }
        return el;
    }
    
    void nodelistChanged() {
    }
    
    public void replaceWith(final Node in) {
        Validate.notNull(in);
        Validate.notNull(this.parentNode);
        this.parentNode.replaceChild(this, in);
    }
    
    protected void setParentNode(final Node parentNode) {
        Validate.notNull(parentNode);
        if (this.parentNode != null) {
            this.parentNode.removeChild(this);
        }
        this.parentNode = parentNode;
    }
    
    protected void replaceChild(final Node out, final Node in) {
        Validate.isTrue(out.parentNode == this);
        Validate.notNull(in);
        if (in.parentNode != null) {
            in.parentNode.removeChild(in);
        }
        final int index = out.siblingIndex;
        this.ensureChildNodes().set(index, in);
        in.parentNode = this;
        in.setSiblingIndex(index);
        out.parentNode = null;
    }
    
    protected void removeChild(final Node out) {
        Validate.isTrue(out.parentNode == this);
        final int index = out.siblingIndex;
        this.ensureChildNodes().remove(index);
        this.reindexChildren(index);
        out.parentNode = null;
    }
    
    protected void addChildren(final Node... children) {
        final List<Node> nodes = this.ensureChildNodes();
        for (final Node child : children) {
            this.reparentChild(child);
            nodes.add(child);
            child.setSiblingIndex(nodes.size() - 1);
        }
    }
    
    protected void addChildren(final int index, final Node... children) {
        Validate.noNullElements(children);
        final List<Node> nodes = this.ensureChildNodes();
        for (final Node child : children) {
            this.reparentChild(child);
        }
        nodes.addAll(index, Arrays.asList(children));
        this.reindexChildren(index);
    }
    
    protected void reparentChild(final Node child) {
        child.setParentNode(this);
    }
    
    private void reindexChildren(final int start) {
        final List<Node> childNodes = this.ensureChildNodes();
        for (int i = start; i < childNodes.size(); ++i) {
            childNodes.get(i).setSiblingIndex(i);
        }
    }
    
    public List<Node> siblingNodes() {
        if (this.parentNode == null) {
            return Collections.emptyList();
        }
        final List<Node> nodes = this.parentNode.ensureChildNodes();
        final List<Node> siblings = new ArrayList<Node>(nodes.size() - 1);
        for (final Node node : nodes) {
            if (node != this) {
                siblings.add(node);
            }
        }
        return siblings;
    }
    
    public Node nextSibling() {
        if (this.parentNode == null) {
            return null;
        }
        final List<Node> siblings = this.parentNode.ensureChildNodes();
        final int index = this.siblingIndex + 1;
        if (siblings.size() > index) {
            return siblings.get(index);
        }
        return null;
    }
    
    public Node previousSibling() {
        if (this.parentNode == null) {
            return null;
        }
        if (this.siblingIndex > 0) {
            return this.parentNode.ensureChildNodes().get(this.siblingIndex - 1);
        }
        return null;
    }
    
    public int siblingIndex() {
        return this.siblingIndex;
    }
    
    protected void setSiblingIndex(final int siblingIndex) {
        this.siblingIndex = siblingIndex;
    }
    
    public Node traverse(final NodeVisitor nodeVisitor) {
        Validate.notNull(nodeVisitor);
        NodeTraversor.traverse(nodeVisitor, this);
        return this;
    }
    
    public Node filter(final NodeFilter nodeFilter) {
        Validate.notNull(nodeFilter);
        NodeTraversor.filter(nodeFilter, this);
        return this;
    }
    
    public String outerHtml() {
        final StringBuilder accum = new StringBuilder(128);
        this.outerHtml(accum);
        return accum.toString();
    }
    
    protected void outerHtml(final Appendable accum) {
        NodeTraversor.traverse(new OuterHtmlVisitor(accum, this.getOutputSettings()), this);
    }
    
    Document.OutputSettings getOutputSettings() {
        final Document owner = this.ownerDocument();
        return (owner != null) ? owner.outputSettings() : new Document("").outputSettings();
    }
    
    abstract void outerHtmlHead(final Appendable p0, final int p1, final Document.OutputSettings p2) throws IOException;
    
    abstract void outerHtmlTail(final Appendable p0, final int p1, final Document.OutputSettings p2) throws IOException;
    
    public <T extends Appendable> T html(final T appendable) {
        this.outerHtml(appendable);
        return appendable;
    }
    
    @Override
    public String toString() {
        return this.outerHtml();
    }
    
    protected void indent(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
        accum.append('\n').append(StringUtil.padding(depth * out.indentAmount()));
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o;
    }
    
    public boolean hasSameValue(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.outerHtml().equals(((Node)o).outerHtml()));
    }
    
    public Node clone() {
        final Node thisClone = this.doClone(null);
        final LinkedList<Node> nodesToProcess = new LinkedList<Node>();
        nodesToProcess.add(thisClone);
        while (!nodesToProcess.isEmpty()) {
            final Node currParent = nodesToProcess.remove();
            for (int size = currParent.childNodeSize(), i = 0; i < size; ++i) {
                final List<Node> childNodes = currParent.ensureChildNodes();
                final Node childClone = childNodes.get(i).doClone(currParent);
                childNodes.set(i, childClone);
                nodesToProcess.add(childClone);
            }
        }
        return thisClone;
    }
    
    public Node shallowClone() {
        return this.doClone(null);
    }
    
    protected Node doClone(final Node parent) {
        Node clone;
        try {
            clone = (Node)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        clone.parentNode = parent;
        clone.siblingIndex = ((parent == null) ? 0 : this.siblingIndex);
        return clone;
    }
    
    private static class OuterHtmlVisitor implements NodeVisitor
    {
        private Appendable accum;
        private Document.OutputSettings out;
        
        OuterHtmlVisitor(final Appendable accum, final Document.OutputSettings out) {
            this.accum = accum;
            (this.out = out).prepareEncoder();
        }
        
        @Override
        public void head(final Node node, final int depth) {
            try {
                node.outerHtmlHead(this.accum, depth, this.out);
            }
            catch (IOException exception) {
                throw new SerializationException(exception);
            }
        }
        
        @Override
        public void tail(final Node node, final int depth) {
            if (!node.nodeName().equals("#text")) {
                try {
                    node.outerHtmlTail(this.accum, depth, this.out);
                }
                catch (IOException exception) {
                    throw new SerializationException(exception);
                }
            }
        }
    }
}
