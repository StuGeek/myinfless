// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import java.util.Iterator;
import net.sf.json.JSONNull;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSON;

public class WebUtils
{
    private static final WebHijackPreventionStrategy DEFAULT_WEB_HIJACK_PREVENTION_STRATEGY;
    private static WebHijackPreventionStrategy webHijackPreventionStrategy;
    
    public static WebHijackPreventionStrategy getWebHijackPreventionStrategy() {
        return WebUtils.webHijackPreventionStrategy;
    }
    
    public static String protect(final JSON json) {
        return protect(json, false);
    }
    
    public static String protect(final JSON json, final boolean shrink) {
        final String output = shrink ? toString(json) : json.toString(0);
        return WebUtils.webHijackPreventionStrategy.protect(output);
    }
    
    public static void setWebHijackPreventionStrategy(final WebHijackPreventionStrategy strategy) {
        WebUtils.webHijackPreventionStrategy = ((strategy == null) ? WebUtils.DEFAULT_WEB_HIJACK_PREVENTION_STRATEGY : strategy);
    }
    
    public static String toString(final JSON json) {
        if (json instanceof JSONObject) {
            return toString((JSONObject)json);
        }
        if (json instanceof JSONArray) {
            return toString((JSONArray)json);
        }
        return toString((JSONNull)json);
    }
    
    private static String join(final JSONArray jsonArray) {
        final int len = jsonArray.size();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; ++i) {
            if (i > 0) {
                sb.append(",");
            }
            final Object value = jsonArray.get(i);
            sb.append(toString(value));
        }
        return sb.toString();
    }
    
    private static String quote(final String str) {
        if (str.indexOf(" ") > -1 || str.indexOf(":") > -1) {
            return JSONUtils.quote(str);
        }
        return str;
    }
    
    private static String toString(final JSONArray jsonArray) {
        try {
            return '[' + join(jsonArray) + ']';
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private static String toString(final JSONNull jsonNull) {
        return jsonNull.toString();
    }
    
    private static String toString(final JSONObject jsonObject) {
        if (jsonObject.isNullObject()) {
            return JSONNull.getInstance().toString();
        }
        final Iterator keys = jsonObject.keys();
        final StringBuffer sb = new StringBuffer("{");
        while (keys.hasNext()) {
            if (sb.length() > 1) {
                sb.append(',');
            }
            final Object o = keys.next();
            sb.append(quote(o.toString()));
            sb.append(':');
            sb.append(toString(jsonObject.get(String.valueOf(o))));
        }
        sb.append('}');
        return sb.toString();
    }
    
    private static String toString(final Object object) {
        if (object instanceof JSON) {
            return toString((JSON)object);
        }
        return JSONUtils.valueToString(object);
    }
    
    private WebUtils() {
    }
    
    static {
        DEFAULT_WEB_HIJACK_PREVENTION_STRATEGY = WebHijackPreventionStrategy.INFINITE_LOOP;
        WebUtils.webHijackPreventionStrategy = WebUtils.DEFAULT_WEB_HIJACK_PREVENTION_STRATEGY;
    }
}
