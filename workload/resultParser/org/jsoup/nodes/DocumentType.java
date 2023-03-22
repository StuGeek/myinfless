// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import java.io.IOException;
import org.jsoup.helper.Validate;

public class DocumentType extends LeafNode
{
    public static final String PUBLIC_KEY = "PUBLIC";
    public static final String SYSTEM_KEY = "SYSTEM";
    private static final String NAME = "name";
    private static final String PUB_SYS_KEY = "pubSysKey";
    private static final String PUBLIC_ID = "publicId";
    private static final String SYSTEM_ID = "systemId";
    
    public DocumentType(final String name, final String publicId, final String systemId) {
        Validate.notNull(name);
        Validate.notNull(publicId);
        Validate.notNull(systemId);
        this.attr("name", name);
        this.attr("publicId", publicId);
        if (this.has("publicId")) {
            this.attr("pubSysKey", "PUBLIC");
        }
        this.attr("systemId", systemId);
    }
    
    @Deprecated
    public DocumentType(final String name, final String publicId, final String systemId, final String baseUri) {
        this.attr("name", name);
        this.attr("publicId", publicId);
        if (this.has("publicId")) {
            this.attr("pubSysKey", "PUBLIC");
        }
        this.attr("systemId", systemId);
    }
    
    @Deprecated
    public DocumentType(final String name, final String pubSysKey, final String publicId, final String systemId, final String baseUri) {
        this.attr("name", name);
        if (pubSysKey != null) {
            this.attr("pubSysKey", pubSysKey);
        }
        this.attr("publicId", publicId);
        this.attr("systemId", systemId);
    }
    
    public void setPubSysKey(final String value) {
        if (value != null) {
            this.attr("pubSysKey", value);
        }
    }
    
    @Override
    public String nodeName() {
        return "#doctype";
    }
    
    @Override
    void outerHtmlHead(final Appendable accum, final int depth, final Document.OutputSettings out) throws IOException {
        if (out.syntax() == Document.OutputSettings.Syntax.html && !this.has("publicId") && !this.has("systemId")) {
            accum.append("<!doctype");
        }
        else {
            accum.append("<!DOCTYPE");
        }
        if (this.has("name")) {
            accum.append(" ").append(this.attr("name"));
        }
        if (this.has("pubSysKey")) {
            accum.append(" ").append(this.attr("pubSysKey"));
        }
        if (this.has("publicId")) {
            accum.append(" \"").append(this.attr("publicId")).append('\"');
        }
        if (this.has("systemId")) {
            accum.append(" \"").append(this.attr("systemId")).append('\"');
        }
        accum.append('>');
    }
    
    @Override
    void outerHtmlTail(final Appendable accum, final int depth, final Document.OutputSettings out) {
    }
    
    private boolean has(final String attribute) {
        return !StringUtil.isBlank(this.attr(attribute));
    }
}
