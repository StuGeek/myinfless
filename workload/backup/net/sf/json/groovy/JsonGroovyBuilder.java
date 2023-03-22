// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.groovy;

import java.util.Iterator;
import net.sf.json.JSONException;
import net.sf.json.JSONSerializer;
import groovy.lang.GString;
import java.util.List;
import groovy.lang.Closure;
import net.sf.json.JSONArray;
import groovy.lang.MissingMethodException;
import net.sf.json.JSONObject;
import java.util.HashMap;
import java.util.Stack;
import java.util.Map;
import net.sf.json.JSON;
import groovy.lang.GroovyObjectSupport;

public class JsonGroovyBuilder extends GroovyObjectSupport
{
    private static final String JSON = "json";
    private JSON current;
    private Map properties;
    private Stack stack;
    
    public JsonGroovyBuilder() {
        this.stack = new Stack();
        this.properties = new HashMap();
    }
    
    public Object getProperty(final String name) {
        if (this.stack.isEmpty()) {
            return this._getProperty(name);
        }
        final Object top = this.stack.peek();
        if (!(top instanceof JSONObject)) {
            return this._getProperty(name);
        }
        final JSONObject json = (JSONObject)top;
        if (json.containsKey(name)) {
            return json.get(name);
        }
        return this._getProperty(name);
    }
    
    public Object invokeMethod(final String name, final Object arg) {
        if ("json".equals(name) && this.stack.isEmpty()) {
            return this.createObject(name, arg);
        }
        final Object[] args = (Object[])arg;
        if (args.length == 0) {
            throw new MissingMethodException(name, (Class)this.getClass(), args);
        }
        Object value = null;
        if (args.length > 1) {
            final JSONArray array = new JSONArray();
            this.stack.push(array);
            for (int i = 0; i < args.length; ++i) {
                if (args[i] instanceof Closure) {
                    this.append(name, this.createObject((Closure)args[i]));
                }
                else if (args[i] instanceof Map) {
                    this.append(name, this.createObject((Map)args[i]));
                }
                else if (args[i] instanceof List) {
                    this.append(name, this.createArray((List)args[i]));
                }
                else {
                    this._append(name, args[i], this.stack.peek());
                }
            }
            this.stack.pop();
        }
        else if (args[0] instanceof Closure) {
            value = this.createObject((Closure)args[0]);
        }
        else if (args[0] instanceof Map) {
            value = this.createObject((Map)args[0]);
        }
        else if (args[0] instanceof List) {
            value = this.createArray((List)args[0]);
        }
        if (this.stack.isEmpty()) {
            final JSONObject object = new JSONObject();
            object.accumulate(name, this.current);
            this.current = object;
        }
        else {
            final JSON top = this.stack.peek();
            if (top instanceof JSONObject) {
                this.append(name, (this.current == null) ? value : this.current);
            }
        }
        return this.current;
    }
    
    public void setProperty(final String name, Object value) {
        if (value instanceof GString) {
            value = value.toString();
            try {
                value = JSONSerializer.toJSON(value);
            }
            catch (JSONException jsone) {}
        }
        else if (value instanceof Closure) {
            value = this.createObject((Closure)value);
        }
        else if (value instanceof Map) {
            value = this.createObject((Map)value);
        }
        else if (value instanceof List) {
            value = this.createArray((List)value);
        }
        this.append(name, value);
    }
    
    private Object _getProperty(final String name) {
        if (this.properties.containsKey(name)) {
            return this.properties.get(name);
        }
        return super.getProperty(name);
    }
    
    private void append(final String key, final Object value) {
        Object target = null;
        if (!this.stack.isEmpty()) {
            target = this.stack.peek();
            this._append(key, value, this.current = (JSON)target);
        }
        else {
            this.properties.put(key, value);
        }
    }
    
    private void _append(final String key, final Object value, final JSON target) {
        if (target instanceof JSONObject) {
            ((JSONObject)target).accumulate(key, value);
        }
        else if (target instanceof JSONArray) {
            ((JSONArray)target).element(value);
        }
    }
    
    private JSON createArray(final List list) {
        final JSONArray array = new JSONArray();
        this.stack.push(array);
        for (Object element : list) {
            if (element instanceof Closure) {
                element = this.createObject((Closure)element);
            }
            else if (element instanceof Map) {
                element = this.createObject((Map)element);
            }
            else if (element instanceof List) {
                element = this.createArray((List)element);
            }
            array.element(element);
        }
        this.stack.pop();
        return array;
    }
    
    private JSON createObject(final Closure closure) {
        final JSONObject object = new JSONObject();
        this.stack.push(object);
        closure.setDelegate((Object)this);
        closure.setResolveStrategy(1);
        closure.call();
        this.stack.pop();
        return object;
    }
    
    private JSON createObject(final Map map) {
        final JSONObject object = new JSONObject();
        this.stack.push(object);
        for (final Map.Entry property : map.entrySet()) {
            final String key = String.valueOf(property.getKey());
            Object value = property.getValue();
            if (value instanceof Closure) {
                value = this.createObject((Closure)value);
            }
            else if (value instanceof Map) {
                value = this.createObject((Map)value);
            }
            else if (value instanceof List) {
                value = this.createArray((List)value);
            }
            object.element(key, value);
        }
        this.stack.pop();
        return object;
    }
    
    private JSON createObject(final String name, final Object arg) {
        final Object[] args = (Object[])arg;
        if (args.length == 0) {
            throw new MissingMethodException(name, (Class)this.getClass(), args);
        }
        if (args.length != 1) {
            final JSONArray array = new JSONArray();
            this.stack.push(array);
            for (int i = 0; i < args.length; ++i) {
                if (args[i] instanceof Closure) {
                    this.append(name, this.createObject((Closure)args[i]));
                }
                else if (args[i] instanceof Map) {
                    this.append(name, this.createObject((Map)args[i]));
                }
                else if (args[i] instanceof List) {
                    this.append(name, this.createArray((List)args[i]));
                }
                else {
                    this._append(name, args[i], this.stack.peek());
                }
            }
            this.stack.pop();
            return array;
        }
        if (args[0] instanceof Closure) {
            return this.createObject((Closure)args[0]);
        }
        if (args[0] instanceof Map) {
            return this.createObject((Map)args[0]);
        }
        if (args[0] instanceof List) {
            return this.createArray((List)args[0]);
        }
        throw new JSONException("Unsupported type");
    }
}
