// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import java.util.List;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import org.jsoup.nodes.Document;
import java.io.Reader;

public class XmlTreeBuilder extends TreeBuilder
{
    @Override
    ParseSettings defaultSettings() {
        return ParseSettings.preserveCase;
    }
    
    Document parse(final Reader input, final String baseUri) {
        return this.parse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }
    
    Document parse(final String input, final String baseUri) {
        return this.parse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }
    
    @Override
    protected void initialiseParse(final Reader input, final String baseUri, final ParseErrorList errors, final ParseSettings settings) {
        super.initialiseParse(input, baseUri, errors, settings);
        this.stack.add(this.doc);
        this.doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    }
    
    @Override
    protected boolean process(final Token token) {
        switch (token.type) {
            case StartTag: {
                this.insert(token.asStartTag());
                break;
            }
            case EndTag: {
                this.popStackToClose(token.asEndTag());
                break;
            }
            case Comment: {
                this.insert(token.asComment());
                break;
            }
            case Character: {
                this.insert(token.asCharacter());
                break;
            }
            case Doctype: {
                this.insert(token.asDoctype());
                break;
            }
            case EOF: {
                break;
            }
            default: {
                Validate.fail("Unexpected token type: " + token.type);
                break;
            }
        }
        return true;
    }
    
    private void insertNode(final Node node) {
        this.currentElement().appendChild(node);
    }
    
    Element insert(final Token.StartTag startTag) {
        final Tag tag = Tag.valueOf(startTag.name(), this.settings);
        final Element el = new Element(tag, this.baseUri, this.settings.normalizeAttributes(startTag.attributes));
        this.insertNode(el);
        if (startTag.isSelfClosing()) {
            if (!tag.isKnownTag()) {
                tag.setSelfClosing();
            }
        }
        else {
            this.stack.add(el);
        }
        return el;
    }
    
    void insert(final Token.Comment commentToken) {
        Node insert;
        final Comment comment = (Comment)(insert = new Comment(commentToken.getData()));
        if (commentToken.bogus) {
            final String data = comment.getData();
            if (data.length() > 1 && (data.startsWith("!") || data.startsWith("?"))) {
                final Document doc = Jsoup.parse("<" + data.substring(1, data.length() - 1) + ">", this.baseUri, Parser.xmlParser());
                if (doc.childNodeSize() > 0) {
                    final Element el = doc.child(0);
                    insert = new XmlDeclaration(this.settings.normalizeTag(el.tagName()), data.startsWith("!"));
                    insert.attributes().addAll(el.attributes());
                }
            }
        }
        this.insertNode(insert);
    }
    
    void insert(final Token.Character token) {
        final String data = token.getData();
        this.insertNode(token.isCData() ? new CDataNode(data) : new TextNode(data));
    }
    
    void insert(final Token.Doctype d) {
        final DocumentType doctypeNode = new DocumentType(this.settings.normalizeTag(d.getName()), d.getPublicIdentifier(), d.getSystemIdentifier());
        doctypeNode.setPubSysKey(d.getPubSysKey());
        this.insertNode(doctypeNode);
    }
    
    private void popStackToClose(final Token.EndTag endTag) {
        final String elName = this.settings.normalizeTag(endTag.tagName);
        Element firstFound = null;
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            if (next.nodeName().equals(elName)) {
                firstFound = next;
                break;
            }
        }
        if (firstFound == null) {
            return;
        }
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            this.stack.remove(pos);
            if (next == firstFound) {
                break;
            }
        }
    }
    
    List<Node> parseFragment(final String inputFragment, final String baseUri, final ParseErrorList errors, final ParseSettings settings) {
        this.initialiseParse(new StringReader(inputFragment), baseUri, errors, settings);
        this.runParser();
        return this.doc.childNodes();
    }
}
