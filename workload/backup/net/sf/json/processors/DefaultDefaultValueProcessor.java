// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.processors;

import net.sf.json.JSONNull;
import net.sf.json.JSONArray;
import net.sf.json.util.JSONUtils;

public class DefaultDefaultValueProcessor implements DefaultValueProcessor
{
    public Object getDefaultValue(final Class type) {
        if (JSONUtils.isArray(type)) {
            return new JSONArray();
        }
        if (JSONUtils.isNumber(type)) {
            if (JSONUtils.isDouble(type)) {
                return new Double(0.0);
            }
            return new Integer(0);
        }
        else {
            if (JSONUtils.isBoolean(type)) {
                return Boolean.FALSE;
            }
            if (JSONUtils.isString(type)) {
                return "";
            }
            return JSONNull.getInstance();
        }
    }
}
