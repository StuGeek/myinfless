// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.collections.comparators.ComparableComparator;
import java.io.Serializable;
import java.util.Comparator;

public class BeanComparator implements Comparator, Serializable
{
    private String property;
    private Comparator comparator;
    
    public BeanComparator() {
        this(null);
    }
    
    public BeanComparator(final String property) {
        this(property, ComparableComparator.getInstance());
    }
    
    public BeanComparator(final String property, final Comparator comparator) {
        this.setProperty(property);
        if (comparator != null) {
            this.comparator = comparator;
        }
        else {
            this.comparator = ComparableComparator.getInstance();
        }
    }
    
    public void setProperty(final String property) {
        this.property = property;
    }
    
    public String getProperty() {
        return this.property;
    }
    
    public Comparator getComparator() {
        return this.comparator;
    }
    
    public int compare(final Object o1, final Object o2) {
        if (this.property == null) {
            return this.comparator.compare(o1, o2);
        }
        try {
            final Object value1 = PropertyUtils.getProperty(o1, this.property);
            final Object value2 = PropertyUtils.getProperty(o2, this.property);
            return this.comparator.compare(value1, value2);
        }
        catch (IllegalAccessException iae) {
            throw new RuntimeException("IllegalAccessException: " + iae.toString());
        }
        catch (InvocationTargetException ite) {
            throw new RuntimeException("InvocationTargetException: " + ite.toString());
        }
        catch (NoSuchMethodException nsme) {
            throw new RuntimeException("NoSuchMethodException: " + nsme.toString());
        }
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeanComparator)) {
            return false;
        }
        final BeanComparator beanComparator = (BeanComparator)o;
        if (!this.comparator.equals(beanComparator.comparator)) {
            return false;
        }
        if (this.property != null) {
            return this.property.equals(beanComparator.property);
        }
        return beanComparator.property == null;
    }
    
    public int hashCode() {
        final int result = this.comparator.hashCode();
        return result;
    }
}
