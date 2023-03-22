// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.util.Collection;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.beans.PropertyDescriptor;
import java.lang.ref.Reference;

public class WrapDynaClass implements DynaClass
{
    private String beanClassName;
    private Reference beanClassRef;
    protected Class beanClass;
    protected PropertyDescriptor[] descriptors;
    protected HashMap descriptorsMap;
    protected DynaProperty[] properties;
    protected HashMap propertiesMap;
    private static final ContextClassLoaderLocal CLASSLOADER_CACHE;
    protected static HashMap dynaClasses;
    
    private WrapDynaClass(final Class beanClass) {
        this.beanClassName = null;
        this.beanClassRef = null;
        this.beanClass = null;
        this.descriptors = null;
        this.descriptorsMap = new HashMap();
        this.properties = null;
        this.propertiesMap = new HashMap();
        this.beanClassRef = new SoftReference(beanClass);
        this.beanClassName = beanClass.getName();
        this.introspect();
    }
    
    private static Map getDynaClassesMap() {
        return (Map)WrapDynaClass.CLASSLOADER_CACHE.get();
    }
    
    protected Class getBeanClass() {
        return this.beanClassRef.get();
    }
    
    public String getName() {
        return this.beanClassName;
    }
    
    public DynaProperty getDynaProperty(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("No property name specified");
        }
        return this.propertiesMap.get(name);
    }
    
    public DynaProperty[] getDynaProperties() {
        return this.properties;
    }
    
    public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
        return new WrapDynaBean(this.getBeanClass().newInstance());
    }
    
    public PropertyDescriptor getPropertyDescriptor(final String name) {
        return this.descriptorsMap.get(name);
    }
    
    public static void clear() {
        getDynaClassesMap().clear();
    }
    
    public static WrapDynaClass createDynaClass(final Class beanClass) {
        WrapDynaClass dynaClass = getDynaClassesMap().get(beanClass);
        if (dynaClass == null) {
            dynaClass = new WrapDynaClass(beanClass);
            getDynaClassesMap().put(beanClass, dynaClass);
        }
        return dynaClass;
    }
    
    protected void introspect() {
        final Class beanClass = this.getBeanClass();
        PropertyDescriptor[] regulars = PropertyUtils.getPropertyDescriptors(beanClass);
        if (regulars == null) {
            regulars = new PropertyDescriptor[0];
        }
        Map mappeds = PropertyUtils.getMappedPropertyDescriptors(beanClass);
        if (mappeds == null) {
            mappeds = new HashMap();
        }
        this.properties = new DynaProperty[regulars.length + mappeds.size()];
        for (int i = 0; i < regulars.length; ++i) {
            this.descriptorsMap.put(regulars[i].getName(), regulars[i]);
            this.properties[i] = new DynaProperty(regulars[i].getName(), regulars[i].getPropertyType());
            this.propertiesMap.put(this.properties[i].getName(), this.properties[i]);
        }
        int j = regulars.length;
        for (final String name : mappeds.keySet()) {
            final PropertyDescriptor descriptor = mappeds.get(name);
            this.properties[j] = new DynaProperty(descriptor.getName(), Map.class);
            this.propertiesMap.put(this.properties[j].getName(), this.properties[j]);
            ++j;
        }
    }
    
    static {
        CLASSLOADER_CACHE = new ContextClassLoaderLocal() {
            protected Object initialValue() {
                return new WeakHashMap();
            }
        };
        WrapDynaClass.dynaClasses = new HashMap() {
            public void clear() {
                getDynaClassesMap().clear();
            }
            
            public boolean containsKey(final Object key) {
                return getDynaClassesMap().containsKey(key);
            }
            
            public boolean containsValue(final Object value) {
                return getDynaClassesMap().containsValue(value);
            }
            
            public Set entrySet() {
                return getDynaClassesMap().entrySet();
            }
            
            public boolean equals(final Object o) {
                return getDynaClassesMap().equals(o);
            }
            
            public Object get(final Object key) {
                return getDynaClassesMap().get(key);
            }
            
            public int hashCode() {
                return getDynaClassesMap().hashCode();
            }
            
            public boolean isEmpty() {
                return getDynaClassesMap().isEmpty();
            }
            
            public Set keySet() {
                return getDynaClassesMap().keySet();
            }
            
            public Object put(final Object key, final Object value) {
                return getDynaClassesMap().put(key, value);
            }
            
            public void putAll(final Map m) {
                getDynaClassesMap().putAll(m);
            }
            
            public Object remove(final Object key) {
                return getDynaClassesMap().remove(key);
            }
            
            public int size() {
                return getDynaClassesMap().size();
            }
            
            public Collection values() {
                return getDynaClassesMap().values();
            }
        };
    }
}
