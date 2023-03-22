// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

public class WrapDynaBean implements DynaBean, Serializable
{
    protected transient WrapDynaClass dynaClass;
    protected Object instance;
    
    public WrapDynaBean(final Object instance) {
        this.dynaClass = null;
        this.instance = null;
        this.instance = instance;
        this.dynaClass = (WrapDynaClass)this.getDynaClass();
    }
    
    public boolean contains(final String name, final String key) {
        throw new UnsupportedOperationException("WrapDynaBean does not support contains()");
    }
    
    public Object get(final String name) {
        Object value = null;
        try {
            value = PropertyUtils.getSimpleProperty(this.instance, name);
        }
        catch (InvocationTargetException ite) {
            final Throwable cause = ite.getTargetException();
            throw new IllegalArgumentException("Error reading property '" + name + "' nested exception - " + cause);
        }
        catch (Throwable t) {
            throw new IllegalArgumentException("Error reading property '" + name + "', exception - " + t);
        }
        return value;
    }
    
    public Object get(final String name, final int index) {
        Object value = null;
        try {
            value = PropertyUtils.getIndexedProperty(this.instance, name, index);
        }
        catch (IndexOutOfBoundsException e) {
            throw e;
        }
        catch (InvocationTargetException ite) {
            final Throwable cause = ite.getTargetException();
            throw new IllegalArgumentException("Error reading indexed property '" + name + "' nested exception - " + cause);
        }
        catch (Throwable t) {
            throw new IllegalArgumentException("Error reading indexed property '" + name + "', exception - " + t);
        }
        return value;
    }
    
    public Object get(final String name, final String key) {
        Object value = null;
        try {
            value = PropertyUtils.getMappedProperty(this.instance, name, key);
        }
        catch (InvocationTargetException ite) {
            final Throwable cause = ite.getTargetException();
            throw new IllegalArgumentException("Error reading mapped property '" + name + "' nested exception - " + cause);
        }
        catch (Throwable t) {
            throw new IllegalArgumentException("Error reading mapped property '" + name + "', exception - " + t);
        }
        return value;
    }
    
    public DynaClass getDynaClass() {
        if (this.dynaClass == null) {
            this.dynaClass = WrapDynaClass.createDynaClass(this.instance.getClass());
        }
        return this.dynaClass;
    }
    
    public void remove(final String name, final String key) {
        throw new UnsupportedOperationException("WrapDynaBean does not support remove()");
    }
    
    public void set(final String name, final Object value) {
        try {
            PropertyUtils.setSimpleProperty(this.instance, name, value);
        }
        catch (InvocationTargetException ite) {
            final Throwable cause = ite.getTargetException();
            throw new IllegalArgumentException("Error setting property '" + name + "' nested exception -" + cause);
        }
        catch (Throwable t) {
            throw new IllegalArgumentException("Error setting property '" + name + "', exception - " + t);
        }
    }
    
    public void set(final String name, final int index, final Object value) {
        try {
            PropertyUtils.setIndexedProperty(this.instance, name, index, value);
        }
        catch (IndexOutOfBoundsException e) {
            throw e;
        }
        catch (InvocationTargetException ite) {
            final Throwable cause = ite.getTargetException();
            throw new IllegalArgumentException("Error setting indexed property '" + name + "' nested exception - " + cause);
        }
        catch (Throwable t) {
            throw new IllegalArgumentException("Error setting indexed property '" + name + "', exception - " + t);
        }
    }
    
    public void set(final String name, final String key, final Object value) {
        try {
            PropertyUtils.setMappedProperty(this.instance, name, key, value);
        }
        catch (InvocationTargetException ite) {
            final Throwable cause = ite.getTargetException();
            throw new IllegalArgumentException("Error setting mapped property '" + name + "' nested exception - " + cause);
        }
        catch (Throwable t) {
            throw new IllegalArgumentException("Error setting mapped property '" + name + "', exception - " + t);
        }
    }
    
    public Object getInstance() {
        return this.instance;
    }
    
    protected DynaProperty getDynaProperty(final String name) {
        final DynaProperty descriptor = this.getDynaClass().getDynaProperty(name);
        if (descriptor == null) {
            throw new IllegalArgumentException("Invalid property name '" + name + "'");
        }
        return descriptor;
    }
}
