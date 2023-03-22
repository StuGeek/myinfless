// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.helper.Validate;
import org.jsoup.helper.StringUtil;

public class TextNode extends LeafNode
{
    public TextNode(final String text) {
        this.value = text;
    }
    
    @Deprecated
    public TextNode(final String text, final String baseUri) {
        this(text);
    }
    
    @Override
    public String nodeName() {
        return "#text";
    }
    
    public String text() {
        return StringUtil.normaliseWhitespace(this.getWholeText());
    }
    
    public TextNode text(final String text) {
        this.coreValue(text);
        return this;
    }
    
    public String getWholeText() {
        return this.coreValue();
    }
    
    public boolean isBlank() {
        return StringUtil.isBlank(this.coreValue());
    }
    
    public TextNode splitText(final int offset) {
        final String text = this.coreValue();
        Validate.isTrue(offset >= 0, "Split offset must be not be negative");
        Validate.isTrue(offset < text.length(), "Split offset must not be greater than current text length");
        final String head = text.substring(0, offset);
        final String tail = text.substring(offset);
        this.text(head);
        final TextNode tailNode = new TextNode(tail);
        if (this.parent() != null) {
            this.parent().addChildren(this.siblingIndex() + 1, tailNode);
        }
        return tailNode;
    }
    
    @Override
    void outerHtmlHead(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
        if (out.prettyPrint() && ((this.siblingIndex() == 0 && this.parentNode instanceof Element && ((Element)this.parentNode).tag().formatAsBlock() && !this.isBlank()) || (out.outline() && this.siblingNodes().size() > 0 && !this.isBlank()))) {
            this.indent(accum, depth, out);
        }
        final boolean normaliseWhite = out.prettyPrint() && this.parent() instanceof Element && !Element.preserveWhitespace(this.parent());
        Entities.escape(accum, this.coreValue(), out, false, normaliseWhite, false);
    }
    
    @Override
    void outerHtmlTail(final Appendable accum, final int depth, final Document.OutputSettings out) {
    }
    
    @Override
    public String toString() {
        return this.outerHtml();
    }
    
    @Deprecated
    public static TextNode createFromEncoded(final String encodedText, final String baseUri) {
        final String text = Entities.unescape(encodedText);
        return new TextNode(text);
    }
    
    public static TextNode createFromEncoded(final String encodedText) {
        final String text = Entities.unescape(encodedText);
        return new TextNode(text);
    }
    
    static String normaliseWhitespace(String text) {
        text = StringUtil.normaliseWhitespace(text);
        return text;
    }
    
    static String stripLeadingWhitespace(final String text) {
        return text.replaceFirst("^\\s+", "");
    }
    
    static boolean lastCharIsWhitespace(final StringBuilder sb) {
        return sb.length() != 0 && sb.charAt(sb.length() - 1) == ' ';
    }
}
