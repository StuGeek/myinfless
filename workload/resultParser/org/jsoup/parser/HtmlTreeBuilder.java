// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import java.util.Iterator;
import org.jsoup.select.Elements;
import org.jsoup.helper.StringUtil;
import java.io.StringReader;
import org.jsoup.nodes.Node;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Element;

public class HtmlTreeBuilder extends TreeBuilder
{
    static final String[] TagsSearchInScope;
    static final String[] TagSearchList;
    static final String[] TagSearchButton;
    static final String[] TagSearchTableScope;
    static final String[] TagSearchSelectScope;
    static final String[] TagSearchEndTags;
    static final String[] TagSearchSpecial;
    public static final int MaxScopeSearchDepth = 100;
    private HtmlTreeBuilderState state;
    private HtmlTreeBuilderState originalState;
    private boolean baseUriSetFromDoc;
    private Element headElement;
    private FormElement formElement;
    private Element contextElement;
    private ArrayList<Element> formattingElements;
    private List<String> pendingTableCharacters;
    private Token.EndTag emptyEnd;
    private boolean framesetOk;
    private boolean fosterInserts;
    private boolean fragmentParsing;
    private String[] specificScopeTarget;
    
    public HtmlTreeBuilder() {
        this.specificScopeTarget = new String[] { null };
    }
    
    @Override
    ParseSettings defaultSettings() {
        return ParseSettings.htmlDefault;
    }
    
    @Override
    protected void initialiseParse(final Reader input, final String baseUri, final ParseErrorList errors, final ParseSettings settings) {
        super.initialiseParse(input, baseUri, errors, settings);
        this.state = HtmlTreeBuilderState.Initial;
        this.originalState = null;
        this.baseUriSetFromDoc = false;
        this.headElement = null;
        this.formElement = null;
        this.contextElement = null;
        this.formattingElements = new ArrayList<Element>();
        this.pendingTableCharacters = new ArrayList<String>();
        this.emptyEnd = new Token.EndTag();
        this.framesetOk = true;
        this.fosterInserts = false;
        this.fragmentParsing = false;
    }
    
    List<Node> parseFragment(final String inputFragment, final Element context, final String baseUri, final ParseErrorList errors, final ParseSettings settings) {
        this.state = HtmlTreeBuilderState.Initial;
        this.initialiseParse(new StringReader(inputFragment), baseUri, errors, settings);
        this.contextElement = context;
        this.fragmentParsing = true;
        Element root = null;
        if (context != null) {
            if (context.ownerDocument() != null) {
                this.doc.quirksMode(context.ownerDocument().quirksMode());
            }
            final String contextTag = context.tagName();
            if (StringUtil.in(contextTag, "title", "textarea")) {
                this.tokeniser.transition(TokeniserState.Rcdata);
            }
            else if (StringUtil.in(contextTag, "iframe", "noembed", "noframes", "style", "xmp")) {
                this.tokeniser.transition(TokeniserState.Rawtext);
            }
            else if (contextTag.equals("script")) {
                this.tokeniser.transition(TokeniserState.ScriptData);
            }
            else if (contextTag.equals("noscript")) {
                this.tokeniser.transition(TokeniserState.Data);
            }
            else if (contextTag.equals("plaintext")) {
                this.tokeniser.transition(TokeniserState.Data);
            }
            else {
                this.tokeniser.transition(TokeniserState.Data);
            }
            root = new Element(Tag.valueOf("html", settings), baseUri);
            this.doc.appendChild(root);
            this.stack.add(root);
            this.resetInsertionMode();
            final Elements contextChain = context.parents();
            contextChain.add(0, context);
            for (final Element parent : contextChain) {
                if (parent instanceof FormElement) {
                    this.formElement = (FormElement)parent;
                    break;
                }
            }
        }
        this.runParser();
        if (context != null) {
            return root.childNodes();
        }
        return this.doc.childNodes();
    }
    
    @Override
    protected boolean process(final Token token) {
        this.currentToken = token;
        return this.state.process(token, this);
    }
    
    boolean process(final Token token, final HtmlTreeBuilderState state) {
        this.currentToken = token;
        return state.process(token, this);
    }
    
    void transition(final HtmlTreeBuilderState state) {
        this.state = state;
    }
    
