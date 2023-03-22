// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import org.apache.commons.logging.LogFactory;
import net.sf.json.util.CycleDetectionStrategy;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.collections.map.ListOrderedMap;
import net.sf.json.util.PropertySetStrategy;
import net.sf.json.util.EnumMorpher;
import net.sf.json.regexp.RegexpUtils;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.DynaProperty;
import java.lang.reflect.Field;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.processors.JsonVerifier;
import java.lang.reflect.AnnotatedElement;
import net.sf.json.processors.JsonBeanProcessor;
import java.util.Collection;
import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.array.ObjectArrayMorpher;
import net.sf.ezmorph.bean.BeanMorpher;
import net.sf.ezmorph.object.IdentityObjectMorpher;
import java.lang.reflect.Array;
import java.beans.PropertyDescriptor;
import net.sf.json.processors.PropertyNameProcessor;
import net.sf.json.util.PropertyFilter;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import net.sf.json.util.JSONTokener;
import org.apache.commons.beanutils.DynaBean;
import java.lang.annotation.Annotation;
import net.sf.json.util.JSONUtils;
import org.apache.commons.logging.Log;
import java.util.Map;

public final class JSONObject extends AbstractJSON implements JSON, Map, Comparable
{
    private static final Log log;
    private boolean nullObject;
    private Map properties;
    
    public static JSONObject fromObject(final Object object) {
        return fromObject(object, new JsonConfig());
    }
    
    public static JSONObject fromObject(final Object object, final JsonConfig jsonConfig) {
        if (object == null || JSONUtils.isNull(object)) {
            return new JSONObject(true);
        }
        if (object instanceof Enum) {
            throw new JSONException("'object' is an Enum. Use JSONArray instead");
        }
        if (object instanceof Annotation || (object != null && object.getClass().isAnnotation())) {
            throw new JSONException("'object' is an Annotation.");
        }
        if (object instanceof JSONObject) {
            return _fromJSONObject((JSONObject)object, jsonConfig);
        }
        if (object instanceof DynaBean) {
            return _fromDynaBean((DynaBean)object, jsonConfig);
        }
        if (object instanceof JSONTokener) {
            return _fromJSONTokener((JSONTokener)object, jsonConfig);
        }
        if (object instanceof JSONString) {
            return _fromJSONString((JSONString)object, jsonConfig);
        }
        if (object instanceof Map) {
            return _fromMap((Map)object, jsonConfig);
        }
        if (object instanceof String) {
            return _fromString((String)object, jsonConfig);
        }
        if (JSONUtils.isNumber(object) || JSONUtils.isBoolean(object) || JSONUtils.isString(object)) {
            return new JSONObject();
        }
        if (JSONUtils.isArray(object)) {
            throw new JSONException("'object' is an array. Use JSONArray instead");
        }
        return _fromBean(object, jsonConfig);
    }
    
