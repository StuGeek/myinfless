// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import java.io.IOException;
import java.io.Writer;

public final class JSONNull implements JSON
{
    private static JSONNull instance;
    
    public static JSONNull getInstance() {
        return JSONNull.instance;
    }
    
    private JSONNull() {
    }
    
    public boolean equals(final Object object) {
        return object == null || object == this || object == JSONNull.instance || (object instanceof JSONObject && ((JSONObject)object).isNullObject()) || "null".equals(object);
    }
    
    public int hashCode() {
        return 37 + "null".hashCode();
    }
    
    public boolean isArray() {
        return false;
    }
    
    public boolean isEmpty() {
        throw new JSONException("Object is null");
    }
    
    public int size() {
        throw new JSONException("Object is null");
    }
    
    public String toString() {
        return "null";
    }
    
    public String toString(final int indentFactor) {
        return this.toString();
    }
    
    public String toString(final int indentFactor, final int indent) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < indent; ++i) {
            sb.append(' ');
        }
        sb.append(this.toString());
        return sb.toString();
    }
    
    public Writer write(final Writer writer) {
        try {
            writer.write(this.toString());
            return writer;
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
    }
    
    static {
        JSONNull.instance = new JSONNull();
    }
}
