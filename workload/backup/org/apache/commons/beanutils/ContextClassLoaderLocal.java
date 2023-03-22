// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.util.WeakHashMap;
import java.util.Map;

public class ContextClassLoaderLocal
{
    private Map valueByClassLoader;
    private boolean globalValueInitialized;
    private Object globalValue;
    
    public ContextClassLoaderLocal() {
        this.valueByClassLoader = new WeakHashMap();
        this.globalValueInitialized = false;
    }
    
    protected Object initialValue() {
        return null;
    }
    
    public synchronized Object get() {
        this.valueByClassLoader.isEmpty();
        try {
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                Object value = this.valueByClassLoader.get(contextClassLoader);
                if (value == null && !this.valueByClassLoader.containsKey(contextClassLoader)) {
                    value = this.initialValue();
                    this.valueByClassLoader.put(contextClassLoader, value);
                }
                return value;
            }
        }
        catch (SecurityException ex) {}
        if (!this.globalValueInitialized) {
            this.globalValue = this.initialValue();
            this.globalValueInitialized = true;
        }
        return this.globalValue;
    }
    
    public synchronized void set(final Object value) {
        this.valueByClassLoader.isEmpty();
        try {
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                this.valueByClassLoader.put(contextClassLoader, value);
                return;
            }
        }
        catch (SecurityException ex) {}
        this.globalValue = value;
        this.globalValueInitialized = true;
    }
    
    public synchronized void unset() {
        try {
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            this.unset(contextClassLoader);
        }
        catch (SecurityException ex) {}
    }
    
    public synchronized void unset(final ClassLoader classLoader) {
        this.valueByClassLoader.remove(classLoader);
    }
}
