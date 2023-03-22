// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class DynaBeanMapDecorator implements Map
{
    private DynaBean dynaBean;
    private boolean readOnly;
    private transient Set keySet;
    
    public DynaBeanMapDecorator(final DynaBean dynaBean) {
        this(dynaBean, true);
    }
    
    public DynaBeanMapDecorator(final DynaBean dynaBean, final boolean readOnly) {
        if (dynaBean == null) {
            throw new IllegalArgumentException("DynaBean is null");
        }
        this.dynaBean = dynaBean;
        this.readOnly = readOnly;
    }
    
    public boolean isReadOnly() {
        return this.readOnly;
    }
    
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    public boolean containsKey(final Object key) {
        final DynaClass dynaClass = this.getDynaBean().getDynaClass();
        final DynaProperty dynaProperty = dynaClass.getDynaProperty(this.toString(key));
        return dynaProperty != null;
    }
    
    public boolean containsValue(final Object value) {
        final DynaProperty[] properties = this.getDynaProperties();
        for (int i = 0; i < properties.length; ++i) {
            final String key = properties[i].getName();
            final Object prop = this.getDynaBean().get(key);
            if (value == null) {
                if (prop == null) {
                    return true;
                }
            }
            else if (value.equals(prop)) {
                return true;
            }
        }
        return false;
    }
    
    public Set entrySet() {
        final DynaProperty[] properties = this.getDynaProperties();
        final Set set = new HashSet(properties.length);
        for (int i = 0; i < properties.length; ++i) {
            final String key = properties[i].getName();
            final Object value = this.getDynaBean().get(key);
            set.add(new MapEntry(key, value));
        }
        return Collections.unmodifiableSet((Set<?>)set);
    }
    
    public Object get(final Object key) {
        return this.getDynaBean().get(this.toString(key));
    }
    
    public boolean isEmpty() {
        return this.getDynaProperties().length == 0;
    }
    
    public Set keySet() {
        if (this.keySet != null) {
            return this.keySet;
        }
        final DynaProperty[] properties = this.getDynaProperties();
        Set set = new HashSet(properties.length);
        for (int i = 0; i < properties.length; ++i) {
            set.add(properties[i].getName());
        }
        set = Collections.unmodifiableSet((Set<?>)set);
        final DynaClass dynaClass = this.getDynaBean().getDynaClass();
        if (!(dynaClass instanceof MutableDynaClass)) {
            this.keySet = set;
        }
        return set;
    }
    
    public Object put(final Object key, final Object value) {
        if (this.isReadOnly()) {
            throw new UnsupportedOperationException("Map is read only");
        }
        final String property = this.toString(key);
        final Object previous = this.getDynaBean().get(property);
        this.getDynaBean().set(property, value);
        return previous;
    }
    
    public void putAll(final Map map) {
        if (this.isReadOnly()) {
            throw new UnsupportedOperationException("Map is read only");
        }
        for (final Object key : map.keySet()) {
            this.put(key, map.get(key));
        }
    }
    
    public Object remove(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    public int size() {
        return this.getDynaProperties().length;
    }
    
    public Collection values() {
        final DynaProperty[] properties = this.getDynaProperties();
        final List values = new ArrayList(properties.length);
        for (int i = 0; i < properties.length; ++i) {
            final String key = properties[i].getName();
            final Object value = this.getDynaBean().get(key);
            values.add(value);
        }
        return Collections.unmodifiableList((List<?>)values);
    }
    
    public DynaBean getDynaBean() {
        return this.dynaBean;
    }
    
    private DynaProperty[] getDynaProperties() {
        return this.getDynaBean().getDynaClass().getDynaProperties();
    }
    
    private String toString(final Object obj) {
        return (obj == null) ? null : obj.toString();
    }
    
    private static class MapEntry implements Entry
    {
        private Object key;
        private Object value;
        
        MapEntry(final Object key, final Object value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry e = (Entry)o;
            return this.key.equals(e.getKey()) && ((this.value != null) ? this.value.equals(e.getValue()) : (e.getValue() == null));
        }
        
        public int hashCode() {
            return this.key.hashCode() + ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        public Object getKey() {
            return this.key;
        }
        
        public Object getValue() {
            return this.value;
        }
        
        public Object setValue(final Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
