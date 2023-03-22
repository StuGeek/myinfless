// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import org.apache.commons.logging.LogFactory;
import net.sf.json.util.JSONUtils;
import java.util.Set;
import java.util.Iterator;
import net.sf.json.util.JsonEventListener;
import org.apache.commons.logging.Log;

abstract class AbstractJSON
{
    private static CycleSet cycleSet;
    private static final Log log;
    
    protected static boolean addInstance(final Object instance) {
        return getCycleSet().add(instance);
    }
    
    protected static void fireArrayEndEvent(final JsonConfig jsonConfig) {
        if (jsonConfig.isEventTriggeringEnabled()) {
            for (final JsonEventListener listener : jsonConfig.getJsonEventListeners()) {
                try {
                    listener.onArrayEnd();
                }
                catch (RuntimeException e) {
                    AbstractJSON.log.warn(e);
                }
            }
        }
    }
    
    protected static void fireArrayStartEvent(final JsonConfig jsonConfig) {
        if (jsonConfig.isEventTriggeringEnabled()) {
            for (final JsonEventListener listener : jsonConfig.getJsonEventListeners()) {
                try {
                    listener.onArrayStart();
                }
                catch (RuntimeException e) {
                    AbstractJSON.log.warn(e);
                }
            }
        }
    }
    
    protected static void fireElementAddedEvent(final int index, final Object element, final JsonConfig jsonConfig) {
        if (jsonConfig.isEventTriggeringEnabled()) {
            for (final JsonEventListener listener : jsonConfig.getJsonEventListeners()) {
                try {
                    listener.onElementAdded(index, element);
                }
                catch (RuntimeException e) {
                    AbstractJSON.log.warn(e);
                }
            }
        }
    }
    
    protected static void fireErrorEvent(final JSONException jsone, final JsonConfig jsonConfig) {
        if (jsonConfig.isEventTriggeringEnabled()) {
            for (final JsonEventListener listener : jsonConfig.getJsonEventListeners()) {
                try {
                    listener.onError(jsone);
                }
                catch (RuntimeException e) {
                    AbstractJSON.log.warn(e);
                }
            }
        }
    }
    
    protected static void fireObjectEndEvent(final JsonConfig jsonConfig) {
        if (jsonConfig.isEventTriggeringEnabled()) {
            for (final JsonEventListener listener : jsonConfig.getJsonEventListeners()) {
                try {
                    listener.onObjectEnd();
                }
                catch (RuntimeException e) {
                    AbstractJSON.log.warn(e);
                }
            }
        }
    }
    
    protected static void fireObjectStartEvent(final JsonConfig jsonConfig) {
        if (jsonConfig.isEventTriggeringEnabled()) {
            for (final JsonEventListener listener : jsonConfig.getJsonEventListeners()) {
                try {
                    listener.onObjectStart();
                }
                catch (RuntimeException e) {
                    AbstractJSON.log.warn(e);
                }
            }
        }
    }
    
    protected static void firePropertySetEvent(final String key, final Object value, final boolean accumulated, final JsonConfig jsonConfig) {
        if (jsonConfig.isEventTriggeringEnabled()) {
            for (final JsonEventListener listener : jsonConfig.getJsonEventListeners()) {
                try {
                    listener.onPropertySet(key, value, accumulated);
                }
                catch (RuntimeException e) {
                    AbstractJSON.log.warn(e);
                }
            }
        }
    }
    
    protected static void fireWarnEvent(final String warning, final JsonConfig jsonConfig) {
        if (jsonConfig.isEventTriggeringEnabled()) {
            for (final JsonEventListener listener : jsonConfig.getJsonEventListeners()) {
                try {
                    listener.onWarning(warning);
                }
                catch (RuntimeException e) {
                    AbstractJSON.log.warn(e);
                }
            }
        }
    }
    
    protected static void removeInstance(final Object instance) {
        final Set set = getCycleSet();
        set.remove(instance);
        if (set.size() == 0) {
            AbstractJSON.cycleSet.remove();
        }
    }
    
    protected Object _processValue(Object value, final JsonConfig jsonConfig) {
        if (JSONNull.getInstance().equals(value)) {
            return JSONNull.getInstance();
        }
        if (Class.class.isAssignableFrom(value.getClass()) || value instanceof Class) {
            return ((Class)value).getName();
        }
        if (JSONUtils.isFunction(value)) {
            if (value instanceof String) {
                value = JSONFunction.parse((String)value);
            }
            return value;
        }
        if (value instanceof JSONString) {
            return JSONSerializer.toJSON(value, jsonConfig);
        }
        if (value instanceof JSON) {
            return JSONSerializer.toJSON(value, jsonConfig);
        }
        if (JSONUtils.isArray(value)) {
            return JSONArray.fromObject(value, jsonConfig);
        }
        if (JSONUtils.isString(value)) {
            final String str = String.valueOf(value);
            if (JSONUtils.hasQuotes(str)) {
                final String stripped = JSONUtils.stripQuotes(str);
                if (JSONUtils.isFunction(stripped)) {
                    return "\"" + stripped + "\"";
                }
                if (stripped.startsWith("[") && stripped.endsWith("]")) {
                    return stripped;
                }
                if (stripped.startsWith("{") && stripped.endsWith("}")) {
                    return stripped;
                }
                return str;
            }
            else {
                if (!JSONUtils.isJsonKeyword(str, jsonConfig)) {
                    if (JSONUtils.mayBeJSON(str)) {
                        try {
                            return JSONSerializer.toJSON((Object)str, jsonConfig);
                        }
                        catch (JSONException jsone) {
                            return str;
                        }
                    }
                    return str;
                }
                if (jsonConfig.isJavascriptCompliant() && "undefined".equals(str)) {
                    return JSONNull.getInstance();
                }
                return str;
            }
        }
        else {
            if (JSONUtils.isNumber(value)) {
                JSONUtils.testValidity(value);
                return JSONUtils.transformNumber((Number)value);
            }
            if (JSONUtils.isBoolean(value)) {
                return value;
            }
            final JSONObject jsonObject = JSONObject.fromObject(value, jsonConfig);
            if (jsonObject.isNullObject()) {
                return JSONNull.getInstance();
            }
            return jsonObject;
        }
    }
    
    private static Set getCycleSet() {
        return AbstractJSON.cycleSet.getSet();
    }
    
    static {
        AbstractJSON.cycleSet = new CycleSet();
        log = LogFactory.getLog(AbstractJSON.class);
    }
    
    private static class CycleSet extends ThreadLocal
    {
        protected Object initialValue() {
            return new SoftReference(new HashSet());
        }
        
        public Set getSet() {
            Set set = this.get().get();
            if (set == null) {
                set = new HashSet();
                this.set(new SoftReference<Set>(set));
            }
            return set;
        }
    }
}
