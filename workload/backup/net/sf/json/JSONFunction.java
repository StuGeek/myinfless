// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.StringUtils;
import net.sf.json.util.JSONUtils;
import java.io.Serializable;

public class JSONFunction implements Serializable
{
    private static final String[] EMPTY_PARAM_ARRAY;
    private String[] params;
    private String text;
    
    public static JSONFunction parse(final String str) {
        if (!JSONUtils.isFunction(str)) {
            throw new JSONException("String is not a function. " + str);
        }
        final String params = JSONUtils.getFunctionParams(str);
        final String text = JSONUtils.getFunctionBody(str);
        return new JSONFunction((String[])((params != null) ? StringUtils.split(params, ",") : null), (text != null) ? text : "");
    }
    
    public JSONFunction(final String text) {
        this(null, text);
    }
    
    public JSONFunction(final String[] params, final String text) {
        this.text = ((text != null) ? text.trim() : "");
        if (params != null) {
            if (params.length == 1 && params[0].trim().equals("")) {
                this.params = JSONFunction.EMPTY_PARAM_ARRAY;
            }
            else {
                System.arraycopy(params, 0, this.params = new String[params.length], 0, params.length);
                for (int i = 0; i < params.length; ++i) {
                    this.params[i] = this.params[i].trim();
                }
            }
        }
        else {
            this.params = JSONFunction.EMPTY_PARAM_ARRAY;
        }
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            try {
                final JSONFunction other = parse((String)obj);
                return this.equals(other);
            }
            catch (JSONException e) {
                return false;
            }
        }
        if (!(obj instanceof JSONFunction)) {
            return false;
        }
        final JSONFunction other = (JSONFunction)obj;
        if (this.params.length != other.params.length) {
            return false;
        }
        final EqualsBuilder builder = new EqualsBuilder();
        for (int i = 0; i < this.params.length; ++i) {
            builder.append(this.params[i], other.params[i]);
        }
        builder.append(this.text, other.text);
        return builder.isEquals();
    }
    
    public String[] getParams() {
        return this.params;
    }
    
    public String getText() {
        return this.text;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        for (int i = 0; i < this.params.length; ++i) {
            builder.append(this.params[i]);
        }
        builder.append(this.text);
        return builder.toHashCode();
    }
    
    public String toString() {
        final StringBuffer b = new StringBuffer("function(");
        if (this.params.length > 0) {
            for (int i = 0; i < this.params.length - 1; ++i) {
                b.append(this.params[i]).append(',');
            }
            b.append(this.params[this.params.length - 1]);
        }
        b.append("){");
        if (this.text.length() > 0) {
            b.append(' ').append(this.text).append(' ');
        }
        b.append('}');
        return b.toString();
    }
    
    static {
        EMPTY_PARAM_ARRAY = new String[0];
    }
}