    public static Object toBean(final JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.isNullObject()) {
            return null;
        }
        DynaBean dynaBean = null;
        final JsonConfig jsonConfig = new JsonConfig();
        final Map props = JSONUtils.getProperties(jsonObject);
        dynaBean = JSONUtils.newDynaBean(jsonObject, jsonConfig);
        for (final String name : jsonObject.names(jsonConfig)) {
            final String key = JSONUtils.convertToJavaIdentifier(name, jsonConfig);
            final Class type = props.get(name);
            final Object value = jsonObject.get(name);
            try {
                if (!JSONUtils.isNull(value)) {
                    if (value instanceof JSONArray) {
                        dynaBean.set(key, JSONArray.toCollection((JSONArray)value));
                    }
                    else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type) || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)) {
                        dynaBean.set(key, value);
                    }
                    else {
                        dynaBean.set(key, toBean((JSONObject)value));
                    }
                }
                else if (type.isPrimitive()) {
                    JSONObject.log.warn("Tried to assign null value to " + key + ":" + type.getName());
                    dynaBean.set(key, JSONUtils.getMorpherRegistry().morph(type, null));
                }
                else {
                    dynaBean.set(key, null);
                }
            }
            catch (JSONException jsone) {
                throw jsone;
            }
            catch (Exception e) {
                throw new JSONException("Error while setting property=" + name + " type" + type, e);
            }
        }
        return dynaBean;
    }
    
    public static Object toBean(final JSONObject jsonObject, final Class beanClass) {
        final JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(beanClass);
        return toBean(jsonObject, jsonConfig);
    }
    
    public static Object toBean(final JSONObject jsonObject, final Class beanClass, final Map classMap) {
        final JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(beanClass);
        jsonConfig.setClassMap(classMap);
        return toBean(jsonObject, jsonConfig);
    }
    
    public static Object toBean(final JSONObject jsonObject, final JsonConfig jsonConfig) {
        if (jsonObject == null || jsonObject.isNullObject()) {
            return null;
        }
        final Class beanClass = jsonConfig.getRootClass();
        Map classMap = jsonConfig.getClassMap();
        if (beanClass == null) {
            return toBean(jsonObject);
        }
        if (classMap == null) {
            classMap = Collections.EMPTY_MAP;
        }
        Object bean = null;
        try {
            if (beanClass.isInterface()) {
                if (!Map.class.isAssignableFrom(beanClass)) {
                    throw new JSONException("beanClass is an interface. " + beanClass);
                }
                bean = new HashMap();
            }
            else {
                bean = jsonConfig.getNewBeanInstanceStrategy().newInstance(beanClass, jsonObject);
            }
        }
        catch (JSONException jsone) {
            throw jsone;
        }
        catch (Exception e) {
            throw new JSONException(e);
        }
        final Map props = JSONUtils.getProperties(jsonObject);
        final PropertyFilter javaPropertyFilter = jsonConfig.getJavaPropertyFilter();
        for (final String name : jsonObject.names(jsonConfig)) {
            final Class type = props.get(name);
            final Object value = jsonObject.get(name);
            if (javaPropertyFilter != null && javaPropertyFilter.apply(bean, name, value)) {
                continue;
            }
            String key = (Map.class.isAssignableFrom(beanClass) && jsonConfig.isSkipJavaIdentifierTransformationInMapKeys()) ? name : JSONUtils.convertToJavaIdentifier(name, jsonConfig);
            final PropertyNameProcessor propertyNameProcessor = jsonConfig.findJavaPropertyNameProcessor(beanClass);
            if (propertyNameProcessor != null) {
                key = propertyNameProcessor.processPropertyName(beanClass, key);
            }
            try {
                if (Map.class.isAssignableFrom(beanClass)) {
                    if (JSONUtils.isNull(value)) {
                        setProperty(bean, key, value, jsonConfig);
                    }
                    else if (value instanceof JSONArray) {
                        setProperty(bean, key, convertPropertyValueToCollection(key, value, jsonConfig, name, classMap, List.class), jsonConfig);
                    }
                    else if (String.class.isAssignableFrom(type) || JSONUtils.isBoolean(type) || JSONUtils.isNumber(type) || JSONUtils.isString(type) || JSONFunction.class.isAssignableFrom(type)) {
                        if (jsonConfig.isHandleJettisonEmptyElement() && "".equals(value)) {
                            setProperty(bean, key, null, jsonConfig);
                        }
                        else {
                            setProperty(bean, key, value, jsonConfig);
                        }
                    }
                    else {
                        final Class targetClass = resolveClass(classMap, key, name, type);
                        final JsonConfig jsc = jsonConfig.copy();
                        jsc.setRootClass(targetClass);
                        jsc.setClassMap(classMap);
                        if (targetClass != null) {
                            setProperty(bean, key, toBean((JSONObject)value, jsc), jsonConfig);
                        }
                        else {
                            setProperty(bean, key, toBean((JSONObject)value), jsonConfig);
                        }
                    }
                }
                else {
                    final PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, key);
                    if (pd != null && pd.getWriteMethod() == null) {
                        JSONObject.log.info("Property '" + key + "' of " + bean.getClass() + " has no write method. SKIPPED.");
                    }
                    else if (pd != null) {
                        Class targetType = pd.getPropertyType();
                        if (!JSONUtils.isNull(value)) {
                            if (value instanceof JSONArray) {
                                if (List.class.isAssignableFrom(pd.getPropertyType())) {
                                    setProperty(bean, key, convertPropertyValueToCollection(key, value, jsonConfig, name, classMap, pd.getPropertyType()), jsonConfig);
                                }
                                else if (Set.class.isAssignableFrom(pd.getPropertyType())) {
                                    setProperty(bean, key, convertPropertyValueToCollection(key, value, jsonConfig, name, classMap, pd.getPropertyType()), jsonConfig);
                                }
                                else {
                                    setProperty(bean, key, convertPropertyValueToArray(key, value, targetType, jsonConfig, classMap), jsonConfig);
                                }
                            }
                            else if (String.class.isAssignableFrom(type) || JSONUtils.isBoolean(type) || JSONUtils.isNumber(type) || JSONUtils.isString(type) || JSONFunction.class.isAssignableFrom(type)) {
                                if (pd != null) {
                                    if (jsonConfig.isHandleJettisonEmptyElement() && "".equals(value)) {
                                        setProperty(bean, key, null, jsonConfig);
                                    }
                                    else if (!targetType.isInstance(value)) {
                                        setProperty(bean, key, morphPropertyValue(key, value, type, targetType), jsonConfig);
                                    }
                                    else {
                                        setProperty(bean, key, value, jsonConfig);
                                    }
                                }
                                else if (beanClass == null || bean instanceof Map) {
                                    setProperty(bean, key, value, jsonConfig);
                                }
                                else {
                                    JSONObject.log.warn("Tried to assign property " + key + ":" + type.getName() + " to bean of class " + bean.getClass().getName());
                                }
                            }
                            else if (jsonConfig.isHandleJettisonSingleElementArray()) {
                                final JSONArray array = new JSONArray().element(value, jsonConfig);
                                final Class newTargetClass = resolveClass(classMap, key, name, type);
                                final JsonConfig jsc2 = jsonConfig.copy();
                                jsc2.setRootClass(newTargetClass);
                                jsc2.setClassMap(classMap);
                                if (targetType.isArray()) {
                                    setProperty(bean, key, JSONArray.toArray(array, jsc2), jsonConfig);
                                }
                                else if (JSONArray.class.isAssignableFrom(targetType)) {
                                    setProperty(bean, key, array, jsonConfig);
                                }
                                else if (List.class.isAssignableFrom(targetType) || Set.class.isAssignableFrom(targetType)) {
                                    jsc2.setCollectionType(targetType);
                                    setProperty(bean, key, JSONArray.toCollection(array, jsc2), jsonConfig);
                                }
                                else {
                                    setProperty(bean, key, toBean((JSONObject)value, jsc2), jsonConfig);
                                }
                            }
                            else {
                                if (targetType == Object.class || targetType.isInterface()) {
                                    final Class targetTypeCopy = targetType;
                                    targetType = findTargetClass(key, classMap);
                                    targetType = ((targetType == null) ? findTargetClass(name, classMap) : targetType);
                                    targetType = ((targetType == null && targetTypeCopy.isInterface()) ? targetTypeCopy : targetType);
                                }
                                final JsonConfig jsc3 = jsonConfig.copy();
                                jsc3.setRootClass(targetType);
                                jsc3.setClassMap(classMap);
                                setProperty(bean, key, toBean((JSONObject)value, jsc3), jsonConfig);
                            }
                        }
                        else if (type.isPrimitive()) {
                            JSONObject.log.warn("Tried to assign null value to " + key + ":" + type.getName());
                            setProperty(bean, key, JSONUtils.getMorpherRegistry().morph(type, null), jsonConfig);
                        }
                        else {
                            setProperty(bean, key, null, jsonConfig);
                        }
                    }
                    else if (!JSONUtils.isNull(value)) {
                        if (value instanceof JSONArray) {
                            setProperty(bean, key, convertPropertyValueToCollection(key, value, jsonConfig, name, classMap, List.class), jsonConfig);
                        }
                        else if (String.class.isAssignableFrom(type) || JSONUtils.isBoolean(type) || JSONUtils.isNumber(type) || JSONUtils.isString(type) || JSONFunction.class.isAssignableFrom(type)) {
                            if (beanClass == null || bean instanceof Map || jsonConfig.getPropertySetStrategy() != null || !jsonConfig.isIgnorePublicFields()) {
                                setProperty(bean, key, value, jsonConfig);
                            }
                            else {
                                JSONObject.log.warn("Tried to assign property " + key + ":" + type.getName() + " to bean of class " + bean.getClass().getName());
                            }
                        }
                        else if (jsonConfig.isHandleJettisonSingleElementArray()) {
                            final Class newTargetClass2 = resolveClass(classMap, key, name, type);
                            final JsonConfig jsc3 = jsonConfig.copy();
                            jsc3.setRootClass(newTargetClass2);
                            jsc3.setClassMap(classMap);
                            setProperty(bean, key, toBean((JSONObject)value, jsc3), jsonConfig);
                        }
                        else {
                            setProperty(bean, key, value, jsonConfig);
                        }
                    }
                    else if (type.isPrimitive()) {
                        JSONObject.log.warn("Tried to assign null value to " + key + ":" + type.getName());
                        setProperty(bean, key, JSONUtils.getMorpherRegistry().morph(type, null), jsonConfig);
                    }
                    else {
                        setProperty(bean, key, null, jsonConfig);
                    }
                }
            }
            catch (JSONException jsone2) {
                throw jsone2;
            }
            catch (Exception e2) {
                throw new JSONException("Error while setting property=" + name + " type " + type, e2);
            }
        }
        return bean;
    }
    
    public static Object toBean(final JSONObject jsonObject, final Object root, final JsonConfig jsonConfig) {
        if (jsonObject == null || jsonObject.isNullObject() || root == null) {
            return root;
        }
        final Class rootClass = root.getClass();
        if (rootClass.isInterface()) {
            throw new JSONException("Root bean is an interface. " + rootClass);
        }
        Map classMap = jsonConfig.getClassMap();
        if (classMap == null) {
            classMap = Collections.EMPTY_MAP;
        }
        final Map props = JSONUtils.getProperties(jsonObject);
        final PropertyFilter javaPropertyFilter = jsonConfig.getJavaPropertyFilter();
        for (final String name : jsonObject.names(jsonConfig)) {
            final Class type = props.get(name);
            final Object value = jsonObject.get(name);
            if (javaPropertyFilter != null && javaPropertyFilter.apply(root, name, value)) {
                continue;
            }
            final String key = JSONUtils.convertToJavaIdentifier(name, jsonConfig);
            try {
                final PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(root, key);
                if (pd != null && pd.getWriteMethod() == null) {
                    JSONObject.log.info("Property '" + key + "' of " + root.getClass() + " has no write method. SKIPPED.");
                }
                else if (!JSONUtils.isNull(value)) {
                    if (value instanceof JSONArray) {
                        if (pd == null || List.class.isAssignableFrom(pd.getPropertyType())) {
                            final Class targetClass = resolveClass(classMap, key, name, type);
                            final Object newRoot = jsonConfig.getNewBeanInstanceStrategy().newInstance(targetClass, null);
                            final List list = JSONArray.toList((JSONArray)value, newRoot, jsonConfig);
                            setProperty(root, key, list, jsonConfig);
                        }
                        else {
                            Class innerType = JSONUtils.getInnerComponentType(pd.getPropertyType());
                            final Class targetInnerType = findTargetClass(key, classMap);
                            if (innerType.equals(Object.class) && targetInnerType != null && !targetInnerType.equals(Object.class)) {
                                innerType = targetInnerType;
                            }
                            final Object newRoot2 = jsonConfig.getNewBeanInstanceStrategy().newInstance(innerType, null);
                            Object array = JSONArray.toArray((JSONArray)value, newRoot2, jsonConfig);
                            if (innerType.isPrimitive() || JSONUtils.isNumber(innerType) || Boolean.class.isAssignableFrom(innerType) || JSONUtils.isString(innerType)) {
                                array = JSONUtils.getMorpherRegistry().morph(Array.newInstance(innerType, 0).getClass(), array);
                            }
                            else if (!array.getClass().equals(pd.getPropertyType()) && !pd.getPropertyType().equals(Object.class)) {
                                final Morpher morpher = JSONUtils.getMorpherRegistry().getMorpherFor(Array.newInstance(innerType, 0).getClass());
                                if (IdentityObjectMorpher.getInstance().equals(morpher)) {
                                    final ObjectArrayMorpher beanMorpher = new ObjectArrayMorpher(new BeanMorpher(innerType, JSONUtils.getMorpherRegistry()));
                                    JSONUtils.getMorpherRegistry().registerMorpher(beanMorpher);
                                }
                                array = JSONUtils.getMorpherRegistry().morph(Array.newInstance(innerType, 0).getClass(), array);
                            }
                            setProperty(root, key, array, jsonConfig);
                        }
                    }
                    else if (String.class.isAssignableFrom(type) || JSONUtils.isBoolean(type) || JSONUtils.isNumber(type) || JSONUtils.isString(type) || JSONFunction.class.isAssignableFrom(type)) {
                        if (pd != null) {
                            if (jsonConfig.isHandleJettisonEmptyElement() && "".equals(value)) {
                                setProperty(root, key, null, jsonConfig);
                            }
                            else if (!pd.getPropertyType().isInstance(value)) {
                                final Morpher morpher2 = JSONUtils.getMorpherRegistry().getMorpherFor(pd.getPropertyType());
                                if (IdentityObjectMorpher.getInstance().equals(morpher2)) {
                                    JSONObject.log.warn("Can't transform property '" + key + "' from " + type.getName() + " into " + pd.getPropertyType().getName() + ". Will register a default BeanMorpher");
                                    JSONUtils.getMorpherRegistry().registerMorpher(new BeanMorpher(pd.getPropertyType(), JSONUtils.getMorpherRegistry()));
                                }
                                setProperty(root, key, JSONUtils.getMorpherRegistry().morph(pd.getPropertyType(), value), jsonConfig);
                            }
                            else {
                                setProperty(root, key, value, jsonConfig);
                            }
                        }
                        else if (root instanceof Map) {
                            setProperty(root, key, value, jsonConfig);
                        }
                        else {
                            JSONObject.log.warn("Tried to assign property " + key + ":" + type.getName() + " to bean of class " + root.getClass().getName());
                        }
                    }
                    else if (pd != null) {
                        Class targetClass = pd.getPropertyType();
                        if (jsonConfig.isHandleJettisonSingleElementArray()) {
                            final JSONArray array2 = new JSONArray().element(value, jsonConfig);
                            final Class newTargetClass = resolveClass(classMap, key, name, type);
                            final Object newRoot3 = jsonConfig.getNewBeanInstanceStrategy().newInstance(newTargetClass, (JSONObject)value);
                            if (targetClass.isArray()) {
                                setProperty(root, key, JSONArray.toArray(array2, newRoot3, jsonConfig), jsonConfig);
                            }
                            else if (Collection.class.isAssignableFrom(targetClass)) {
                                setProperty(root, key, JSONArray.toList(array2, newRoot3, jsonConfig), jsonConfig);
                            }
                            else if (JSONArray.class.isAssignableFrom(targetClass)) {
                                setProperty(root, key, array2, jsonConfig);
                            }
                            else {
                                setProperty(root, key, toBean((JSONObject)value, newRoot3, jsonConfig), jsonConfig);
                            }
                        }
                        else {
                            if (targetClass == Object.class) {
                                targetClass = resolveClass(classMap, key, name, type);
                                if (targetClass == null) {
                                    targetClass = Object.class;
                                }
                            }
                            final Object newRoot = jsonConfig.getNewBeanInstanceStrategy().newInstance(targetClass, (JSONObject)value);
                            setProperty(root, key, toBean((JSONObject)value, newRoot, jsonConfig), jsonConfig);
                        }
                    }
                    else if (root instanceof Map) {
                        Class targetClass = findTargetClass(key, classMap);
                        targetClass = ((targetClass == null) ? findTargetClass(name, classMap) : targetClass);
                        final Object newRoot = jsonConfig.getNewBeanInstanceStrategy().newInstance(targetClass, null);
                        setProperty(root, key, toBean((JSONObject)value, newRoot, jsonConfig), jsonConfig);
                    }
                    else {
                        JSONObject.log.warn("Tried to assign property " + key + ":" + type.getName() + " to bean of class " + rootClass.getName());
                    }
                }
                else if (type.isPrimitive()) {
                    JSONObject.log.warn("Tried to assign null value to " + key + ":" + type.getName());
                    setProperty(root, key, JSONUtils.getMorpherRegistry().morph(type, null), jsonConfig);
                }
                else {
                    setProperty(root, key, null, jsonConfig);
                }
            }
            catch (JSONException jsone) {
                throw jsone;
            }
            catch (Exception e) {
                throw new JSONException("Error while setting property=" + name + " type " + type, e);
            }
        }
        return root;
    }
    
    private static JSONObject _fromBean(final Object bean, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(bean)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(bean);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(bean);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(bean);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireObjectStartEvent(jsonConfig);
        final JsonBeanProcessor processor = jsonConfig.findJsonBeanProcessor(bean.getClass());
        if (processor != null) {
            JSONObject json = null;
            try {
                json = processor.processBean(bean, jsonConfig);
                if (json == null) {
                    json = (JSONObject)jsonConfig.findDefaultValueProcessor(bean.getClass()).getDefaultValue(bean.getClass());
                    if (json == null) {
                        json = new JSONObject(true);
                    }
                }
                AbstractJSON.removeInstance(bean);
                AbstractJSON.fireObjectEndEvent(jsonConfig);
            }
            catch (JSONException jsone3) {
                AbstractJSON.removeInstance(bean);
                AbstractJSON.fireErrorEvent(jsone3, jsonConfig);
                throw jsone3;
            }
            catch (RuntimeException e2) {
                AbstractJSON.removeInstance(bean);
                final JSONException jsone4 = new JSONException(e2);
                AbstractJSON.fireErrorEvent(jsone4, jsonConfig);
                throw jsone4;
            }
            return json;
        }
        final JSONObject jsonObject = defaultBeanProcessing(bean, jsonConfig);
        AbstractJSON.removeInstance(bean);
        AbstractJSON.fireObjectEndEvent(jsonConfig);
        return jsonObject;
    }
    
    private static JSONObject defaultBeanProcessing(final Object bean, final JsonConfig jsonConfig) {
        final Class beanClass = bean.getClass();
        final PropertyNameProcessor propertyNameProcessor = jsonConfig.findJsonPropertyNameProcessor(beanClass);
        final Collection exclusions = jsonConfig.getMergedExcludes(beanClass);
        final JSONObject jsonObject = new JSONObject();
        try {
            final PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean);
            final PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
            for (int i = 0; i < pds.length; ++i) {
                boolean bypass = false;
                String key = pds[i].getName();
                if (!exclusions.contains(key)) {
                    if (!jsonConfig.isIgnoreTransientFields() || !isTransientField(key, beanClass, jsonConfig)) {
                        final Class type = pds[i].getPropertyType();
                        try {
                            pds[i].getReadMethod();
                        }
                        catch (Exception e3) {
                            final String warning = "Property '" + key + "' of " + beanClass + " has no read method. SKIPPED";
                            AbstractJSON.fireWarnEvent(warning, jsonConfig);
                            JSONObject.log.info(warning);
                            continue;
                        }
                        if (pds[i].getReadMethod() != null) {
                            if (!isTransient(pds[i].getReadMethod(), jsonConfig)) {
                                Object value = PropertyUtils.getProperty(bean, key);
                                if (jsonPropertyFilter == null || !jsonPropertyFilter.apply(bean, key, value)) {
                                    final JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(beanClass, type, key);
                                    if (jsonValueProcessor != null) {
                                        value = jsonValueProcessor.processObjectValue(key, value, jsonConfig);
                                        bypass = true;
                                        if (!JsonVerifier.isValidJsonValue(value)) {
                                            throw new JSONException("Value is not a valid JSON value. " + value);
                                        }
                                    }
                                    if (propertyNameProcessor != null) {
                                        key = propertyNameProcessor.processPropertyName(beanClass, key);
                                    }
                                    setValue(jsonObject, key, value, type, jsonConfig, bypass);
                                }
                            }
                        }
                        else {
                            final String warning2 = "Property '" + key + "' of " + beanClass + " has no read method. SKIPPED";
                            AbstractJSON.fireWarnEvent(warning2, jsonConfig);
                            JSONObject.log.info(warning2);
                        }
                    }
                }
            }
            try {
                if (!jsonConfig.isIgnorePublicFields()) {
                    final Field[] fields = beanClass.getFields();
                    for (int j = 0; j < fields.length; ++j) {
                        boolean bypass2 = false;
                        final Field field = fields[j];
                        String key2 = field.getName();
                        if (!exclusions.contains(key2)) {
                            if (!jsonConfig.isIgnoreTransientFields() || !isTransient(field, jsonConfig)) {
                                final Class type2 = field.getType();
                                Object value2 = field.get(bean);
                                if (jsonPropertyFilter == null || !jsonPropertyFilter.apply(bean, key2, value2)) {
                                    final JsonValueProcessor jsonValueProcessor2 = jsonConfig.findJsonValueProcessor(beanClass, type2, key2);
                                    if (jsonValueProcessor2 != null) {
                                        value2 = jsonValueProcessor2.processObjectValue(key2, value2, jsonConfig);
                                        bypass2 = true;
                                        if (!JsonVerifier.isValidJsonValue(value2)) {
                                            throw new JSONException("Value is not a valid JSON value. " + value2);
                                        }
                                    }
                                    if (propertyNameProcessor != null) {
                                        key2 = propertyNameProcessor.processPropertyName(beanClass, key2);
                                    }
                                    setValue(jsonObject, key2, value2, type2, jsonConfig, bypass2);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                JSONObject.log.trace("Couldn't read public fields.", e);
            }
        }
        catch (JSONException jsone) {
            AbstractJSON.removeInstance(bean);
            AbstractJSON.fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }
        catch (Exception e2) {
            AbstractJSON.removeInstance(bean);
            final JSONException jsone2 = new JSONException(e2);
            AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
            throw jsone2;
        }
        return jsonObject;
    }
    
    private static JSONObject _fromDynaBean(final DynaBean bean, final JsonConfig jsonConfig) {
        if (bean == null) {
            AbstractJSON.fireObjectStartEvent(jsonConfig);
            AbstractJSON.fireObjectEndEvent(jsonConfig);
            return new JSONObject(true);
        }
        JSONException jsone2 = null;
        if (!AbstractJSON.addInstance(bean)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(bean);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(bean);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(bean);
                jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireObjectStartEvent(jsonConfig);
        final JSONObject jsonObject = new JSONObject();
        try {
            final DynaProperty[] props = bean.getDynaClass().getDynaProperties();
            final Collection exclusions = jsonConfig.getMergedExcludes();
            final PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
            for (int i = 0; i < props.length; ++i) {
                boolean bypass = false;
                final DynaProperty dynaProperty = props[i];
                final String key = dynaProperty.getName();
                if (!exclusions.contains(key)) {
                    final Class type = dynaProperty.getType();
                    Object value = bean.get(dynaProperty.getName());
                    if (jsonPropertyFilter == null || !jsonPropertyFilter.apply(bean, key, value)) {
                        final JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(type, key);
                        if (jsonValueProcessor != null) {
                            value = jsonValueProcessor.processObjectValue(key, value, jsonConfig);
                            bypass = true;
                            if (!JsonVerifier.isValidJsonValue(value)) {
                                throw new JSONException("Value is not a valid JSON value. " + value);
                            }
                        }
                        setValue(jsonObject, key, value, type, jsonConfig, bypass);
                    }
                }
            }
        }
        catch (JSONException jsone2) {
            AbstractJSON.removeInstance(bean);
            AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
            throw jsone2;
        }
        catch (RuntimeException e2) {
            AbstractJSON.removeInstance(bean);
            final JSONException jsone3 = new JSONException(e2);
            AbstractJSON.fireErrorEvent(jsone3, jsonConfig);
            throw jsone3;
        }
        AbstractJSON.removeInstance(bean);
        AbstractJSON.fireObjectEndEvent(jsonConfig);
        return jsonObject;
    }
    
    private static JSONObject _fromJSONObject(final JSONObject object, final JsonConfig jsonConfig) {
        if (object == null || object.isNullObject()) {
            AbstractJSON.fireObjectStartEvent(jsonConfig);
            AbstractJSON.fireObjectEndEvent(jsonConfig);
            return new JSONObject(true);
        }
        if (!AbstractJSON.addInstance(object)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(object);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(object);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(object);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireObjectStartEvent(jsonConfig);
        final JSONArray sa = object.names(jsonConfig);
        final Collection exclusions = jsonConfig.getMergedExcludes();
        final JSONObject jsonObject = new JSONObject();
        final PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
        for (final Object k : sa) {
            if (k == null) {
                throw new JSONException("JSON keys cannot be null.");
            }
            if (!(k instanceof String) && !jsonConfig.isAllowNonStringKeys()) {
                throw new ClassCastException("JSON keys must be strings.");
            }
            final String key = String.valueOf(k);
            if ("null".equals(key)) {
                throw new NullPointerException("JSON keys must not be null nor the 'null' string.");
            }
            if (exclusions.contains(key)) {
                continue;
            }
            final Object value = object.opt(key);
            if (jsonPropertyFilter != null && jsonPropertyFilter.apply(object, key, value)) {
                continue;
            }
            if (jsonObject.properties.containsKey(key)) {
                jsonObject.accumulate(key, value, jsonConfig);
                AbstractJSON.firePropertySetEvent(key, value, true, jsonConfig);
            }
            else {
                jsonObject.setInternal(key, value, jsonConfig);
                AbstractJSON.firePropertySetEvent(key, value, false, jsonConfig);
            }
        }
        AbstractJSON.removeInstance(object);
        AbstractJSON.fireObjectEndEvent(jsonConfig);
        return jsonObject;
    }
    
    private static JSONObject _fromJSONString(final JSONString string, final JsonConfig jsonConfig) {
        return _fromJSONTokener(new JSONTokener(string.toJSONString()), jsonConfig);
    }
    
    private static JSONObject _fromJSONTokener(final JSONTokener tokener, final JsonConfig jsonConfig) {
        try {
            if (tokener.matches("null.*")) {
                AbstractJSON.fireObjectStartEvent(jsonConfig);
                AbstractJSON.fireObjectEndEvent(jsonConfig);
                return new JSONObject(true);
            }
            if (tokener.nextClean() != '{') {
                throw tokener.syntaxError("A JSONObject text must begin with '{'");
            }
            AbstractJSON.fireObjectStartEvent(jsonConfig);
            final Collection exclusions = jsonConfig.getMergedExcludes();
            final PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
            final JSONObject jsonObject = new JSONObject();
            while (true) {
                char c = tokener.nextClean();
                switch (c) {
                    case '\0': {
                        throw tokener.syntaxError("A JSONObject text must end with '}'");
                    }
                    case '}': {
                        AbstractJSON.fireObjectEndEvent(jsonConfig);
                        return jsonObject;
                    }
                    default: {
                        tokener.back();
                        final String key = tokener.nextValue(jsonConfig).toString();
                        c = tokener.nextClean();
                        if (c == '=') {
                            if (tokener.next() != '>') {
                                tokener.back();
                            }
                        }
                        else if (c != ':') {
                            throw tokener.syntaxError("Expected a ':' after a key");
                        }
                        final char peek = tokener.peek();
                        final boolean quoted = peek == '\"' || peek == '\'';
                        Object v = tokener.nextValue(jsonConfig);
                        if (quoted || !JSONUtils.isFunctionHeader(v)) {
                            if (exclusions.contains(key)) {
                                switch (tokener.nextClean()) {
                                    case ',':
                                    case ';': {
                                        if (tokener.nextClean() == '}') {
                                            AbstractJSON.fireObjectEndEvent(jsonConfig);
                                            return jsonObject;
                                        }
                                        tokener.back();
                                        continue;
                                    }
                                    case '}': {
                                        AbstractJSON.fireObjectEndEvent(jsonConfig);
                                        return jsonObject;
                                    }
                                    default: {
                                        throw tokener.syntaxError("Expected a ',' or '}'");
                                    }
                                }
                            }
                            else if (jsonPropertyFilter == null || !jsonPropertyFilter.apply(tokener, key, v)) {
                                if (quoted && v instanceof String && (JSONUtils.mayBeJSON((String)v) || JSONUtils.isFunction(v))) {
                                    v = "\"" + v + "\"";
                                }
                                if (jsonObject.properties.containsKey(key)) {
                                    jsonObject.accumulate(key, v, jsonConfig);
                                    AbstractJSON.firePropertySetEvent(key, v, true, jsonConfig);
                                }
                                else {
                                    jsonObject.element(key, v, jsonConfig);
                                    AbstractJSON.firePropertySetEvent(key, v, false, jsonConfig);
                                }
                            }
                        }
                        else {
                            final String params = JSONUtils.getFunctionParams((String)v);
                            int i = 0;
                            final StringBuffer sb = new StringBuffer();
                            do {
                                final char ch = tokener.next();
                                if (ch == '\0') {
                                    break;
                                }
                                if (ch == '{') {
                                    ++i;
                                }
                                if (ch == '}') {
                                    --i;
                                }
                                sb.append(ch);
                            } while (i != 0);
                            if (i != 0) {
                                throw tokener.syntaxError("Unbalanced '{' or '}' on prop: " + v);
                            }
                            String text = sb.toString();
                            text = text.substring(1, text.length() - 1).trim();
                            final Object value = new JSONFunction((String[])((params != null) ? StringUtils.split(params, ",") : null), text);
                            if (jsonPropertyFilter == null || !jsonPropertyFilter.apply(tokener, key, value)) {
                                if (jsonObject.properties.containsKey(key)) {
                                    jsonObject.accumulate(key, value, jsonConfig);
                                    AbstractJSON.firePropertySetEvent(key, value, true, jsonConfig);
                                }
                                else {
                                    jsonObject.element(key, value, jsonConfig);
                                    AbstractJSON.firePropertySetEvent(key, value, false, jsonConfig);
                                }
                            }
                        }
                        switch (tokener.nextClean()) {
                            case ',':
                            case ';': {
                                if (tokener.nextClean() == '}') {
                                    AbstractJSON.fireObjectEndEvent(jsonConfig);
                                    return jsonObject;
                                }
                                tokener.back();
                                continue;
                            }
                            case '}': {
                                AbstractJSON.fireObjectEndEvent(jsonConfig);
                                return jsonObject;
                            }
                            default: {
                                throw tokener.syntaxError("Expected a ',' or '}'");
                            }
                        }
                        break;
                    }
                }
            }
        }
        catch (JSONException jsone) {
            AbstractJSON.fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }
    }
    
    private static JSONObject _fromMap(final Map map, final JsonConfig jsonConfig) {
        if (map == null) {
            AbstractJSON.fireObjectStartEvent(jsonConfig);
            AbstractJSON.fireObjectEndEvent(jsonConfig);
            return new JSONObject(true);
        }
        if (!AbstractJSON.addInstance(map)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(map);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(map);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(map);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireObjectStartEvent(jsonConfig);
        final Collection exclusions = jsonConfig.getMergedExcludes();
        final JSONObject jsonObject = new JSONObject();
        final PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
        try {
            final Iterator entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                boolean bypass = false;
                final Entry entry = entries.next();
                final Object k = entry.getKey();
                if (k == null) {
                    throw new JSONException("JSON keys cannot be null.");
                }
                if (!(k instanceof String) && !jsonConfig.isAllowNonStringKeys()) {
                    throw new ClassCastException("JSON keys must be strings.");
                }
                final String key = String.valueOf(k);
                if ("null".equals(key)) {
                    throw new NullPointerException("JSON keys must not be null nor the 'null' string.");
                }
                if (exclusions.contains(key)) {
                    continue;
                }
                Object value = entry.getValue();
                if (jsonPropertyFilter != null && jsonPropertyFilter.apply(map, key, value)) {
                    continue;
                }
                if (value != null) {
                    final JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(value.getClass(), key);
                    if (jsonValueProcessor != null) {
                        value = jsonValueProcessor.processObjectValue(key, value, jsonConfig);
                        bypass = true;
                        if (!JsonVerifier.isValidJsonValue(value)) {
                            throw new JSONException("Value is not a valid JSON value. " + value);
                        }
                    }
                    setValue(jsonObject, key, value, value.getClass(), jsonConfig, bypass);
                }
                else if (jsonObject.properties.containsKey(key)) {
                    jsonObject.accumulate(key, JSONNull.getInstance());
                    AbstractJSON.firePropertySetEvent(key, JSONNull.getInstance(), true, jsonConfig);
                }
                else {
                    jsonObject.element(key, JSONNull.getInstance());
                    AbstractJSON.firePropertySetEvent(key, JSONNull.getInstance(), false, jsonConfig);
                }
            }
        }
        catch (JSONException jsone3) {
            AbstractJSON.removeInstance(map);
            AbstractJSON.fireErrorEvent(jsone3, jsonConfig);
            throw jsone3;
        }
        catch (RuntimeException e2) {
            AbstractJSON.removeInstance(map);
            final JSONException jsone4 = new JSONException(e2);
            AbstractJSON.fireErrorEvent(jsone4, jsonConfig);
            throw jsone4;
        }
        AbstractJSON.removeInstance(map);
        AbstractJSON.fireObjectEndEvent(jsonConfig);
        return jsonObject;
    }
    
    private static JSONObject _fromString(final String str, final JsonConfig jsonConfig) {
        if (str == null || "null".equals(str)) {
            AbstractJSON.fireObjectStartEvent(jsonConfig);
            AbstractJSON.fireObjectEndEvent(jsonConfig);
            return new JSONObject(true);
        }
        return _fromJSONTokener(new JSONTokener(str), jsonConfig);
    }
    
    private static Object convertPropertyValueToArray(final String key, final Object value, final Class targetType, final JsonConfig jsonConfig, final Map classMap) {
        Class innerType = JSONUtils.getInnerComponentType(targetType);
        final Class targetInnerType = findTargetClass(key, classMap);
        if (innerType.equals(Object.class) && targetInnerType != null && !targetInnerType.equals(Object.class)) {
            innerType = targetInnerType;
        }
        final JsonConfig jsc = jsonConfig.copy();
        jsc.setRootClass(innerType);
        jsc.setClassMap(classMap);
        Object array = JSONArray.toArray((JSONArray)value, jsc);
        if (innerType.isPrimitive() || JSONUtils.isNumber(innerType) || Boolean.class.isAssignableFrom(innerType) || JSONUtils.isString(innerType)) {
            array = JSONUtils.getMorpherRegistry().morph(Array.newInstance(innerType, 0).getClass(), array);
        }
        else if (!array.getClass().equals(targetType) && !targetType.equals(Object.class)) {
            final Morpher morpher = JSONUtils.getMorpherRegistry().getMorpherFor(Array.newInstance(innerType, 0).getClass());
            if (IdentityObjectMorpher.getInstance().equals(morpher)) {
                final ObjectArrayMorpher beanMorpher = new ObjectArrayMorpher(new BeanMorpher(innerType, JSONUtils.getMorpherRegistry()));
                JSONUtils.getMorpherRegistry().registerMorpher(beanMorpher);
            }
            array = JSONUtils.getMorpherRegistry().morph(Array.newInstance(innerType, 0).getClass(), array);
        }
        return array;
    }
    
    private static List convertPropertyValueToList(final String key, final Object value, final JsonConfig jsonConfig, final String name, final Map classMap) {
        Class targetClass = findTargetClass(key, classMap);
        targetClass = ((targetClass == null) ? findTargetClass(name, classMap) : targetClass);
        final JsonConfig jsc = jsonConfig.copy();
        jsc.setRootClass(targetClass);
        jsc.setClassMap(classMap);
        final List list = (List)JSONArray.toCollection((JSONArray)value, jsc);
        return list;
    }
    
    private static Collection convertPropertyValueToCollection(final String key, final Object value, final JsonConfig jsonConfig, final String name, final Map classMap, final Class collectionType) {
        Class targetClass = findTargetClass(key, classMap);
        targetClass = ((targetClass == null) ? findTargetClass(name, classMap) : targetClass);
        final JsonConfig jsc = jsonConfig.copy();
        jsc.setRootClass(targetClass);
        jsc.setClassMap(classMap);
        jsc.setCollectionType(collectionType);
        return JSONArray.toCollection((JSONArray)value, jsc);
    }
    
    private static Class resolveClass(final Map classMap, final String key, final String name, final Class type) {
        Class targetClass = findTargetClass(key, classMap);
        if (targetClass == null) {
            targetClass = findTargetClass(name, classMap);
        }
        if (targetClass == null && type != null) {
            if (List.class.equals(type)) {
                targetClass = ArrayList.class;
            }
            else if (Map.class.equals(type)) {
                targetClass = LinkedHashMap.class;
            }
            else if (Set.class.equals(type)) {
                targetClass = LinkedHashSet.class;
            }
            else if (!type.isInterface() && !Object.class.equals(type)) {
                targetClass = type;
            }
        }
        return targetClass;
    }
    
    private static Class findTargetClass(final String key, final Map classMap) {
        Class targetClass = classMap.get(key);
        if (targetClass == null) {
            for (final Entry entry : classMap.entrySet()) {
                if (RegexpUtils.getMatcher(entry.getKey()).matches(key)) {
                    targetClass = entry.getValue();
                    break;
                }
            }
        }
        return targetClass;
    }
    
    private static boolean isTransientField(final String name, final Class beanClass, final JsonConfig jsonConfig) {
        try {
            final Field field = beanClass.getDeclaredField(name);
            return (field.getModifiers() & 0x80) == 0x80 || isTransient(field, jsonConfig);
        }
        catch (Exception e) {
            JSONObject.log.info("Error while inspecting field " + beanClass + "." + name + " for transient status.", e);
            return false;
        }
    }
    
    private static boolean isTransient(final AnnotatedElement element, final JsonConfig jsonConfig) {
        final Iterator annotations = jsonConfig.getIgnoreFieldAnnotations().iterator();
        while (annotations.hasNext()) {
            try {
                final String annotationClassName = annotations.next();
                if (element.getAnnotation(Class.forName(annotationClassName)) != null) {
                    return true;
                }
                continue;
            }
            catch (Exception e) {
                JSONObject.log.info("Error while inspecting " + element + " for transient status.", e);
            }
        }
        return false;
    }
    
    private static Object morphPropertyValue(final String key, Object value, final Class type, final Class targetType) {
        final Morpher morpher = JSONUtils.getMorpherRegistry().getMorpherFor(targetType);
        if (IdentityObjectMorpher.getInstance().equals(morpher)) {
            JSONObject.log.warn("Can't transform property '" + key + "' from " + type.getName() + " into " + targetType.getName() + ". Will register a default Morpher");
            if (Enum.class.isAssignableFrom(targetType)) {
                JSONUtils.getMorpherRegistry().registerMorpher(new EnumMorpher(targetType));
            }
            else {
                JSONUtils.getMorpherRegistry().registerMorpher(new BeanMorpher(targetType, JSONUtils.getMorpherRegistry()));
            }
        }
        value = JSONUtils.getMorpherRegistry().morph(targetType, value);
        return value;
    }
    
    private static void setProperty(final Object bean, final String key, final Object value, final JsonConfig jsonConfig) throws Exception {
        final PropertySetStrategy propertySetStrategy = (jsonConfig.getPropertySetStrategy() != null) ? jsonConfig.getPropertySetStrategy() : PropertySetStrategy.DEFAULT;
        propertySetStrategy.setProperty(bean, key, value, jsonConfig);
    }
    
    private static void setValue(final JSONObject jsonObject, final String key, Object value, final Class type, final JsonConfig jsonConfig, final boolean bypass) {
        boolean accumulated = false;
        if (value == null) {
            value = jsonConfig.findDefaultValueProcessor(type).getDefaultValue(type);
            if (!JsonVerifier.isValidJsonValue(value)) {
                throw new JSONException("Value is not a valid JSON value. " + value);
            }
        }
        if (jsonObject.properties.containsKey(key)) {
            if (String.class.isAssignableFrom(type)) {
                final Object o = jsonObject.opt(key);
                if (o instanceof JSONArray) {
                    ((JSONArray)o).addString((String)value);
                }
                else {
                    jsonObject.properties.put(key, new JSONArray().element(o).addString((String)value));
                }
            }
            else {
                jsonObject.accumulate(key, value, jsonConfig);
            }
            accumulated = true;
        }
        else if (bypass || String.class.isAssignableFrom(type)) {
            jsonObject.properties.put(key, value);
        }
        else {
            jsonObject.setInternal(key, value, jsonConfig);
        }
        value = jsonObject.opt(key);
        if (accumulated) {
            final JSONArray array = (JSONArray)value;
            value = array.get(array.size() - 1);
        }
        AbstractJSON.firePropertySetEvent(key, value, accumulated, jsonConfig);
    }
    
    public JSONObject() {
        this.properties = new ListOrderedMap();
    }
    
    public JSONObject(final boolean isNull) {
        this();
        this.nullObject = isNull;
    }
    
    public JSONObject accumulate(final String key, final boolean value) {
        return this._accumulate(key, value ? Boolean.TRUE : Boolean.FALSE, new JsonConfig());
    }
    
    public JSONObject accumulate(final String key, final double value) {
        return this._accumulate(key, value, new JsonConfig());
    }
    
    public JSONObject accumulate(final String key, final int value) {
        return this._accumulate(key, value, new JsonConfig());
    }
    
    public JSONObject accumulate(final String key, final long value) {
        return this._accumulate(key, value, new JsonConfig());
    }
    
    public JSONObject accumulate(final String key, final Object value) {
        return this._accumulate(key, value, new JsonConfig());
    }
    
    public JSONObject accumulate(final String key, final Object value, final JsonConfig jsonConfig) {
        return this._accumulate(key, value, jsonConfig);
    }
    
    public void accumulateAll(final Map map) {
        this.accumulateAll(map, new JsonConfig());
    }
    
    public void accumulateAll(final Map map, final JsonConfig jsonConfig) {
        if (map instanceof JSONObject) {
            for (final Entry entry : map.entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                this.accumulate(key, value, jsonConfig);
            }
        }
        else {
            for (final Entry entry : map.entrySet()) {
                final String key = String.valueOf(entry.getKey());
                final Object value = entry.getValue();
                this.accumulate(key, value, jsonConfig);
            }
        }
    }
    
    public void clear() {
        this.properties.clear();
    }
    
    public int compareTo(final Object obj) {
        if (obj != null && obj instanceof JSONObject) {
            final JSONObject other = (JSONObject)obj;
            final int size1 = this.size();
            final int size2 = other.size();
            if (size1 < size2) {
                return -1;
            }
            if (size1 > size2) {
                return 1;
            }
            if (this.equals(other)) {
                return 0;
            }
        }
        return -1;
    }
    
    public boolean containsKey(final Object key) {
        return this.properties.containsKey(key);
    }
    
    public boolean containsValue(final Object value) {
        return this.containsValue(value, new JsonConfig());
    }
    
    public boolean containsValue(Object value, final JsonConfig jsonConfig) {
        try {
            value = this.processValue(value, jsonConfig);
        }
        catch (JSONException e) {
            return false;
        }
        return this.properties.containsValue(value);
    }
    
    public JSONObject discard(final String key) {
        this.verifyIsNull();
        this.properties.remove(key);
        return this;
    }
    
    public JSONObject element(final String key, final boolean value) {
        this.verifyIsNull();
        return this.element(key, value ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONObject element(final String key, final Collection value) {
        return this.element(key, value, new JsonConfig());
    }
    
    public JSONObject element(final String key, Collection value, final JsonConfig jsonConfig) {
        if (!(value instanceof JSONArray)) {
            value = JSONArray.fromObject(value, jsonConfig);
        }
        return this.setInternal(key, value, jsonConfig);
    }
    
    public JSONObject element(final String key, final double value) {
        this.verifyIsNull();
        final Double d = new Double(value);
        JSONUtils.testValidity(d);
        return this.element(key, d);
    }
    
    public JSONObject element(final String key, final int value) {
        this.verifyIsNull();
        return this.element(key, new Integer(value));
    }
    
    public JSONObject element(final String key, final long value) {
        this.verifyIsNull();
        return this.element(key, new Long(value));
    }
    
    public JSONObject element(final String key, final Map value) {
        return this.element(key, value, new JsonConfig());
    }
    
    public JSONObject element(final String key, final Map value, final JsonConfig jsonConfig) {
        this.verifyIsNull();
        if (value instanceof JSONObject) {
            return this.setInternal(key, value, jsonConfig);
        }
        return this.element(key, fromObject(value, jsonConfig), jsonConfig);
    }
    
    public JSONObject element(final String key, final Object value) {
        return this.element(key, value, new JsonConfig());
    }
    
    public JSONObject element(final String key, Object value, final JsonConfig jsonConfig) {
        this.verifyIsNull();
        if (key == null) {
            throw new JSONException("Null key.");
        }
        if (value != null) {
            value = this.processValue(key, value, jsonConfig);
            this._setInternal(key, value, jsonConfig);
        }
        else {
            this.remove(key);
        }
        return this;
    }
    
    public JSONObject elementOpt(final String key, final Object value) {
        return this.elementOpt(key, value, new JsonConfig());
    }
    
    public JSONObject elementOpt(final String key, final Object value, final JsonConfig jsonConfig) {
        this.verifyIsNull();
        if (key != null && value != null) {
            this.element(key, value, jsonConfig);
        }
        return this;
    }
    
    public Set entrySet() {
        return Collections.unmodifiableSet((Set<?>)this.properties.entrySet());
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof JSONObject)) {
            return false;
        }
        final JSONObject other = (JSONObject)obj;
        if (this.isNullObject()) {
            return other.isNullObject();
        }
        if (other.isNullObject()) {
            return false;
        }
        if (other.size() != this.size()) {
            return false;
        }
        for (final String key : this.properties.keySet()) {
            if (!other.properties.containsKey(key)) {
                return false;
            }
            final Object o1 = this.properties.get(key);
            final Object o2 = other.properties.get(key);
            if (JSONNull.getInstance().equals(o1)) {
                if (JSONNull.getInstance().equals(o2)) {
                    continue;
                }
                return false;
            }
            else {
                if (JSONNull.getInstance().equals(o2)) {
                    return false;
                }
                if (o1 instanceof String && o2 instanceof JSONFunction) {
                    if (!o1.equals(String.valueOf(o2))) {
                        return false;
                    }
                    continue;
                }
                else if (o1 instanceof JSONFunction && o2 instanceof String) {
                    if (!o2.equals(String.valueOf(o1))) {
                        return false;
                    }
                    continue;
                }
                else if (o1 instanceof JSONObject && o2 instanceof JSONObject) {
                    if (!o1.equals(o2)) {
                        return false;
                    }
                    continue;
                }
                else if (o1 instanceof JSONArray && o2 instanceof JSONArray) {
                    if (!o1.equals(o2)) {
                        return false;
                    }
                    continue;
                }
                else if (o1 instanceof JSONFunction && o2 instanceof JSONFunction) {
                    if (!o1.equals(o2)) {
                        return false;
                    }
                    continue;
                }
                else if (o1 instanceof String) {
                    if (!o1.equals(String.valueOf(o2))) {
                        return false;
                    }
                    continue;
                }
                else if (o2 instanceof String) {
                    if (!o2.equals(String.valueOf(o1))) {
                        return false;
                    }
                    continue;
                }
                else {
                    final Morpher m1 = JSONUtils.getMorpherRegistry().getMorpherFor(o1.getClass());
                    final Morpher m2 = JSONUtils.getMorpherRegistry().getMorpherFor(o2.getClass());
                    if (m1 != null && m1 != IdentityObjectMorpher.getInstance()) {
                        if (!o1.equals(JSONUtils.getMorpherRegistry().morph(o1.getClass(), o2))) {
                            return false;
                        }
                        continue;
                    }
                    else if (m2 != null && m2 != IdentityObjectMorpher.getInstance()) {
                        if (!JSONUtils.getMorpherRegistry().morph(o1.getClass(), o1).equals(o2)) {
                            return false;
                        }
                        continue;
                    }
                    else {
                        if (!o1.equals(o2)) {
                            return false;
                        }
                        continue;
                    }
                }
            }
        }
        return true;
    }
    
    public Object get(final Object key) {
        if (key instanceof String) {
            return this.get((String)key);
        }
        return null;
    }
    
    public Object get(final String key) {
        this.verifyIsNull();
        return this.properties.get(key);
    }
    
    public boolean getBoolean(final String key) {
        this.verifyIsNull();
        final Object o = this.get(key);
        if (o != null) {
            if (o.equals(Boolean.FALSE) || (o instanceof String && ((String)o).equalsIgnoreCase("false"))) {
                return false;
            }
            if (o.equals(Boolean.TRUE) || (o instanceof String && ((String)o).equalsIgnoreCase("true"))) {
                return true;
            }
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a Boolean.");
    }
    
    public double getDouble(final String key) {
        this.verifyIsNull();
        final Object o = this.get(key);
        if (o != null) {
            try {
                return (o instanceof Number) ? ((Number)o).doubleValue() : Double.parseDouble((String)o);
            }
            catch (Exception e) {
                throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a number.");
            }
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a number.");
    }
    
    public int getInt(final String key) {
        this.verifyIsNull();
        final Object o = this.get(key);
        if (o != null) {
            return (o instanceof Number) ? ((Number)o).intValue() : ((int)this.getDouble(key));
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a number.");
    }
    
    public JSONArray getJSONArray(final String key) {
        this.verifyIsNull();
        final Object o = this.get(key);
        if (o != null && o instanceof JSONArray) {
            return (JSONArray)o;
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final String key) {
        this.verifyIsNull();
        final Object o = this.get(key);
        if (JSONNull.getInstance().equals(o)) {
            return new JSONObject(true);
        }
        if (o instanceof JSONObject) {
            return (JSONObject)o;
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a JSONObject.");
    }
    
    public long getLong(final String key) {
        this.verifyIsNull();
        final Object o = this.get(key);
        if (o != null) {
            return (o instanceof Number) ? ((Number)o).longValue() : ((long)this.getDouble(key));
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a number.");
    }
    
    public String getString(final String key) {
        this.verifyIsNull();
        final Object o = this.get(key);
        if (o != null) {
            return o.toString();
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] not found.");
    }
    
    public boolean has(final String key) {
        this.verifyIsNull();
        return this.properties.containsKey(key);
    }
    
    @Override
    public int hashCode() {
        int hashcode = 19;
        if (this.isNullObject()) {
            return hashcode + JSONNull.getInstance().hashCode();
        }
        for (final Entry entry : this.properties.entrySet()) {
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            hashcode += key.hashCode() + JSONUtils.hashCode(value);
        }
        return hashcode;
    }
    
    public boolean isArray() {
        return false;
    }
    
    public boolean isEmpty() {
        return this.properties.isEmpty();
    }
    
    public boolean isNullObject() {
        return this.nullObject;
    }
    
    public Iterator keys() {
        this.verifyIsNull();
        return this.keySet().iterator();
    }
    
    public Set keySet() {
        return Collections.unmodifiableSet(this.properties.keySet());
    }
    
    public JSONArray names() {
        this.verifyIsNull();
        final JSONArray ja = new JSONArray();
        final Iterator keys = this.keys();
        while (keys.hasNext()) {
            ja.element(keys.next());
        }
        return ja;
    }
    
    public JSONArray names(final JsonConfig jsonConfig) {
        this.verifyIsNull();
        final JSONArray ja = new JSONArray();
        final Iterator keys = this.keys();
        while (keys.hasNext()) {
            ja.element(keys.next(), jsonConfig);
        }
        return ja;
    }
    
    public Object opt(final String key) {
        this.verifyIsNull();
        return (key == null) ? null : this.properties.get(key);
    }
    
    public boolean optBoolean(final String key) {
        this.verifyIsNull();
        return this.optBoolean(key, false);
    }
    
    public boolean optBoolean(final String key, final boolean defaultValue) {
        this.verifyIsNull();
        try {
            return this.getBoolean(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public double optDouble(final String key) {
        this.verifyIsNull();
        return this.optDouble(key, Double.NaN);
    }
    
    public double optDouble(final String key, final double defaultValue) {
        this.verifyIsNull();
        try {
            final Object o = this.opt(key);
            return (o instanceof Number) ? ((Number)o).doubleValue() : new Double((String)o);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public int optInt(final String key) {
        this.verifyIsNull();
        return this.optInt(key, 0);
    }
    
    public int optInt(final String key, final int defaultValue) {
        this.verifyIsNull();
        try {
            return this.getInt(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public JSONArray optJSONArray(final String key) {
        this.verifyIsNull();
        final Object o = this.opt(key);
        return (o instanceof JSONArray) ? ((JSONArray)o) : null;
    }
    
    public JSONObject optJSONObject(final String key) {
        this.verifyIsNull();
        final Object o = this.opt(key);
        return (o instanceof JSONObject) ? ((JSONObject)o) : null;
    }
    
    public long optLong(final String key) {
        this.verifyIsNull();
        return this.optLong(key, 0L);
    }
    
    public long optLong(final String key, final long defaultValue) {
        this.verifyIsNull();
        try {
            return this.getLong(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public String optString(final String key) {
        this.verifyIsNull();
        return this.optString(key, "");
    }
    
    public String optString(final String key, final String defaultValue) {
        this.verifyIsNull();
        final Object o = this.opt(key);
        return (o != null) ? o.toString() : defaultValue;
    }
    
    public Object put(final Object key, final Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null.");
        }
        final Object previous = this.properties.get(key);
        this.element(String.valueOf(key), value);
        return previous;
    }
    
    public void putAll(final Map map) {
        this.putAll(map, new JsonConfig());
    }
    
    public void putAll(final Map map, final JsonConfig jsonConfig) {
        if (map instanceof JSONObject) {
            for (final Entry entry : map.entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                this.properties.put(key, value);
            }
        }
        else {
            for (final Entry entry : map.entrySet()) {
                final String key = String.valueOf(entry.getKey());
                final Object value = entry.getValue();
                this.element(key, value, jsonConfig);
            }
        }
    }
    
    public Object remove(final Object key) {
        return this.properties.remove(key);
    }
    
    public Object remove(final String key) {
        this.verifyIsNull();
        return this.properties.remove(key);
    }
    
    public int size() {
        return this.properties.size();
    }
    
    public JSONArray toJSONArray(final JSONArray names) {
        this.verifyIsNull();
        if (names == null || names.size() == 0) {
            return null;
        }
        final JSONArray ja = new JSONArray();
        for (int i = 0; i < names.size(); ++i) {
            ja.element(this.opt(names.getString(i)));
        }
        return ja;
    }
    
    @Override
    public String toString() {
        if (this.isNullObject()) {
            return JSONNull.getInstance().toString();
        }
        try {
            final Iterator keys = this.keys();
            final StringBuffer sb = new StringBuffer("{");
            while (keys.hasNext()) {
                if (sb.length() > 1) {
                    sb.append(',');
                }
                final Object o = keys.next();
                sb.append(JSONUtils.quote(o.toString()));
                sb.append(':');
                sb.append(JSONUtils.valueToString(this.properties.get(o)));
            }
            sb.append('}');
            return sb.toString();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public String toString(final int indentFactor) {
        if (this.isNullObject()) {
            return JSONNull.getInstance().toString();
        }
        if (indentFactor == 0) {
            return this.toString();
        }
        return this.toString(indentFactor, 0);
    }
    
    public String toString(final int indentFactor, final int indent) {
        if (this.isNullObject()) {
            return JSONNull.getInstance().toString();
        }
        final int n = this.size();
        if (n == 0) {
            return "{}";
        }
        if (indentFactor == 0) {
            return this.toString();
        }
        final Iterator keys = this.keys();
        final StringBuffer sb = new StringBuffer("{");
        final int newindent = indent + indentFactor;
        if (n == 1) {
            final Object o = keys.next();
            sb.append(JSONUtils.quote(o.toString()));
            sb.append(": ");
            sb.append(JSONUtils.valueToString(this.properties.get(o), indentFactor, indent));
        }
        else {
            while (keys.hasNext()) {
                final Object o = keys.next();
                if (sb.length() > 1) {
                    sb.append(",\n");
                }
                else {
                    sb.append('\n');
                }
                for (int i = 0; i < newindent; ++i) {
                    sb.append(' ');
                }
                sb.append(JSONUtils.quote(o.toString()));
                sb.append(": ");
                sb.append(JSONUtils.valueToString(this.properties.get(o), indentFactor, newindent));
            }
            if (sb.length() > 1) {
                sb.append('\n');
                for (int i = 0; i < indent; ++i) {
                    sb.append(' ');
                }
            }
            for (int i = 0; i < indent; ++i) {
                sb.insert(0, ' ');
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public Collection values() {
        return Collections.unmodifiableCollection(this.properties.values());
    }
    
    public Writer write(final Writer writer) {
        try {
            if (this.isNullObject()) {
                writer.write(JSONNull.getInstance().toString());
                return writer;
            }
            boolean b = false;
            final Iterator keys = this.keys();
            writer.write(123);
            while (keys.hasNext()) {
                if (b) {
                    writer.write(44);
                }
                final Object k = keys.next();
                writer.write(JSONUtils.quote(k.toString()));
                writer.write(58);
                final Object v = this.properties.get(k);
                if (v instanceof JSONObject) {
                    ((JSONObject)v).write(writer);
                }
                else if (v instanceof JSONArray) {
                    ((JSONArray)v).write(writer);
                }
                else {
                    writer.write(JSONUtils.valueToString(v));
                }
                b = true;
            }
            writer.write(125);
            return writer;
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
    }
    
    private JSONObject _accumulate(final String key, final Object value, final JsonConfig jsonConfig) {
        if (this.isNullObject()) {
            throw new JSONException("Can't accumulate on null object");
        }
        if (!this.has(key)) {
            this.setInternal(key, value, jsonConfig);
        }
        else {
            final Object o = this.opt(key);
            if (o instanceof JSONArray) {
                ((JSONArray)o).element(value, jsonConfig);
            }
            else {
                this.setInternal(key, new JSONArray().element(o).element(value, jsonConfig), jsonConfig);
            }
        }
        return this;
    }
    
    @Override
    protected Object _processValue(final Object value, final JsonConfig jsonConfig) {
        if (value instanceof JSONTokener) {
            return _fromJSONTokener((JSONTokener)value, jsonConfig);
        }
        if (value != null && Enum.class.isAssignableFrom(value.getClass())) {
            return ((Enum)value).name();
        }
        return super._processValue(value, jsonConfig);
    }
    
    private JSONObject _setInternal(final String key, final Object value, final JsonConfig jsonConfig) {
        this.verifyIsNull();
        if (key == null) {
            throw new JSONException("Null key.");
        }
        if (JSONUtils.isString(value) && JSONUtils.mayBeJSON(String.valueOf(value))) {
            this.properties.put(key, value);
        }
        else if (CycleDetectionStrategy.IGNORE_PROPERTY_OBJ != value) {
            if (CycleDetectionStrategy.IGNORE_PROPERTY_ARR != value) {
                this.properties.put(key, value);
            }
        }
        return this;
    }
    
    private Object processValue(Object value, final JsonConfig jsonConfig) {
        if (value != null) {
            final JsonValueProcessor processor = jsonConfig.findJsonValueProcessor(value.getClass());
            if (processor != null) {
                value = processor.processObjectValue(null, value, jsonConfig);
                if (!JsonVerifier.isValidJsonValue(value)) {
                    throw new JSONException("Value is not a valid JSON value. " + value);
                }
            }
        }
        return this._processValue(value, jsonConfig);
    }
    
    private Object processValue(final String key, Object value, final JsonConfig jsonConfig) {
        if (value != null) {
            final JsonValueProcessor processor = jsonConfig.findJsonValueProcessor(value.getClass(), key);
            if (processor != null) {
                value = processor.processObjectValue(null, value, jsonConfig);
                if (!JsonVerifier.isValidJsonValue(value)) {
                    throw new JSONException("Value is not a valid JSON value. " + value);
                }
            }
        }
        return this._processValue(value, jsonConfig);
    }
    
    private JSONObject setInternal(final String key, final Object value, final JsonConfig jsonConfig) {
        return this._setInternal(key, this.processValue(key, value, jsonConfig), jsonConfig);
    }
    
    private void verifyIsNull() {
        if (this.isNullObject()) {
            throw new JSONException("null object");
        }
    }
    
    static {
        log = LogFactory.getLog(JSONObject.class);
    }
}
