// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import org.apache.commons.collections.FastHashMap;
import java.lang.reflect.Method;
import java.beans.IndexedPropertyDescriptor;
import java.util.List;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.Map;
import java.beans.Introspector;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.expression.DefaultResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.beanutils.expression.Resolver;

public class PropertyUtilsBean
{
    private Resolver resolver;
    private WeakFastHashMap descriptorsCache;
    private WeakFastHashMap mappedDescriptorsCache;
    private static final Class[] EMPTY_CLASS_PARAMETERS;
    private static final Class[] LIST_CLASS_PARAMETER;
    private static final Object[] EMPTY_OBJECT_ARRAY;
    private Log log;
    
    protected static PropertyUtilsBean getInstance() {
        return BeanUtilsBean.getInstance().getPropertyUtils();
    }
    
    public PropertyUtilsBean() {
        this.resolver = new DefaultResolver();
        this.descriptorsCache = null;
        this.mappedDescriptorsCache = null;
        this.log = LogFactory.getLog(PropertyUtils.class);
        (this.descriptorsCache = new WeakFastHashMap()).setFast(true);
        (this.mappedDescriptorsCache = new WeakFastHashMap()).setFast(true);
    }
    
    public Resolver getResolver() {
        return this.resolver;
    }
    
    public void setResolver(final Resolver resolver) {
        if (resolver == null) {
            this.resolver = new DefaultResolver();
        }
        else {
            this.resolver = resolver;
        }
    }
    
    public void clearDescriptors() {
        this.descriptorsCache.clear();
        this.mappedDescriptorsCache.clear();
        Introspector.flushCaches();
    }
    
