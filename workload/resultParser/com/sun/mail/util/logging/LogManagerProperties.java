// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.mail.util.logging;

import java.io.ObjectStreamException;
import java.util.Map;
import java.util.Enumeration;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.LogManager;
import java.util.Properties;

final class LogManagerProperties extends Properties
{
    private static final long serialVersionUID = -2239983349056806252L;
    static final LogManager manager;
    private final String prefix;
    
    static final Class findClass(final String name) throws ClassNotFoundException {
        final ClassLoader[] loaders = getClassLoaders();
        Class clazz;
        if (loaders[0] != null) {
            try {
                clazz = Class.forName(name, false, loaders[0]);
            }
            catch (ClassNotFoundException tryContext) {
                clazz = tryLoad(name, loaders[1]);
            }
        }
        else {
            clazz = tryLoad(name, loaders[1]);
        }
        return clazz;
    }
    
    private static Class tryLoad(final String name, final ClassLoader l) throws ClassNotFoundException {
        if (l != null) {
            return Class.forName(name, false, l);
        }
        return Class.forName(name);
    }
    
    private static ClassLoader[] getClassLoaders() {
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader[]>)new PrivilegedAction() {
            public Object run() {
                final ClassLoader[] loaders = new ClassLoader[2];
                try {
                    loaders[0] = ClassLoader.getSystemClassLoader();
                }
                catch (SecurityException ignore) {
                    loaders[0] = null;
                }
                try {
                    loaders[1] = Thread.currentThread().getContextClassLoader();
                }
                catch (SecurityException ignore) {
                    loaders[1] = null;
                }
                return loaders;
            }
        });
    }
    
    LogManagerProperties(final Properties parent, final String prefix) {
        super(parent);
        if (prefix == null) {
            throw new NullPointerException();
        }
        this.prefix = prefix;
    }
    
    public String getProperty(final String key) {
        String value = super.getProperty(key);
        if (value == null && key.length() > 0) {
            value = LogManagerProperties.manager.getProperty(this.prefix + '.' + key);
            if (value == null) {
                value = LogManagerProperties.manager.getProperty(key);
            }
        }
        return value;
    }
    
    public Enumeration propertyNames() {
        assert false;
        return super.propertyNames();
    }
    
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Properties)) {
            return false;
        }
        assert false;
        return super.equals(o);
    }
    
    public int hashCode() {
        assert false;
        return super.hashCode();
    }
    
    private synchronized Object writeReplace() throws ObjectStreamException {
        assert false;
        final Properties out = new Properties(this.defaults);
        if (!super.isEmpty()) {
            out.putAll(this);
        }
        return out;
    }
    
    static {
        manager = LogManager.getLogManager();
    }
}
