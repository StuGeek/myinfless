// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.internal.Normalizer;

public class ParseSettings
{
    public static final ParseSettings htmlDefault;
    public static final ParseSettings preserveCase;
    private final boolean preserveTagCase;
    private final boolean preserveAttributeCase;
    
    public ParseSettings(final boolean tag, final boolean attribute) {
        this.preserveTagCase = tag;
        this.preserveAttributeCase = attribute;
    }
    
    String normalizeTag(String name) {
        name = name.trim();
        if (!this.preserveTagCase) {
            name = Normalizer.lowerCase(name);
        }
        return name;
    }
    
    String normalizeAttribute(String name) {
        name = name.trim();
        if (!this.preserveAttributeCase) {
            name = Normalizer.lowerCase(name);
        }
        return name;
    }
    
    Attributes normalizeAttributes(final Attributes attributes) {
        if (!this.preserveAttributeCase) {
            attributes.normalize();
        }
        return attributes;
    }
    
    static {
        htmlDefault = new ParseSettings(false, false);
        preserveCase = new ParseSettings(true, true);
    }
}
