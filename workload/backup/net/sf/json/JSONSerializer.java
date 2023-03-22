// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import net.sf.json.util.JSONTokener;
import net.sf.json.util.JSONUtils;

public class JSONSerializer
{
    public static Object toJava(final JSON json) {
        return toJava(json, new JsonConfig());
    }
    
    public static Object toJava(final JSON json, final JsonConfig jsonConfig) {
        if (JSONUtils.isNull(json)) {
            return null;
        }
        Object object = null;
        if (json instanceof JSONArray) {
            if (jsonConfig.getArrayMode() == 2) {
                object = JSONArray.toArray((JSONArray)json, jsonConfig);
            }
            else {
                object = JSONArray.toCollection((JSONArray)json, jsonConfig);
            }
        }
        else {
            object = JSONObject.toBean((JSONObject)json, jsonConfig);
        }
        return object;
    }
    
    public static JSON toJSON(final Object object) {
        return toJSON(object, new JsonConfig());
    }
    
    public static JSON toJSON(final Object object, final JsonConfig jsonConfig) {
        JSON json = null;
        if (object == null) {
            json = JSONNull.getInstance();
        }
        else if (object instanceof JSONString) {
            json = toJSON((JSONString)object, jsonConfig);
        }
        else if (object instanceof String) {
            json = toJSON((String)object, jsonConfig);
        }
        else if (JSONUtils.isArray(object)) {
            json = JSONArray.fromObject(object, jsonConfig);
        }
        else {
            try {
                json = JSONObject.fromObject(object, jsonConfig);
            }
            catch (JSONException e) {
                if (object instanceof JSONTokener) {
                    ((JSONTokener)object).reset();
                }
                json = JSONArray.fromObject(object, jsonConfig);
            }
        }
        return json;
    }
    
    private static JSON toJSON(final JSONString string, final JsonConfig jsonConfig) {
        return toJSON(string.toJSONString(), jsonConfig);
    }
    
    private static JSON toJSON(final String string, final JsonConfig jsonConfig) {
        JSON json = null;
        if (string.startsWith("[")) {
            json = JSONArray.fromObject(string, jsonConfig);
        }
        else if (string.startsWith("{")) {
            json = JSONObject.fromObject(string, jsonConfig);
        }
        else {
            if (!"null".equals(string)) {
                throw new JSONException("Invalid JSON String");
            }
            json = JSONNull.getInstance();
        }
        return json;
    }
}
