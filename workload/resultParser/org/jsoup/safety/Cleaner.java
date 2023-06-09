// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.safety;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.TextNode;
import java.util.Iterator;
import org.jsoup.nodes.Attribute;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Attributes;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.NodeTraversor;
import org.jsoup.nodes.Element;
import java.util.List;
import org.jsoup.nodes.Node;
import java.util.Collection;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

public class Cleaner
{
    private Whitelist whitelist;
    
    public Cleaner(final Whitelist whitelist) {
        Validate.notNull(whitelist);
        this.whitelist = whitelist;
    }
    
    public Document clean(final Document dirtyDocument) {
        Validate.notNull(dirtyDocument);
        final Document clean = Document.createShell(dirtyDocument.baseUri());
        if (dirtyDocument.body() != null) {
            this.copySafeNodes(dirtyDocument.body(), clean.body());
        }
        return clean;
    }
    
    public boolean isValid(final Document dirtyDocument) {
        Validate.notNull(dirtyDocument);
        final Document clean = Document.createShell(dirtyDocument.baseUri());
        final int numDiscarded = this.copySafeNodes(dirtyDocument.body(), clean.body());
        return numDiscarded == 0 && dirtyDocument.head().childNodes().size() == 0;
    }
    
    public boolean isValidBodyHtml(final String bodyHtml) {
        final Document clean = Document.createShell("");
        final Document dirty = Document.createShell("");
        final ParseErrorList errorList = ParseErrorList.tracking(1);
        final List<Node> nodes = Parser.parseFragment(bodyHtml, dirty.body(), "", errorList);
        dirty.body().insertChildren(0, nodes);
        final int numDiscarded = this.copySafeNodes(dirty.body(), clean.body());
        return numDiscarded == 0 && errorList.size() == 0;
    }
    
    private int copySafeNodes(final Element source, final Element dest) {
        final CleaningVisitor cleaningVisitor = new CleaningVisitor(source, dest);
        NodeTraversor.traverse(cleaningVisitor, source);
        return cleaningVisitor.numDiscarded;
    }
    
    private ElementMeta createSafeElement(final Element sourceEl) {
        final String sourceTag = sourceEl.tagName();
        final Attributes destAttrs = new Attributes();
        final Element dest = new Element(Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
        int numDiscarded = 0;
        final Attributes sourceAttrs = sourceEl.attributes();
        for (final Attribute sourceAttr : sourceAttrs) {
            if (this.whitelist.isSafeAttribute(sourceTag, sourceEl, sourceAttr)) {
                destAttrs.put(sourceAttr);
            }
            else {
                ++numDiscarded;
            }
        }
        final Attributes enforcedAttrs = this.whitelist.getEnforcedAttributes(sourceTag);
        destAttrs.addAll(enforcedAttrs);
        return new ElementMeta(dest, numDiscarded);
    }
    
    private final class CleaningVisitor implements NodeVisitor
    {
        private int numDiscarded;
        private final Element root;
        private Element destination;
        
        private CleaningVisitor(final Element root, final Element destination) {
            this.numDiscarded = 0;
            this.root = root;
            this.destination = destination;
        }
        
        @Override
        public void head(final Node source, final int depth) {
            if (source instanceof Element) {
                final Element sourceEl = (Element)source;
                if (Cleaner.this.whitelist.isSafeTag(sourceEl.tagName())) {
                    final ElementMeta meta = Cleaner.this.createSafeElement(sourceEl);
                    final Element destChild = meta.el;
                    this.destination.appendChild(destChild);
                    this.numDiscarded += meta.numAttribsDiscarded;
                    this.destination = destChild;
                }
                else if (source != this.root) {
                    ++this.numDiscarded;
                }
            }
            else if (source instanceof TextNode) {
                final TextNode sourceText = (TextNode)source;
                final TextNode destText = new TextNode(sourceText.getWholeText());
                this.destination.appendChild(destText);
            }
            else if (source instanceof DataNode && Cleaner.this.whitelist.isSafeTag(source.parent().nodeName())) {
                final DataNode sourceData = (DataNode)source;
                final DataNode destData = new DataNode(sourceData.getWholeData());
                this.destination.appendChild(destData);
            }
            else {
                ++this.numDiscarded;
            }
        }
        
        @Override
        public void tail(final Node source, final int depth) {
            if (source instanceof Element && Cleaner.this.whitelist.isSafeTag(source.nodeName())) {
                this.destination = this.destination.parent();
            }
        }
    }
    
    private static class ElementMeta
    {
        Element el;
        int numAttribsDiscarded;
        
        ElementMeta(final Element el, final int numAttribsDiscarded) {
            this.el = el;
            this.numAttribsDiscarded = numAttribsDiscarded;
        }
    }
}