    HtmlTreeBuilderState state() {
        return this.state;
    }
    
    void markInsertionMode() {
        this.originalState = this.state;
    }
    
    HtmlTreeBuilderState originalState() {
        return this.originalState;
    }
    
    void framesetOk(final boolean framesetOk) {
        this.framesetOk = framesetOk;
    }
    
    boolean framesetOk() {
        return this.framesetOk;
    }
    
    Document getDocument() {
        return this.doc;
    }
    
    String getBaseUri() {
        return this.baseUri;
    }
    
    void maybeSetBaseUri(final Element base) {
        if (this.baseUriSetFromDoc) {
            return;
        }
        final String href = base.absUrl("href");
        if (href.length() != 0) {
            this.baseUri = href;
            this.baseUriSetFromDoc = true;
            this.doc.setBaseUri(href);
        }
    }
    
    boolean isFragmentParsing() {
        return this.fragmentParsing;
    }
    
    void error(final HtmlTreeBuilderState state) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Unexpected token [%s] when in state [%s]", new Object[] { this.currentToken.tokenType(), state }));
        }
    }
    
    Element insert(final Token.StartTag startTag) {
        if (startTag.isSelfClosing()) {
            final Element el = this.insertEmpty(startTag);
            this.stack.add(el);
            this.tokeniser.transition(TokeniserState.Data);
            this.tokeniser.emit(this.emptyEnd.reset().name(el.tagName()));
            return el;
        }
        final Element el = new Element(Tag.valueOf(startTag.name(), this.settings), this.baseUri, this.settings.normalizeAttributes(startTag.attributes));
        this.insert(el);
        return el;
    }
    
    Element insertStartTag(final String startTagName) {
        final Element el = new Element(Tag.valueOf(startTagName, this.settings), this.baseUri);
        this.insert(el);
        return el;
    }
    
    void insert(final Element el) {
        this.insertNode(el);
        this.stack.add(el);
    }
    
    Element insertEmpty(final Token.StartTag startTag) {
        final Tag tag = Tag.valueOf(startTag.name(), this.settings);
        final Element el = new Element(tag, this.baseUri, startTag.attributes);
        this.insertNode(el);
        if (startTag.isSelfClosing()) {
            if (tag.isKnownTag()) {
                if (!tag.isEmpty()) {
                    this.tokeniser.error("Tag cannot be self closing; not a void tag");
                }
            }
            else {
                tag.setSelfClosing();
            }
        }
        return el;
    }
    
    FormElement insertForm(final Token.StartTag startTag, final boolean onStack) {
        final Tag tag = Tag.valueOf(startTag.name(), this.settings);
        final FormElement el = new FormElement(tag, this.baseUri, startTag.attributes);
        this.setFormElement(el);
        this.insertNode(el);
        if (onStack) {
            this.stack.add(el);
        }
        return el;
    }
    
    void insert(final Token.Comment commentToken) {
        final Comment comment = new Comment(commentToken.getData());
        this.insertNode(comment);
    }
    
    void insert(final Token.Character characterToken) {
        final String tagName = this.currentElement().tagName();
        final String data = characterToken.getData();
        Node node;
        if (characterToken.isCData()) {
            node = new CDataNode(data);
        }
        else if (tagName.equals("script") || tagName.equals("style")) {
            node = new DataNode(data);
        }
        else {
            node = new TextNode(data);
        }
        this.currentElement().appendChild(node);
    }
    
    private void insertNode(final Node node) {
        if (this.stack.size() == 0) {
            this.doc.appendChild(node);
        }
        else if (this.isFosterInserts()) {
            this.insertInFosterParent(node);
        }
        else {
            this.currentElement().appendChild(node);
        }
        if (node instanceof Element && ((Element)node).tag().isFormListed() && this.formElement != null) {
            this.formElement.addElement((Element)node);
        }
    }
    
    Element pop() {
        final int size = this.stack.size();
        return this.stack.remove(size - 1);
    }
    
    void push(final Element element) {
        this.stack.add(element);
    }
    
    ArrayList<Element> getStack() {
        return this.stack;
    }
    
    boolean onStack(final Element el) {
        return this.isElementInQueue(this.stack, el);
    }
    
    private boolean isElementInQueue(final ArrayList<Element> queue, final Element element) {
        for (int pos = queue.size() - 1; pos >= 0; --pos) {
            final Element next = queue.get(pos);
            if (next == element) {
                return true;
            }
        }
        return false;
    }
    
    Element getFromStack(final String elName) {
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            if (next.nodeName().equals(elName)) {
                return next;
            }
        }
        return null;
    }
    
    boolean removeFromStack(final Element el) {
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            if (next == el) {
                this.stack.remove(pos);
                return true;
            }
        }
        return false;
    }
    
    void popStackToClose(final String elName) {
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            this.stack.remove(pos);
            if (next.nodeName().equals(elName)) {
                break;
            }
        }
    }
    
    void popStackToClose(final String... elNames) {
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            this.stack.remove(pos);
            if (StringUtil.inSorted(next.nodeName(), elNames)) {
                break;
            }
        }
    }
    
    void popStackToBefore(final String elName) {
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            if (next.nodeName().equals(elName)) {
                break;
            }
            this.stack.remove(pos);
        }
    }
    
    void clearStackToTableContext() {
        this.clearStackToContext("table");
    }
    
    void clearStackToTableBodyContext() {
        this.clearStackToContext("tbody", "tfoot", "thead", "template");
    }
    
    void clearStackToTableRowContext() {
        this.clearStackToContext("tr", "template");
    }
    
    private void clearStackToContext(final String... nodeNames) {
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            if (StringUtil.in(next.nodeName(), nodeNames)) {
                break;
            }
            if (next.nodeName().equals("html")) {
                break;
            }
            this.stack.remove(pos);
        }
    }
    
    Element aboveOnStack(final Element el) {
        assert this.onStack(el);
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element next = this.stack.get(pos);
            if (next == el) {
                return this.stack.get(pos - 1);
            }
        }
        return null;
    }
    
    void insertOnStackAfter(final Element after, final Element in) {
        final int i = this.stack.lastIndexOf(after);
        Validate.isTrue(i != -1);
        this.stack.add(i + 1, in);
    }
    
    void replaceOnStack(final Element out, final Element in) {
        this.replaceInQueue(this.stack, out, in);
    }
    
    private void replaceInQueue(final ArrayList<Element> queue, final Element out, final Element in) {
        final int i = queue.lastIndexOf(out);
        Validate.isTrue(i != -1);
        queue.set(i, in);
    }
    
    void resetInsertionMode() {
        boolean last = false;
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            Element node = this.stack.get(pos);
            if (pos == 0) {
                last = true;
                node = this.contextElement;
            }
            final String name = node.nodeName();
            if ("select".equals(name)) {
                this.transition(HtmlTreeBuilderState.InSelect);
                break;
            }
            if ("td".equals(name) || ("th".equals(name) && !last)) {
                this.transition(HtmlTreeBuilderState.InCell);
                break;
            }
            if ("tr".equals(name)) {
                this.transition(HtmlTreeBuilderState.InRow);
                break;
            }
            if ("tbody".equals(name) || "thead".equals(name) || "tfoot".equals(name)) {
                this.transition(HtmlTreeBuilderState.InTableBody);
                break;
            }
            if ("caption".equals(name)) {
                this.transition(HtmlTreeBuilderState.InCaption);
                break;
            }
            if ("colgroup".equals(name)) {
                this.transition(HtmlTreeBuilderState.InColumnGroup);
                break;
            }
            if ("table".equals(name)) {
                this.transition(HtmlTreeBuilderState.InTable);
                break;
            }
            if ("head".equals(name)) {
                this.transition(HtmlTreeBuilderState.InBody);
                break;
            }
            if ("body".equals(name)) {
                this.transition(HtmlTreeBuilderState.InBody);
                break;
            }
            if ("frameset".equals(name)) {
                this.transition(HtmlTreeBuilderState.InFrameset);
                break;
            }
            if ("html".equals(name)) {
                this.transition(HtmlTreeBuilderState.BeforeHead);
                break;
            }
            if (last) {
                this.transition(HtmlTreeBuilderState.InBody);
                break;
            }
        }
    }
    
    private boolean inSpecificScope(final String targetName, final String[] baseTypes, final String[] extraTypes) {
        this.specificScopeTarget[0] = targetName;
        return this.inSpecificScope(this.specificScopeTarget, baseTypes, extraTypes);
    }
    
    private boolean inSpecificScope(final String[] targetNames, final String[] baseTypes, final String[] extraTypes) {
        final int bottom = this.stack.size() - 1;
        for (int top = (bottom > 100) ? (bottom - 100) : 0, pos = bottom; pos >= top; --pos) {
            final String elName = this.stack.get(pos).nodeName();
            if (StringUtil.inSorted(elName, targetNames)) {
                return true;
            }
            if (StringUtil.inSorted(elName, baseTypes)) {
                return false;
            }
            if (extraTypes != null && StringUtil.inSorted(elName, extraTypes)) {
                return false;
            }
        }
        return false;
    }
    
    boolean inScope(final String[] targetNames) {
        return this.inSpecificScope(targetNames, HtmlTreeBuilder.TagsSearchInScope, null);
    }
    
    boolean inScope(final String targetName) {
        return this.inScope(targetName, null);
    }
    
    boolean inScope(final String targetName, final String[] extras) {
        return this.inSpecificScope(targetName, HtmlTreeBuilder.TagsSearchInScope, extras);
    }
    
    boolean inListItemScope(final String targetName) {
        return this.inScope(targetName, HtmlTreeBuilder.TagSearchList);
    }
    
    boolean inButtonScope(final String targetName) {
        return this.inScope(targetName, HtmlTreeBuilder.TagSearchButton);
    }
    
    boolean inTableScope(final String targetName) {
        return this.inSpecificScope(targetName, HtmlTreeBuilder.TagSearchTableScope, null);
    }
    
    boolean inSelectScope(final String targetName) {
        for (int pos = this.stack.size() - 1; pos >= 0; --pos) {
            final Element el = this.stack.get(pos);
            final String elName = el.nodeName();
            if (elName.equals(targetName)) {
                return true;
            }
            if (!StringUtil.inSorted(elName, HtmlTreeBuilder.TagSearchSelectScope)) {
                return false;
            }
        }
        Validate.fail("Should not be reachable");
        return false;
    }
    
    void setHeadElement(final Element headElement) {
        this.headElement = headElement;
    }
    
    Element getHeadElement() {
        return this.headElement;
    }
    
    boolean isFosterInserts() {
        return this.fosterInserts;
    }
    
    void setFosterInserts(final boolean fosterInserts) {
        this.fosterInserts = fosterInserts;
    }
    
    FormElement getFormElement() {
        return this.formElement;
    }
    
    void setFormElement(final FormElement formElement) {
        this.formElement = formElement;
    }
    
    void newPendingTableCharacters() {
        this.pendingTableCharacters = new ArrayList<String>();
    }
    
    List<String> getPendingTableCharacters() {
        return this.pendingTableCharacters;
    }
    
    void setPendingTableCharacters(final List<String> pendingTableCharacters) {
        this.pendingTableCharacters = pendingTableCharacters;
    }
    
    void generateImpliedEndTags(final String excludeTag) {
        while (excludeTag != null && !this.currentElement().nodeName().equals(excludeTag) && StringUtil.inSorted(this.currentElement().nodeName(), HtmlTreeBuilder.TagSearchEndTags)) {
            this.pop();
        }
    }
    
    void generateImpliedEndTags() {
        this.generateImpliedEndTags(null);
    }
    
    boolean isSpecial(final Element el) {
        final String name = el.nodeName();
        return StringUtil.inSorted(name, HtmlTreeBuilder.TagSearchSpecial);
    }
    
    Element lastFormattingElement() {
        return (this.formattingElements.size() > 0) ? this.formattingElements.get(this.formattingElements.size() - 1) : null;
    }
    
    Element removeLastFormattingElement() {
        final int size = this.formattingElements.size();
        if (size > 0) {
            return this.formattingElements.remove(size - 1);
        }
        return null;
    }
    
    void pushActiveFormattingElements(final Element in) {
        int numSeen = 0;
        for (int pos = this.formattingElements.size() - 1; pos >= 0; --pos) {
            final Element el = this.formattingElements.get(pos);
            if (el == null) {
                break;
            }
            if (this.isSameFormattingElement(in, el)) {
                ++numSeen;
            }
            if (numSeen == 3) {
                this.formattingElements.remove(pos);
                break;
            }
        }
        this.formattingElements.add(in);
    }
    
    private boolean isSameFormattingElement(final Element a, final Element b) {
        return a.nodeName().equals(b.nodeName()) && a.attributes().equals(b.attributes());
    }
    
    void reconstructFormattingElements() {
        final Element last = this.lastFormattingElement();
        if (last == null || this.onStack(last)) {
            return;
        }
        Element entry = last;
        final int size = this.formattingElements.size();
        int pos = size - 1;
        boolean skip = false;
        while (true) {
            while (pos != 0) {
                entry = this.formattingElements.get(--pos);
                if (entry == null || this.onStack(entry)) {
                    do {
                        if (!skip) {
                            entry = this.formattingElements.get(++pos);
                        }
                        Validate.notNull(entry);
                        skip = false;
                        final Element newEl = this.insertStartTag(entry.nodeName());
                        newEl.attributes().addAll(entry.attributes());
                        this.formattingElements.set(pos, newEl);
                    } while (pos != size - 1);
                    return;
                }
            }
            skip = true;
            continue;
        }
    }
    
    void clearFormattingElementsToLastMarker() {
        while (!this.formattingElements.isEmpty()) {
            final Element el = this.removeLastFormattingElement();
            if (el == null) {
                break;
            }
        }
    }
    
    void removeFromActiveFormattingElements(final Element el) {
        for (int pos = this.formattingElements.size() - 1; pos >= 0; --pos) {
            final Element next = this.formattingElements.get(pos);
            if (next == el) {
                this.formattingElements.remove(pos);
                break;
            }
        }
    }
    
    boolean isInActiveFormattingElements(final Element el) {
        return this.isElementInQueue(this.formattingElements, el);
    }
    
    Element getActiveFormattingElement(final String nodeName) {
        for (int pos = this.formattingElements.size() - 1; pos >= 0; --pos) {
            final Element next = this.formattingElements.get(pos);
            if (next == null) {
                break;
            }
            if (next.nodeName().equals(nodeName)) {
                return next;
            }
        }
        return null;
    }
    
    void replaceActiveFormattingElement(final Element out, final Element in) {
        this.replaceInQueue(this.formattingElements, out, in);
    }
    
    void insertMarkerToFormattingElements() {
        this.formattingElements.add(null);
    }
    
    void insertInFosterParent(final Node in) {
        final Element lastTable = this.getFromStack("table");
        boolean isLastTableParent = false;
        Element fosterParent;
        if (lastTable != null) {
            if (lastTable.parent() != null) {
                fosterParent = lastTable.parent();
                isLastTableParent = true;
            }
            else {
                fosterParent = this.aboveOnStack(lastTable);
            }
        }
        else {
            fosterParent = this.stack.get(0);
        }
        if (isLastTableParent) {
            Validate.notNull(lastTable);
            lastTable.before(in);
        }
        else {
            fosterParent.appendChild(in);
        }
    }
    
    @Override
    public String toString() {
        return "TreeBuilder{currentToken=" + this.currentToken + ", state=" + this.state + ", currentElement=" + this.currentElement() + '}';
    }
    
    static {
        TagsSearchInScope = new String[] { "applet", "caption", "html", "marquee", "object", "table", "td", "th" };
        TagSearchList = new String[] { "ol", "ul" };
        TagSearchButton = new String[] { "button" };
        TagSearchTableScope = new String[] { "html", "table" };
        TagSearchSelectScope = new String[] { "optgroup", "option" };
        TagSearchEndTags = new String[] { "dd", "dt", "li", "optgroup", "option", "p", "rp", "rt" };
        TagSearchSpecial = new String[] { "address", "applet", "area", "article", "aside", "base", "basefont", "bgsound", "blockquote", "body", "br", "button", "caption", "center", "col", "colgroup", "command", "dd", "details", "dir", "div", "dl", "dt", "embed", "fieldset", "figcaption", "figure", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hgroup", "hr", "html", "iframe", "img", "input", "isindex", "li", "link", "listing", "marquee", "menu", "meta", "nav", "noembed", "noframes", "noscript", "object", "ol", "p", "param", "plaintext", "pre", "script", "section", "select", "style", "summary", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr", "ul", "wbr", "xmp" };
    }
}