    public void copyProperties(final Object dest, final Object orig) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (dest == null) {
            throw new IllegalArgumentException("No destination bean specified");
        }
        if (orig == null) {
            throw new IllegalArgumentException("No origin bean specified");
        }
        if (orig instanceof DynaBean) {
            final DynaProperty[] origDescriptors = ((DynaBean)orig).getDynaClass().getDynaProperties();
            for (int i = 0; i < origDescriptors.length; ++i) {
                final String name = origDescriptors[i].getName();
                if (this.isReadable(orig, name) && this.isWriteable(dest, name)) {
                    try {
                        final Object value = ((DynaBean)orig).get(name);
                        if (dest instanceof DynaBean) {
                            ((DynaBean)dest).set(name, value);
                        }
                        else {
                            this.setSimpleProperty(dest, name, value);
                        }
                    }
                    catch (NoSuchMethodException e) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Error writing to '" + name + "' on class '" + dest.getClass() + "'", e);
                        }
                    }
                }
            }
        }
        else if (orig instanceof Map) {
            for (final Map.Entry entry : ((Map)orig).entrySet()) {
                final String name = entry.getKey();
                if (this.isWriteable(dest, name)) {
                    try {
                        if (dest instanceof DynaBean) {
                            ((DynaBean)dest).set(name, entry.getValue());
                        }
                        else {
                            this.setSimpleProperty(dest, name, entry.getValue());
                        }
                    }
                    catch (NoSuchMethodException e) {
                        if (!this.log.isDebugEnabled()) {
                            continue;
                        }
                        this.log.debug("Error writing to '" + name + "' on class '" + dest.getClass() + "'", e);
                    }
                }
            }
        }
        else {
            final PropertyDescriptor[] origDescriptors2 = this.getPropertyDescriptors(orig);
            for (int i = 0; i < origDescriptors2.length; ++i) {
                final String name = origDescriptors2[i].getName();
                if (this.isReadable(orig, name) && this.isWriteable(dest, name)) {
                    try {
                        final Object value = this.getSimpleProperty(orig, name);
                        if (dest instanceof DynaBean) {
                            ((DynaBean)dest).set(name, value);
                        }
                        else {
                            this.setSimpleProperty(dest, name, value);
                        }
                    }
                    catch (NoSuchMethodException e) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Error writing to '" + name + "' on class '" + dest.getClass() + "'", e);
                        }
                    }
                }
            }
        }
    }
    
    public Map describe(final Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        final Map description = new HashMap();
        if (bean instanceof DynaBean) {
            final DynaProperty[] descriptors = ((DynaBean)bean).getDynaClass().getDynaProperties();
            for (int i = 0; i < descriptors.length; ++i) {
                final String name = descriptors[i].getName();
                description.put(name, this.getProperty(bean, name));
            }
        }
        else {
            final PropertyDescriptor[] descriptors2 = this.getPropertyDescriptors(bean);
            for (int i = 0; i < descriptors2.length; ++i) {
                final String name = descriptors2[i].getName();
                if (descriptors2[i].getReadMethod() != null) {
                    description.put(name, this.getProperty(bean, name));
                }
            }
        }
        return description;
    }
    
    public Object getIndexedProperty(final Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        int index = -1;
        try {
            index = this.resolver.getIndex(name);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid indexed property '" + name + "' on bean class '" + bean.getClass() + "' " + e.getMessage());
        }
        if (index < 0) {
            throw new IllegalArgumentException("Invalid indexed property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        name = this.resolver.getProperty(name);
        return this.getIndexedProperty(bean, name, index);
    }
    
    public Object getIndexedProperty(final Object bean, final String name, final int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null || name.length() == 0) {
            if (bean.getClass().isArray()) {
                return Array.get(bean, index);
            }
            if (bean instanceof List) {
                return ((List)bean).get(index);
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        if (bean instanceof DynaBean) {
            final DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
            if (descriptor == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on bean class '" + bean.getClass() + "'");
            }
            return ((DynaBean)bean).get(name, index);
        }
        else {
            final PropertyDescriptor descriptor2 = this.getPropertyDescriptor(bean, name);
            if (descriptor2 == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on bean class '" + bean.getClass() + "'");
            }
            if (descriptor2 instanceof IndexedPropertyDescriptor) {
                Method readMethod = ((IndexedPropertyDescriptor)descriptor2).getIndexedReadMethod();
                readMethod = MethodUtils.getAccessibleMethod(bean.getClass(), readMethod);
                if (readMethod != null) {
                    final Object[] subscript = { new Integer(index) };
                    try {
                        return this.invokeMethod(readMethod, bean, subscript);
                    }
                    catch (InvocationTargetException e) {
                        if (e.getTargetException() instanceof IndexOutOfBoundsException) {
                            throw (IndexOutOfBoundsException)e.getTargetException();
                        }
                        throw e;
                    }
                }
            }
            Method readMethod = this.getReadMethod(bean.getClass(), descriptor2);
            if (readMethod == null) {
                throw new NoSuchMethodException("Property '" + name + "' has no " + "getter method on bean class '" + bean.getClass() + "'");
            }
            final Object value = this.invokeMethod(readMethod, bean, PropertyUtilsBean.EMPTY_OBJECT_ARRAY);
            if (value.getClass().isArray()) {
                return Array.get(value, index);
            }
            if (!(value instanceof List)) {
                throw new IllegalArgumentException("Property '" + name + "' is not indexed on bean class '" + bean.getClass() + "'");
            }
            return ((List)value).get(index);
        }
    }
    
    public Object getMappedProperty(final Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        String key = null;
        try {
            key = this.resolver.getKey(name);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid mapped property '" + name + "' on bean class '" + bean.getClass() + "' " + e.getMessage());
        }
        if (key == null) {
            throw new IllegalArgumentException("Invalid mapped property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        name = this.resolver.getProperty(name);
        return this.getMappedProperty(bean, name, key);
    }
    
    public Object getMappedProperty(final Object bean, final String name, final String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        if (key == null) {
            throw new IllegalArgumentException("No key specified for property '" + name + "' on bean class " + bean.getClass() + "'");
        }
        if (bean instanceof DynaBean) {
            final DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
            if (descriptor == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "'+ on bean class '" + bean.getClass() + "'");
            }
            return ((DynaBean)bean).get(name, key);
        }
        else {
            Object result = null;
            final PropertyDescriptor descriptor2 = this.getPropertyDescriptor(bean, name);
            if (descriptor2 == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "'+ on bean class '" + bean.getClass() + "'");
            }
            if (descriptor2 instanceof MappedPropertyDescriptor) {
                Method readMethod = ((MappedPropertyDescriptor)descriptor2).getMappedReadMethod();
                readMethod = MethodUtils.getAccessibleMethod(bean.getClass(), readMethod);
                if (readMethod == null) {
                    throw new NoSuchMethodException("Property '" + name + "' has no mapped getter method on bean class '" + bean.getClass() + "'");
                }
                final Object[] keyArray = { key };
                result = this.invokeMethod(readMethod, bean, keyArray);
            }
            else {
                final Method readMethod = this.getReadMethod(bean.getClass(), descriptor2);
                if (readMethod == null) {
                    throw new NoSuchMethodException("Property '" + name + "' has no mapped getter method on bean class '" + bean.getClass() + "'");
                }
                final Object invokeResult = this.invokeMethod(readMethod, bean, PropertyUtilsBean.EMPTY_OBJECT_ARRAY);
                if (invokeResult instanceof Map) {
                    result = ((Map)invokeResult).get(key);
                }
            }
            return result;
        }
    }
    
    public FastHashMap getMappedPropertyDescriptors(final Class beanClass) {
        if (beanClass == null) {
            return null;
        }
        return (FastHashMap)this.mappedDescriptorsCache.get(beanClass);
    }
    
    public FastHashMap getMappedPropertyDescriptors(final Object bean) {
        if (bean == null) {
            return null;
        }
        return this.getMappedPropertyDescriptors(bean.getClass());
    }
    
    public Object getNestedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        while (this.resolver.hasNested(name)) {
            final String next = this.resolver.next(name);
            Object nestedBean = null;
            if (bean instanceof Map) {
                nestedBean = this.getPropertyOfMapBean((Map)bean, next);
            }
            else if (this.resolver.isMapped(next)) {
                nestedBean = this.getMappedProperty(bean, next);
            }
            else if (this.resolver.isIndexed(next)) {
                nestedBean = this.getIndexedProperty(bean, next);
            }
            else {
                nestedBean = this.getSimpleProperty(bean, next);
            }
            if (nestedBean == null) {
                throw new NestedNullException("Null property value for '" + name + "' on bean class '" + bean.getClass() + "'");
            }
            bean = nestedBean;
            name = this.resolver.remove(name);
        }
        if (bean instanceof Map) {
            bean = this.getPropertyOfMapBean((Map)bean, name);
        }
        else if (this.resolver.isMapped(name)) {
            bean = this.getMappedProperty(bean, name);
        }
        else if (this.resolver.isIndexed(name)) {
            bean = this.getIndexedProperty(bean, name);
        }
        else {
            bean = this.getSimpleProperty(bean, name);
        }
        return bean;
    }
    
    protected Object getPropertyOfMapBean(final Map bean, String propertyName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (this.resolver.isMapped(propertyName)) {
            final String name = this.resolver.getProperty(propertyName);
            if (name == null || name.length() == 0) {
                propertyName = this.resolver.getKey(propertyName);
            }
        }
        if (this.resolver.isIndexed(propertyName) || this.resolver.isMapped(propertyName)) {
            throw new IllegalArgumentException("Indexed or mapped properties are not supported on objects of type Map: " + propertyName);
        }
        return bean.get(propertyName);
    }
    
    public Object getProperty(final Object bean, final String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return this.getNestedProperty(bean, name);
    }
    
    public PropertyDescriptor getPropertyDescriptor(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        while (this.resolver.hasNested(name)) {
            final String next = this.resolver.next(name);
            final Object nestedBean = this.getProperty(bean, next);
            if (nestedBean == null) {
                throw new NestedNullException("Null property value for '" + next + "' on bean class '" + bean.getClass() + "'");
            }
            bean = nestedBean;
            name = this.resolver.remove(name);
        }
        name = this.resolver.getProperty(name);
        if (bean == null || name == null) {
            return null;
        }
        final PropertyDescriptor[] descriptors = this.getPropertyDescriptors(bean);
        if (descriptors != null) {
            for (int i = 0; i < descriptors.length; ++i) {
                if (name.equals(descriptors[i].getName())) {
                    return descriptors[i];
                }
            }
        }
        PropertyDescriptor result = null;
        FastHashMap mappedDescriptors = this.getMappedPropertyDescriptors(bean);
        if (mappedDescriptors == null) {
            mappedDescriptors = new FastHashMap();
            mappedDescriptors.setFast(true);
            this.mappedDescriptorsCache.put(bean.getClass(), mappedDescriptors);
        }
        result = (PropertyDescriptor)mappedDescriptors.get(name);
        if (result == null) {
            try {
                result = new MappedPropertyDescriptor(name, bean.getClass());
            }
            catch (IntrospectionException ex) {}
            if (result != null) {
                mappedDescriptors.put(name, result);
            }
        }
        return result;
    }
    
    public PropertyDescriptor[] getPropertyDescriptors(final Class beanClass) {
        if (beanClass == null) {
            throw new IllegalArgumentException("No bean class specified");
        }
        PropertyDescriptor[] descriptors = null;
        descriptors = (PropertyDescriptor[])this.descriptorsCache.get(beanClass);
        if (descriptors != null) {
            return descriptors;
        }
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(beanClass);
        }
        catch (IntrospectionException e2) {
            return new PropertyDescriptor[0];
        }
        descriptors = beanInfo.getPropertyDescriptors();
        if (descriptors == null) {
            descriptors = new PropertyDescriptor[0];
        }
        for (int i = 0; i < descriptors.length; ++i) {
            if (descriptors[i] instanceof IndexedPropertyDescriptor) {
                final IndexedPropertyDescriptor descriptor = (IndexedPropertyDescriptor)descriptors[i];
                final String propName = descriptor.getName().substring(0, 1).toUpperCase() + descriptor.getName().substring(1);
                if (descriptor.getReadMethod() == null) {
                    final String methodName = (descriptor.getIndexedReadMethod() != null) ? descriptor.getIndexedReadMethod().getName() : ("get" + propName);
                    final Method readMethod = MethodUtils.getMatchingAccessibleMethod(beanClass, methodName, PropertyUtilsBean.EMPTY_CLASS_PARAMETERS);
                    if (readMethod != null) {
                        try {
                            descriptor.setReadMethod(readMethod);
                        }
                        catch (Exception e) {
                            this.log.error("Error setting indexed property read method", e);
                        }
                    }
                }
                if (descriptor.getWriteMethod() == null) {
                    final String methodName = (descriptor.getIndexedWriteMethod() != null) ? descriptor.getIndexedWriteMethod().getName() : ("set" + propName);
                    Method writeMethod = MethodUtils.getMatchingAccessibleMethod(beanClass, methodName, PropertyUtilsBean.LIST_CLASS_PARAMETER);
                    if (writeMethod == null) {
                        final Method[] methods = beanClass.getMethods();
                        for (int j = 0; j < methods.length; ++j) {
                            if (methods[j].getName().equals(methodName)) {
                                final Class[] parameterTypes = methods[j].getParameterTypes();
                                if (parameterTypes.length == 1 && List.class.isAssignableFrom(parameterTypes[0])) {
                                    writeMethod = methods[j];
                                    break;
                                }
                            }
                        }
                    }
                    if (writeMethod != null) {
                        try {
                            descriptor.setWriteMethod(writeMethod);
                        }
                        catch (Exception e) {
                            this.log.error("Error setting indexed property write method", e);
                        }
                    }
                }
            }
        }
        this.descriptorsCache.put(beanClass, descriptors);
        return descriptors;
    }
    
    public PropertyDescriptor[] getPropertyDescriptors(final Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        return this.getPropertyDescriptors(bean.getClass());
    }
    
    public Class getPropertyEditorClass(final Object bean, final String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        final PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
        if (descriptor != null) {
            return descriptor.getPropertyEditorClass();
        }
        return null;
    }
    
    public Class getPropertyType(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        while (this.resolver.hasNested(name)) {
            final String next = this.resolver.next(name);
            final Object nestedBean = this.getProperty(bean, next);
            if (nestedBean == null) {
                throw new NestedNullException("Null property value for '" + next + "' on bean class '" + bean.getClass() + "'");
            }
            bean = nestedBean;
            name = this.resolver.remove(name);
        }
        name = this.resolver.getProperty(name);
        if (bean instanceof DynaBean) {
            final DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
            if (descriptor == null) {
                return null;
            }
            final Class type = descriptor.getType();
            if (type == null) {
                return null;
            }
            if (type.isArray()) {
                return type.getComponentType();
            }
            return type;
        }
        else {
            final PropertyDescriptor descriptor2 = this.getPropertyDescriptor(bean, name);
            if (descriptor2 == null) {
                return null;
            }
            if (descriptor2 instanceof IndexedPropertyDescriptor) {
                return ((IndexedPropertyDescriptor)descriptor2).getIndexedPropertyType();
            }
            if (descriptor2 instanceof MappedPropertyDescriptor) {
                return ((MappedPropertyDescriptor)descriptor2).getMappedPropertyType();
            }
            return descriptor2.getPropertyType();
        }
    }
    
    public Method getReadMethod(final PropertyDescriptor descriptor) {
        return MethodUtils.getAccessibleMethod(descriptor.getReadMethod());
    }
    
    Method getReadMethod(final Class clazz, final PropertyDescriptor descriptor) {
        return MethodUtils.getAccessibleMethod(clazz, descriptor.getReadMethod());
    }
    
    public Object getSimpleProperty(final Object bean, final String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        if (this.resolver.hasNested(name)) {
            throw new IllegalArgumentException("Nested property names are not allowed: Property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (this.resolver.isIndexed(name)) {
            throw new IllegalArgumentException("Indexed property names are not allowed: Property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (this.resolver.isMapped(name)) {
            throw new IllegalArgumentException("Mapped property names are not allowed: Property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (bean instanceof DynaBean) {
            final DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
            if (descriptor == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on dynaclass '" + ((DynaBean)bean).getDynaClass() + "'");
            }
            return ((DynaBean)bean).get(name);
        }
        else {
            final PropertyDescriptor descriptor2 = this.getPropertyDescriptor(bean, name);
            if (descriptor2 == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on class '" + bean.getClass() + "'");
            }
            final Method readMethod = this.getReadMethod(bean.getClass(), descriptor2);
            if (readMethod == null) {
                throw new NoSuchMethodException("Property '" + name + "' has no getter method in class '" + bean.getClass() + "'");
            }
            final Object value = this.invokeMethod(readMethod, bean, PropertyUtilsBean.EMPTY_OBJECT_ARRAY);
            return value;
        }
    }
    
    public Method getWriteMethod(final PropertyDescriptor descriptor) {
        return MethodUtils.getAccessibleMethod(descriptor.getWriteMethod());
    }
    
    Method getWriteMethod(final Class clazz, final PropertyDescriptor descriptor) {
        return MethodUtils.getAccessibleMethod(clazz, descriptor.getWriteMethod());
    }
    
    public boolean isReadable(Object bean, String name) {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        while (this.resolver.hasNested(name)) {
            final String next = this.resolver.next(name);
            Object nestedBean = null;
            try {
                nestedBean = this.getProperty(bean, next);
            }
            catch (IllegalAccessException e) {
                return false;
            }
            catch (InvocationTargetException e2) {
                return false;
            }
            catch (NoSuchMethodException e3) {
                return false;
            }
            if (nestedBean == null) {
                throw new NestedNullException("Null property value for '" + next + "' on bean class '" + bean.getClass() + "'");
            }
            bean = nestedBean;
            name = this.resolver.remove(name);
        }
        name = this.resolver.getProperty(name);
        if (bean instanceof WrapDynaBean) {
            bean = ((WrapDynaBean)bean).getInstance();
        }
        if (bean instanceof DynaBean) {
            return ((DynaBean)bean).getDynaClass().getDynaProperty(name) != null;
        }
        try {
            final PropertyDescriptor desc = this.getPropertyDescriptor(bean, name);
            if (desc != null) {
                Method readMethod = this.getReadMethod(bean.getClass(), desc);
                if (readMethod == null) {
                    if (desc instanceof IndexedPropertyDescriptor) {
                        readMethod = ((IndexedPropertyDescriptor)desc).getIndexedReadMethod();
                    }
                    else if (desc instanceof MappedPropertyDescriptor) {
                        readMethod = ((MappedPropertyDescriptor)desc).getMappedReadMethod();
                    }
                    readMethod = MethodUtils.getAccessibleMethod(bean.getClass(), readMethod);
                }
                return readMethod != null;
            }
            return false;
        }
        catch (IllegalAccessException e4) {
            return false;
        }
        catch (InvocationTargetException e5) {
            return false;
        }
        catch (NoSuchMethodException e6) {
            return false;
        }
    }
    
    public boolean isWriteable(Object bean, String name) {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        while (this.resolver.hasNested(name)) {
            final String next = this.resolver.next(name);
            Object nestedBean = null;
            try {
                nestedBean = this.getProperty(bean, next);
            }
            catch (IllegalAccessException e) {
                return false;
            }
            catch (InvocationTargetException e2) {
                return false;
            }
            catch (NoSuchMethodException e3) {
                return false;
            }
            if (nestedBean == null) {
                throw new NestedNullException("Null property value for '" + next + "' on bean class '" + bean.getClass() + "'");
            }
            bean = nestedBean;
            name = this.resolver.remove(name);
        }
        name = this.resolver.getProperty(name);
        if (bean instanceof WrapDynaBean) {
            bean = ((WrapDynaBean)bean).getInstance();
        }
        if (bean instanceof DynaBean) {
            return ((DynaBean)bean).getDynaClass().getDynaProperty(name) != null;
        }
        try {
            final PropertyDescriptor desc = this.getPropertyDescriptor(bean, name);
            if (desc != null) {
                Method writeMethod = this.getWriteMethod(bean.getClass(), desc);
                if (writeMethod == null) {
                    if (desc instanceof IndexedPropertyDescriptor) {
                        writeMethod = ((IndexedPropertyDescriptor)desc).getIndexedWriteMethod();
                    }
                    else if (desc instanceof MappedPropertyDescriptor) {
                        writeMethod = ((MappedPropertyDescriptor)desc).getMappedWriteMethod();
                    }
                    writeMethod = MethodUtils.getAccessibleMethod(bean.getClass(), writeMethod);
                }
                return writeMethod != null;
            }
            return false;
        }
        catch (IllegalAccessException e4) {
            return false;
        }
        catch (InvocationTargetException e5) {
            return false;
        }
        catch (NoSuchMethodException e6) {
            return false;
        }
    }
    
    public void setIndexedProperty(final Object bean, String name, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        int index = -1;
        try {
            index = this.resolver.getIndex(name);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid indexed property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Invalid indexed property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        name = this.resolver.getProperty(name);
        this.setIndexedProperty(bean, name, index, value);
    }
    
    public void setIndexedProperty(final Object bean, final String name, final int index, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null || name.length() == 0) {
            if (bean.getClass().isArray()) {
                Array.set(bean, index, value);
                return;
            }
            if (bean instanceof List) {
                ((List)bean).set(index, value);
                return;
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        if (bean instanceof DynaBean) {
            final DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
            if (descriptor == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on bean class '" + bean.getClass() + "'");
            }
            ((DynaBean)bean).set(name, index, value);
        }
        else {
            final PropertyDescriptor descriptor2 = this.getPropertyDescriptor(bean, name);
            if (descriptor2 == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on bean class '" + bean.getClass() + "'");
            }
            if (descriptor2 instanceof IndexedPropertyDescriptor) {
                Method writeMethod = ((IndexedPropertyDescriptor)descriptor2).getIndexedWriteMethod();
                writeMethod = MethodUtils.getAccessibleMethod(bean.getClass(), writeMethod);
                if (writeMethod != null) {
                    final Object[] subscript = { new Integer(index), value };
                    try {
                        if (this.log.isTraceEnabled()) {
                            final String valueClassName = (value == null) ? "<null>" : value.getClass().getName();
                            this.log.trace("setSimpleProperty: Invoking method " + writeMethod + " with index=" + index + ", value=" + value + " (class " + valueClassName + ")");
                        }
                        this.invokeMethod(writeMethod, bean, subscript);
                    }
                    catch (InvocationTargetException e) {
                        if (e.getTargetException() instanceof IndexOutOfBoundsException) {
                            throw (IndexOutOfBoundsException)e.getTargetException();
                        }
                        throw e;
                    }
                    return;
                }
            }
            final Method readMethod = this.getReadMethod(bean.getClass(), descriptor2);
            if (readMethod == null) {
                throw new NoSuchMethodException("Property '" + name + "' has no getter method on bean class '" + bean.getClass() + "'");
            }
            final Object array = this.invokeMethod(readMethod, bean, PropertyUtilsBean.EMPTY_OBJECT_ARRAY);
            if (!array.getClass().isArray()) {
                if (!(array instanceof List)) {
                    throw new IllegalArgumentException("Property '" + name + "' is not indexed on bean class '" + bean.getClass() + "'");
                }
                ((List)array).set(index, value);
            }
            else {
                Array.set(array, index, value);
            }
        }
    }
    
    public void setMappedProperty(final Object bean, String name, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        String key = null;
        try {
            key = this.resolver.getKey(name);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid mapped property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (key == null) {
            throw new IllegalArgumentException("Invalid mapped property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        name = this.resolver.getProperty(name);
        this.setMappedProperty(bean, name, key, value);
    }
    
    public void setMappedProperty(final Object bean, final String name, final String key, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        if (key == null) {
            throw new IllegalArgumentException("No key specified for property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (bean instanceof DynaBean) {
            final DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
            if (descriptor == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on bean class '" + bean.getClass() + "'");
            }
            ((DynaBean)bean).set(name, key, value);
        }
        else {
            final PropertyDescriptor descriptor2 = this.getPropertyDescriptor(bean, name);
            if (descriptor2 == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on bean class '" + bean.getClass() + "'");
            }
            if (descriptor2 instanceof MappedPropertyDescriptor) {
                Method mappedWriteMethod = ((MappedPropertyDescriptor)descriptor2).getMappedWriteMethod();
                mappedWriteMethod = MethodUtils.getAccessibleMethod(bean.getClass(), mappedWriteMethod);
                if (mappedWriteMethod == null) {
                    throw new NoSuchMethodException("Property '" + name + "' has no mapped setter method" + "on bean class '" + bean.getClass() + "'");
                }
                final Object[] params = { key, value };
                if (this.log.isTraceEnabled()) {
                    final String valueClassName = (value == null) ? "<null>" : value.getClass().getName();
                    this.log.trace("setSimpleProperty: Invoking method " + mappedWriteMethod + " with key=" + key + ", value=" + value + " (class " + valueClassName + ")");
                }
                this.invokeMethod(mappedWriteMethod, bean, params);
            }
            else {
                final Method readMethod = this.getReadMethod(bean.getClass(), descriptor2);
                if (readMethod == null) {
                    throw new NoSuchMethodException("Property '" + name + "' has no mapped getter method on bean class '" + bean.getClass() + "'");
                }
                final Object invokeResult = this.invokeMethod(readMethod, bean, PropertyUtilsBean.EMPTY_OBJECT_ARRAY);
                if (invokeResult instanceof Map) {
                    ((Map)invokeResult).put(key, value);
                }
            }
        }
    }
    
    public void setNestedProperty(Object bean, String name, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        while (this.resolver.hasNested(name)) {
            final String next = this.resolver.next(name);
            Object nestedBean = null;
            if (bean instanceof Map) {
                nestedBean = this.getPropertyOfMapBean((Map)bean, next);
            }
            else if (this.resolver.isMapped(next)) {
                nestedBean = this.getMappedProperty(bean, next);
            }
            else if (this.resolver.isIndexed(next)) {
                nestedBean = this.getIndexedProperty(bean, next);
            }
            else {
                nestedBean = this.getSimpleProperty(bean, next);
            }
            if (nestedBean == null) {
                throw new NestedNullException("Null property value for '" + name + "' on bean class '" + bean.getClass() + "'");
            }
            bean = nestedBean;
            name = this.resolver.remove(name);
        }
        if (bean instanceof Map) {
            this.setPropertyOfMapBean((Map)bean, name, value);
        }
        else if (this.resolver.isMapped(name)) {
            this.setMappedProperty(bean, name, value);
        }
        else if (this.resolver.isIndexed(name)) {
            this.setIndexedProperty(bean, name, value);
        }
        else {
            this.setSimpleProperty(bean, name, value);
        }
    }
    
    protected void setPropertyOfMapBean(final Map bean, String propertyName, final Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (this.resolver.isMapped(propertyName)) {
            final String name = this.resolver.getProperty(propertyName);
            if (name == null || name.length() == 0) {
                propertyName = this.resolver.getKey(propertyName);
            }
        }
        if (this.resolver.isIndexed(propertyName) || this.resolver.isMapped(propertyName)) {
            throw new IllegalArgumentException("Indexed or mapped properties are not supported on objects of type Map: " + propertyName);
        }
        bean.put(propertyName, value);
    }
    
    public void setProperty(final Object bean, final String name, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        this.setNestedProperty(bean, name, value);
    }
    
    public void setSimpleProperty(final Object bean, final String name, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        if (this.resolver.hasNested(name)) {
            throw new IllegalArgumentException("Nested property names are not allowed: Property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (this.resolver.isIndexed(name)) {
            throw new IllegalArgumentException("Indexed property names are not allowed: Property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (this.resolver.isMapped(name)) {
            throw new IllegalArgumentException("Mapped property names are not allowed: Property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        if (bean instanceof DynaBean) {
            final DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
            if (descriptor == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on dynaclass '" + ((DynaBean)bean).getDynaClass() + "'");
            }
            ((DynaBean)bean).set(name, value);
        }
        else {
            final PropertyDescriptor descriptor2 = this.getPropertyDescriptor(bean, name);
            if (descriptor2 == null) {
                throw new NoSuchMethodException("Unknown property '" + name + "' on class '" + bean.getClass() + "'");
            }
            final Method writeMethod = this.getWriteMethod(bean.getClass(), descriptor2);
            if (writeMethod == null) {
                throw new NoSuchMethodException("Property '" + name + "' has no setter method in class '" + bean.getClass() + "'");
            }
            final Object[] values = { value };
            if (this.log.isTraceEnabled()) {
                final String valueClassName = (value == null) ? "<null>" : value.getClass().getName();
                this.log.trace("setSimpleProperty: Invoking method " + writeMethod + " with value " + value + " (class " + valueClassName + ")");
            }
            this.invokeMethod(writeMethod, bean, values);
        }
    }
    
    private Object invokeMethod(final Method method, final Object bean, final Object[] values) throws IllegalAccessException, InvocationTargetException {
        try {
            return method.invoke(bean, values);
        }
        catch (IllegalArgumentException cause) {
            if (bean == null) {
                throw new IllegalArgumentException("No bean specified - this should have been checked before reaching this method");
            }
            String valueString = "";
            if (values != null) {
                for (int i = 0; i < values.length; ++i) {
                    if (i > 0) {
                        valueString += ", ";
                    }
                    valueString += values[i].getClass().getName();
                }
            }
            String expectedString = "";
            final Class[] parTypes = method.getParameterTypes();
            if (parTypes != null) {
                for (int j = 0; j < parTypes.length; ++j) {
                    if (j > 0) {
                        expectedString += ", ";
                    }
                    expectedString += parTypes[j].getName();
                }
            }
            final IllegalArgumentException e = new IllegalArgumentException("Cannot invoke " + method.getDeclaringClass().getName() + "." + method.getName() + " on bean class '" + bean.getClass() + "' - " + cause.getMessage() + " - had objects of type \"" + valueString + "\" but expected signature \"" + expectedString + "\"");
            if (!BeanUtils.initCause(e, cause)) {
                this.log.error("Method invocation failed", cause);
            }
            throw e;
        }
    }
    
    static {
        EMPTY_CLASS_PARAMETERS = new Class[0];
        LIST_CLASS_PARAMETER = new Class[] { List.class };
        EMPTY_OBJECT_ARRAY = new Object[0];
    }
}
