// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.nodes;

import java.util.Arrays;
import java.io.IOException;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import java.util.Map;

public class Attribute implements Map.Entry<String, String>, Cloneable
{
    private static final String[] booleanAttributes;
    private String key;
    private String val;
    Attributes parent;
    
    public Attribute(final String key, final String value) {
        this(key, value, null);
    }
    
    public Attribute(final String key, final String val, final Attributes parent) {
        Validate.notNull(key);
        this.key = key.trim();
        Validate.notEmpty(key);
        this.val = val;
        this.parent = parent;
    }
    
    @Override
    public String getKey() {
        return this.key;
    }
    
    public void setKey(String key) {
        Validate.notNull(key);
        key = key.trim();
        Validate.notEmpty(key);
        if (this.parent != null) {
            final int i = this.parent.indexOfKey(this.key);
            if (i != -1) {
                this.parent.keys[i] = key;
            }
        }
        this.key = key;
    }
    
    @Override
    public String getValue() {
        return this.val;
    }
    
    @Override
    public String setValue(final String val) {
        final String oldVal = this.parent.get(this.key);
        if (this.parent != null) {
            final int i = this.parent.indexOfKey(this.key);
            if (i != -1) {
                this.parent.vals[i] = val;
            }
        }
        this.val = val;
        return oldVal;
    }
    
    public String html() {
        final StringBuilder accum = new StringBuilder();
        try {
            this.html(accum, new Document("").outputSettings());
        }
        catch (IOException exception) {
            throw new SerializationException(exception);
        }
        return accum.toString();
    }
    
    protected static void html(final String key, final String val, final Appendable accum, final Document.OutputSettings out) throws IOException {
        accum.append(key);
        if (!shouldCollapseAttribute(key, val, out)) {
            accum.append("=\"");
            Entities.escape(accum, Attributes.checkNotNull(val), out, true, false, false);
            accum.append('\"');
        }
    }
    
    protected void html(final Appendable accum, final Document.OutputSettings out) throws IOException {
        html(this.key, this.val, accum, out);
    }
    
    @Override
    public String toString() {
        return this.html();
    }
    
    public static Attribute createFromEncoded(final String unencodedKey, final String encodedValue) {
        final String value = Entities.unescape(encodedValue, true);
        return new Attribute(unencodedKey, value, null);
    }
    
    protected boolean isDataAttribute() {
        return isDataAttribute(this.key);
    }
    
    protected static boolean isDataAttribute(final String key) {
        return key.startsWith("data-") && key.length() > "data-".length();
    }
    
    protected final boolean shouldCollapseAttribute(final Document.OutputSettings out) {
        return shouldCollapseAttribute(this.key, this.val, out);
    }
    
    protected static boolean shouldCollapseAttribute(final String key, final String val, final Document.OutputSettings out) {
        return out.syntax() == Document.OutputSettings.Syntax.html && (val == null || (("".equals(val) || val.equalsIgnoreCase(key)) && isBooleanAttribute(key)));
    }
    
    @Deprecated
    protected boolean isBooleanAttribute() {
        return Arrays.binarySearch(Attribute.booleanAttributes, this.key) >= 0 || this.val == null;
    }
    
    protected static boolean isBooleanAttribute(final String key) {
        return Arrays.binarySearch(Attribute.booleanAttributes, key) >= 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Attribute attribute = (Attribute)o;
        if (this.key != null) {
            if (this.key.equals(attribute.key)) {
                return (this.val != null) ? this.val.equals(attribute.val) : (attribute.val == null);
            }
        }
        else if (attribute.key == null) {
            return (this.val != null) ? this.val.equals(attribute.val) : (attribute.val == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.key != null) ? this.key.hashCode() : 0;
        result = 31 * result + ((this.val != null) ? this.val.hashCode() : 0);
        return result;
    }
    
    public Attribute clone() {
        try {
            return (Attribute)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        booleanAttributes = new String[] { "allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed", "seamless", "selected", "sortable", "truespeed", "typemustmatch" };
    }
}
