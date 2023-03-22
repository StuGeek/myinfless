// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.helper;

import org.jsoup.nodes.Attributes;
import java.util.Iterator;
import org.jsoup.nodes.Attribute;
import org.w3c.dom.Text;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.TextNode;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import java.io.Writer;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import javax.xml.transform.dom.DOMSource;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.NodeTraversor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.jsoup.nodes.Document;
import javax.xml.parsers.DocumentBuilderFactory;

public class W3CDom
{
    protected DocumentBuilderFactory factory;
    
    public W3CDom() {
        this.factory = DocumentBuilderFactory.newInstance();
    }
    
    public org.w3c.dom.Document fromJsoup(final Document in) {
        Validate.notNull(in);
        try {
            this.factory.setNamespaceAware(true);
            final DocumentBuilder builder = this.factory.newDocumentBuilder();
            final org.w3c.dom.Document out = builder.newDocument();
            this.convert(in, out);
            return out;
        }
        catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void convert(final Document in, final org.w3c.dom.Document out) {
        if (!StringUtil.isBlank(in.location())) {
            out.setDocumentURI(in.location());
        }
        final Element rootEl = in.child(0);
        NodeTraversor.traverse(new W3CBuilder(out), rootEl);
    }
    
    public String asString(final org.w3c.dom.Document doc) {
        try {
            final DOMSource domSource = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch (TransformerException e) {
            throw new IllegalStateException(e);
        }
    }
    
    protected static class W3CBuilder implements NodeVisitor
    {
        private static final String xmlnsKey = "xmlns";
        private static final String xmlnsPrefix = "xmlns:";
        private final org.w3c.dom.Document doc;
        private final Stack<HashMap<String, String>> namespacesStack;
        private org.w3c.dom.Element dest;
        
        public W3CBuilder(final org.w3c.dom.Document doc) {
            this.namespacesStack = new Stack<HashMap<String, String>>();
            this.doc = doc;
            this.namespacesStack.push(new HashMap<String, String>());
        }
        
        @Override
        public void head(final Node source, final int depth) {
            this.namespacesStack.push(new HashMap<String, String>(this.namespacesStack.peek()));
            if (source instanceof Element) {
                final Element sourceEl = (Element)source;
                final String prefix = this.updateNamespaces(sourceEl);
                final String namespace = this.namespacesStack.peek().get(prefix);
                final org.w3c.dom.Element el = this.doc.createElementNS(namespace, sourceEl.tagName());
                this.copyAttributes(sourceEl, el);
                if (this.dest == null) {
                    this.doc.appendChild(el);
                }
                else {
                    this.dest.appendChild(el);
                }
                this.dest = el;
            }
            else if (source instanceof TextNode) {
                final TextNode sourceText = (TextNode)source;
                final Text text = this.doc.createTextNode(sourceText.getWholeText());
                this.dest.appendChild(text);
            }
            else if (source instanceof Comment) {
                final Comment sourceComment = (Comment)source;
                final org.w3c.dom.Comment comment = this.doc.createComment(sourceComment.getData());
                this.dest.appendChild(comment);
            }
            else if (source instanceof DataNode) {
                final DataNode sourceData = (DataNode)source;
                final Text node = this.doc.createTextNode(sourceData.getWholeData());
                this.dest.appendChild(node);
            }
        }
        
        @Override
        public void tail(final Node source, final int depth) {
            if (source instanceof Element && this.dest.getParentNode() instanceof org.w3c.dom.Element) {
                this.dest = (org.w3c.dom.Element)this.dest.getParentNode();
            }
            this.namespacesStack.pop();
        }
        
        private void copyAttributes(final Node source, final org.w3c.dom.Element el) {
            for (final Attribute attribute : source.attributes()) {
                final String key = attribute.getKey().replaceAll("[^-a-zA-Z0-9_:.]", "");
                if (key.matches("[a-zA-Z_:][-a-zA-Z0-9_:.]*")) {
                    el.setAttribute(key, attribute.getValue());
                }
            }
        }
        
        private String updateNamespaces(final Element el) {
            final Attributes attributes = el.attributes();
            for (final Attribute attr : attributes) {
                final String key = attr.getKey();
                String prefix;
                if (key.equals("xmlns")) {
                    prefix = "";
                }
                else {
                    if (!key.startsWith("xmlns:")) {
                        continue;
                    }
                    prefix = key.substring("xmlns:".length());
                }
                this.namespacesStack.peek().put(prefix, attr.getValue());
            }
            final int pos = el.tagName().indexOf(":");
            return (pos > 0) ? el.tagName().substring(0, pos) : "";
        }
    }
}
