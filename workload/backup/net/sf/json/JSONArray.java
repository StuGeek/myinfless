// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.processors.JsonVerifier;
import java.io.IOException;
import java.io.Writer;
import java.util.ListIterator;
import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.object.IdentityObjectMorpher;
import org.apache.commons.lang.StringUtils;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.JSONTokener;
import java.util.Collection;
import java.util.List;

public final class JSONArray extends AbstractJSON implements JSON, List, Comparable
{
    private List elements;
    private boolean expandElements;
    
    public static JSONArray fromObject(final Object object) {
        return fromObject(object, new JsonConfig());
    }
    
    public static JSONArray fromObject(final Object object, final JsonConfig jsonConfig) {
        if (object instanceof JSONString) {
            return _fromJSONString((JSONString)object, jsonConfig);
        }
        if (object instanceof JSONArray) {
            return _fromJSONArray((JSONArray)object, jsonConfig);
        }
        if (object instanceof Collection) {
            return _fromCollection((Collection)object, jsonConfig);
        }
        if (object instanceof JSONTokener) {
            return _fromJSONTokener((JSONTokener)object, jsonConfig);
        }
        if (object instanceof String) {
            return _fromString((String)object, jsonConfig);
        }
        if (object != null && object.getClass().isArray()) {
            final Class type = object.getClass().getComponentType();
            if (!type.isPrimitive()) {
                return _fromArray((Object[])object, jsonConfig);
            }
            if (type == Boolean.TYPE) {
                return _fromArray((boolean[])object, jsonConfig);
            }
            if (type == Byte.TYPE) {
                return _fromArray((byte[])object, jsonConfig);
            }
            if (type == Short.TYPE) {
                return _fromArray((short[])object, jsonConfig);
            }
            if (type == Integer.TYPE) {
                return _fromArray((int[])object, jsonConfig);
            }
            if (type == Long.TYPE) {
                return _fromArray((long[])object, jsonConfig);
            }
            if (type == Float.TYPE) {
                return _fromArray((float[])object, jsonConfig);
            }
            if (type == Double.TYPE) {
                return _fromArray((double[])object, jsonConfig);
            }
            if (type == Character.TYPE) {
                return _fromArray((char[])object, jsonConfig);
            }
            throw new JSONException("Unsupported type");
        }
        else {
            if (JSONUtils.isBoolean(object) || JSONUtils.isFunction(object) || JSONUtils.isNumber(object) || JSONUtils.isNull(object) || JSONUtils.isString(object) || object instanceof JSON) {
                AbstractJSON.fireArrayStartEvent(jsonConfig);
                final JSONArray jsonArray = new JSONArray().element(object, jsonConfig);
                AbstractJSON.fireElementAddedEvent(0, jsonArray.get(0), jsonConfig);
                AbstractJSON.fireArrayStartEvent(jsonConfig);
                return jsonArray;
            }
            if (object instanceof Enum) {
                return _fromArray((Enum)object, jsonConfig);
            }
            if (object instanceof Annotation || (object != null && object.getClass().isAnnotation())) {
                throw new JSONException("Unsupported type");
            }
            if (JSONUtils.isObject(object)) {
                AbstractJSON.fireArrayStartEvent(jsonConfig);
                final JSONArray jsonArray = new JSONArray().element(JSONObject.fromObject(object, jsonConfig));
                AbstractJSON.fireElementAddedEvent(0, jsonArray.get(0), jsonConfig);
                AbstractJSON.fireArrayStartEvent(jsonConfig);
                return jsonArray;
            }
            throw new JSONException("Unsupported type");
        }
    }
    
    public static Class[] getCollectionType(final PropertyDescriptor pd, final boolean useGetter) throws JSONException {
        Type type;
        if (useGetter) {
            final Method m = pd.getReadMethod();
            type = m.getGenericReturnType();
        }
        else {
            final Method m = pd.getWriteMethod();
            final Type[] gpts = m.getGenericParameterTypes();
            if (1 != gpts.length) {
                throw new JSONException("method " + m + " is not a standard setter");
            }
            type = gpts[0];
        }
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        final ParameterizedType pType = (ParameterizedType)type;
        final Type[] actualTypes = pType.getActualTypeArguments();
        final Class[] ret = new Class[actualTypes.length];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = (Class)actualTypes[i];
        }
        return ret;
    }
    
    public static int[] getDimensions(final JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return new int[] { 0 };
        }
        final List dims = new ArrayList();
        processArrayDimensions(jsonArray, dims, 0);
        final int[] dimensions = new int[dims.size()];
        int j = 0;
        final Iterator i = dims.iterator();
        while (i.hasNext()) {
            dimensions[j++] = i.next();
        }
        return dimensions;
    }
    
    public static Object toArray(final JSONArray jsonArray) {
        return toArray(jsonArray, new JsonConfig());
    }
    
    public static Object toArray(final JSONArray jsonArray, final Class objectClass) {
        final JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toArray(jsonArray, jsonConfig);
    }
    
    public static Object toArray(final JSONArray jsonArray, final Class objectClass, final Map classMap) {
        final JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return toArray(jsonArray, jsonConfig);
    }
    
    public static Object toArray(final JSONArray jsonArray, final JsonConfig jsonConfig) {
        final Class objectClass = jsonConfig.getRootClass();
        final Map classMap = jsonConfig.getClassMap();
        if (jsonArray.size() == 0) {
            return Array.newInstance((objectClass == null) ? Object.class : objectClass, 0);
        }
        final int[] dimensions = getDimensions(jsonArray);
        final Object array = Array.newInstance((objectClass == null) ? Object.class : objectClass, dimensions);
        for (int size = jsonArray.size(), i = 0; i < size; ++i) {
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)) {
                Array.set(array, i, null);
            }
            else {
                final Class type = value.getClass();
                if (JSONArray.class.isAssignableFrom(type)) {
                    Array.set(array, i, toArray((JSONArray)value, objectClass, classMap));
                }
                else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)) {
                    if (objectClass != null && !objectClass.isAssignableFrom(type)) {
                        value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                    }
                    Array.set(array, i, value);
                }
                else if (JSONUtils.isNumber(type)) {
                    if (objectClass != null && (Byte.class.isAssignableFrom(objectClass) || Byte.TYPE.isAssignableFrom(objectClass))) {
                        Array.set(array, i, Byte.valueOf(String.valueOf(value)));
                    }
                    else if (objectClass != null && (Short.class.isAssignableFrom(objectClass) || Short.TYPE.isAssignableFrom(objectClass))) {
                        Array.set(array, i, Short.valueOf(String.valueOf(value)));
                    }
                    else {
                        Array.set(array, i, value);
                    }
                }
                else if (objectClass != null) {
                    final JsonConfig jsc = jsonConfig.copy();
                    jsc.setRootClass(objectClass);
                    jsc.setClassMap(classMap);
                    Array.set(array, i, JSONObject.toBean((JSONObject)value, jsc));
                }
                else {
                    Array.set(array, i, JSONObject.toBean((JSONObject)value));
                }
            }
        }
        return array;
    }
    
    public static Object toArray(final JSONArray jsonArray, final Object root, final JsonConfig jsonConfig) {
        final Class objectClass = root.getClass();
        if (jsonArray.size() == 0) {
            return Array.newInstance(objectClass, 0);
        }
        final int[] dimensions = getDimensions(jsonArray);
        final Object array = Array.newInstance((objectClass == null) ? Object.class : objectClass, dimensions);
        for (int size = jsonArray.size(), i = 0; i < size; ++i) {
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)) {
                Array.set(array, i, null);
            }
            else {
                final Class type = value.getClass();
                if (JSONArray.class.isAssignableFrom(type)) {
                    Array.set(array, i, toArray((JSONArray)value, root, jsonConfig));
                }
                else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type) || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)) {
                    if (objectClass != null && !objectClass.isAssignableFrom(type)) {
                        value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                    }
                    Array.set(array, i, value);
                }
                else {
                    try {
                        final Object newRoot = jsonConfig.getNewBeanInstanceStrategy().newInstance(root.getClass(), null);
                        Array.set(array, i, JSONObject.toBean((JSONObject)value, newRoot, jsonConfig));
                    }
                    catch (JSONException jsone) {
                        throw jsone;
                    }
                    catch (Exception e) {
                        throw new JSONException(e);
                    }
                }
            }
        }
        return array;
    }
    
    public static Collection toCollection(final JSONArray jsonArray) {
        return toCollection(jsonArray, new JsonConfig());
    }
    
    public static Collection toCollection(final JSONArray jsonArray, final Class objectClass) {
        final JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toCollection(jsonArray, jsonConfig);
    }
    
    public static Collection toCollection(final JSONArray jsonArray, final JsonConfig jsonConfig) {
        Collection collection = null;
        final Class collectionType = jsonConfig.getCollectionType();
        if (collectionType.isInterface()) {
            if (collectionType.equals(List.class)) {
                collection = new ArrayList();
            }
            else {
                if (!collectionType.equals(Set.class)) {
                    throw new JSONException("unknown interface: " + collectionType);
                }
                collection = new HashSet();
            }
        }
        else {
            try {
                collection = collectionType.newInstance();
            }
            catch (InstantiationException e) {
                throw new JSONException(e);
            }
            catch (IllegalAccessException e2) {
                throw new JSONException(e2);
            }
        }
        final Class objectClass = jsonConfig.getRootClass();
        final Map classMap = jsonConfig.getClassMap();
        for (int size = jsonArray.size(), i = 0; i < size; ++i) {
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)) {
                collection.add(null);
            }
            else {
                final Class type = value.getClass();
                if (JSONArray.class.isAssignableFrom(value.getClass())) {
                    collection.add(toCollection((JSONArray)value, jsonConfig));
                }
                else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type) || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)) {
                    if (objectClass != null && !objectClass.isAssignableFrom(type)) {
                        value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                    }
                    collection.add(value);
                }
                else if (objectClass != null) {
                    final JsonConfig jsc = jsonConfig.copy();
                    jsc.setRootClass(objectClass);
                    jsc.setClassMap(classMap);
                    collection.add(JSONObject.toBean((JSONObject)value, jsc));
                }
                else {
                    collection.add(JSONObject.toBean((JSONObject)value));
                }
            }
        }
        return collection;
    }
    
    @Deprecated
    public static List toList(final JSONArray jsonArray) {
        return toList(jsonArray, new JsonConfig());
    }
    
    @Deprecated
    public static List toList(final JSONArray jsonArray, final Class objectClass) {
        final JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toList(jsonArray, jsonConfig);
    }
    
    @Deprecated
    public static List toList(final JSONArray jsonArray, final Class objectClass, final Map classMap) {
        final JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return toList(jsonArray, jsonConfig);
    }
    
    @Deprecated
    public static List toList(final JSONArray jsonArray, final JsonConfig jsonConfig) {
        if (jsonArray.size() == 0) {
            return new ArrayList();
        }
        final Class objectClass = jsonConfig.getRootClass();
        final Map classMap = jsonConfig.getClassMap();
        final List list = new ArrayList();
        for (int size = jsonArray.size(), i = 0; i < size; ++i) {
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)) {
                list.add(null);
            }
            else {
                final Class type = value.getClass();
                if (JSONArray.class.isAssignableFrom(type)) {
                    list.add(toList((JSONArray)value, objectClass, classMap));
                }
                else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type) || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)) {
                    if (objectClass != null && !objectClass.isAssignableFrom(type)) {
                        value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                    }
                    list.add(value);
                }
                else if (objectClass != null) {
                    final JsonConfig jsc = jsonConfig.copy();
                    jsc.setRootClass(objectClass);
                    jsc.setClassMap(classMap);
                    list.add(JSONObject.toBean((JSONObject)value, jsc));
                }
                else {
                    list.add(JSONObject.toBean((JSONObject)value));
                }
            }
        }
        return list;
    }
    
    public static List toList(final JSONArray jsonArray, final Object root, final JsonConfig jsonConfig) {
        if (jsonArray.size() == 0 || root == null) {
            return new ArrayList();
        }
        final List list = new ArrayList();
        for (int size = jsonArray.size(), i = 0; i < size; ++i) {
            final Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)) {
                list.add(null);
            }
            else {
                final Class type = value.getClass();
                if (JSONArray.class.isAssignableFrom(type)) {
                    list.add(toList((JSONArray)value, root, jsonConfig));
                }
                else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type) || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)) {
                    list.add(value);
                }
                else {
                    try {
                        final Object newRoot = jsonConfig.getNewBeanInstanceStrategy().newInstance(root.getClass(), null);
                        list.add(JSONObject.toBean((JSONObject)value, newRoot, jsonConfig));
                    }
                    catch (JSONException jsone) {
                        throw jsone;
                    }
                    catch (Exception e) {
                        throw new JSONException(e);
                    }
                }
            }
        }
        return list;
    }
    
    private static JSONArray _fromArray(final boolean[] array, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; ++i) {
            final Boolean b = array[i] ? Boolean.TRUE : Boolean.FALSE;
            jsonArray.addValue(b, jsonConfig);
            AbstractJSON.fireElementAddedEvent(i, b, jsonConfig);
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromArray(final byte[] array, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; ++i) {
            final Number n = JSONUtils.transformNumber(new Byte(array[i]));
            jsonArray.addValue(n, jsonConfig);
            AbstractJSON.fireElementAddedEvent(i, n, jsonConfig);
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromArray(final char[] array, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; ++i) {
            final Character c = new Character(array[i]);
            jsonArray.addValue(c, jsonConfig);
            AbstractJSON.fireElementAddedEvent(i, c, jsonConfig);
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromArray(final double[] array, final JsonConfig jsonConfig) {
        JSONException jsone2 = null;
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < array.length; ++i) {
                final Double d = new Double(array[i]);
                JSONUtils.testValidity(d);
                jsonArray.addValue(d, jsonConfig);
                AbstractJSON.fireElementAddedEvent(i, d, jsonConfig);
            }
        }
        catch (JSONException jsone2) {
            AbstractJSON.removeInstance(array);
            AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
            throw jsone2;
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromArray(final Enum e, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(e)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(e);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(e);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException re) {
                AbstractJSON.removeInstance(e);
                final JSONException jsone2 = new JSONException(re);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        if (e != null) {
            jsonArray.addValue(e, jsonConfig);
            AbstractJSON.fireElementAddedEvent(0, jsonArray.get(0), jsonConfig);
            AbstractJSON.removeInstance(e);
            AbstractJSON.fireArrayEndEvent(jsonConfig);
            return jsonArray;
        }
        final JSONException jsone2 = new JSONException("enum value is null");
        AbstractJSON.removeInstance(e);
        AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
        throw jsone2;
    }
    
    private static JSONArray _fromArray(final float[] array, final JsonConfig jsonConfig) {
        JSONException jsone2 = null;
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < array.length; ++i) {
                final Float f = new Float(array[i]);
                JSONUtils.testValidity(f);
                jsonArray.addValue(f, jsonConfig);
                AbstractJSON.fireElementAddedEvent(i, f, jsonConfig);
            }
        }
        catch (JSONException jsone2) {
            AbstractJSON.removeInstance(array);
            AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
            throw jsone2;
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromArray(final int[] array, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; ++i) {
            final Number n = new Integer(array[i]);
            jsonArray.addValue(n, jsonConfig);
            AbstractJSON.fireElementAddedEvent(i, n, jsonConfig);
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromArray(final long[] array, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; ++i) {
            final Number n = JSONUtils.transformNumber(new Long(array[i]));
            jsonArray.addValue(n, jsonConfig);
            AbstractJSON.fireElementAddedEvent(i, n, jsonConfig);
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromArray(final Object[] array, final JsonConfig jsonConfig) {
        JSONException jsone2 = null;
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < array.length; ++i) {
                final Object element = array[i];
                jsonArray.addValue(element, jsonConfig);
                AbstractJSON.fireElementAddedEvent(i, jsonArray.get(i), jsonConfig);
            }
        }
        catch (JSONException jsone2) {
            AbstractJSON.removeInstance(array);
            AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
            throw jsone2;
        }
        catch (RuntimeException e2) {
            AbstractJSON.removeInstance(array);
            final JSONException jsone3 = new JSONException(e2);
            AbstractJSON.fireErrorEvent(jsone3, jsonConfig);
            throw jsone3;
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromArray(final short[] array, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; ++i) {
            final Number n = JSONUtils.transformNumber(new Short(array[i]));
            jsonArray.addValue(n, jsonConfig);
            AbstractJSON.fireElementAddedEvent(i, n, jsonConfig);
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromCollection(final Collection collection, final JsonConfig jsonConfig) {
        JSONException jsone2 = null;
        if (!AbstractJSON.addInstance(collection)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(collection);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(collection);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(collection);
                jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        try {
            int i = 0;
            for (final Object element : collection) {
                jsonArray.addValue(element, jsonConfig);
                AbstractJSON.fireElementAddedEvent(i, jsonArray.get(i++), jsonConfig);
            }
        }
        catch (JSONException jsone2) {
            AbstractJSON.removeInstance(collection);
            AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
            throw jsone2;
        }
        catch (RuntimeException e2) {
            AbstractJSON.removeInstance(collection);
            final JSONException jsone3 = new JSONException(e2);
            AbstractJSON.fireErrorEvent(jsone3, jsonConfig);
            throw jsone3;
        }
        AbstractJSON.removeInstance(collection);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromJSONArray(final JSONArray array, final JsonConfig jsonConfig) {
        if (!AbstractJSON.addInstance(array)) {
            try {
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }
            catch (JSONException jsone) {
                AbstractJSON.removeInstance(array);
                AbstractJSON.fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
            catch (RuntimeException e) {
                AbstractJSON.removeInstance(array);
                final JSONException jsone2 = new JSONException(e);
                AbstractJSON.fireErrorEvent(jsone2, jsonConfig);
                throw jsone2;
            }
        }
        AbstractJSON.fireArrayStartEvent(jsonConfig);
        final JSONArray jsonArray = new JSONArray();
        int index = 0;
        for (final Object element : array) {
            jsonArray.addValue(element, jsonConfig);
            AbstractJSON.fireElementAddedEvent(index++, element, jsonConfig);
        }
        AbstractJSON.removeInstance(array);
        AbstractJSON.fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }
    
    private static JSONArray _fromJSONString(final JSONString string, final JsonConfig jsonConfig) {
        return _fromJSONTokener(new JSONTokener(string.toJSONString()), jsonConfig);
    }
    
    private static JSONArray _fromJSONTokener(final JSONTokener tokener, final JsonConfig jsonConfig) {
        final JSONArray jsonArray = new JSONArray();
        int index = 0;
        try {
            if (tokener.nextClean() != '[') {
                throw tokener.syntaxError("A JSONArray text must start with '['");
            }
            AbstractJSON.fireArrayStartEvent(jsonConfig);
            if (tokener.nextClean() == ']') {
                AbstractJSON.fireArrayEndEvent(jsonConfig);
                return jsonArray;
            }
            tokener.back();
            while (true) {
                if (tokener.nextClean() == ',') {
                    tokener.back();
                    jsonArray.elements.add(JSONNull.getInstance());
                    AbstractJSON.fireElementAddedEvent(index, jsonArray.get(index++), jsonConfig);
                }
                else {
                    tokener.back();
                    final Object v = tokener.nextValue(jsonConfig);
                    if (!JSONUtils.isFunctionHeader(v)) {
                        if (v instanceof String && JSONUtils.mayBeJSON((String)v)) {
                            jsonArray.addValue("\"" + v + "\"", jsonConfig);
                        }
                        else {
                            jsonArray.addValue(v, jsonConfig);
                        }
                        AbstractJSON.fireElementAddedEvent(index, jsonArray.get(index++), jsonConfig);
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
                        jsonArray.addValue(new JSONFunction((String[])((params != null) ? StringUtils.split(params, ",") : null), text), jsonConfig);
                        AbstractJSON.fireElementAddedEvent(index, jsonArray.get(index++), jsonConfig);
                    }
                }
                switch (tokener.nextClean()) {
                    case ',':
                    case ';': {
                        if (tokener.nextClean() == ']') {
                            AbstractJSON.fireArrayEndEvent(jsonConfig);
                            return jsonArray;
                        }
                        tokener.back();
                        continue;
                    }
                    case ']': {
                        AbstractJSON.fireArrayEndEvent(jsonConfig);
                        return jsonArray;
                    }
                    default: {
                        throw tokener.syntaxError("Expected a ',' or ']'");
                    }
                }
            }
        }
        catch (JSONException jsone) {
            AbstractJSON.fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }
    }
    
    private static JSONArray _fromString(final String string, final JsonConfig jsonConfig) {
        return _fromJSONTokener(new JSONTokener(string), jsonConfig);
    }
    
    private static void processArrayDimensions(final JSONArray jsonArray, final List dims, final int index) {
        if (dims.size() <= index) {
            dims.add(new Integer(jsonArray.size()));
        }
        else {
            final int i = dims.get(index);
            if (jsonArray.size() > i) {
                dims.set(index, new Integer(jsonArray.size()));
            }
        }
        for (final Object item : jsonArray) {
            if (item instanceof JSONArray) {
                processArrayDimensions((JSONArray)item, dims, index + 1);
            }
        }
    }
    
    public JSONArray() {
        this.elements = new ArrayList();
    }
    
    public void add(final int index, final Object value) {
        this.add(index, value, new JsonConfig());
    }
    
    public void add(final int index, final Object value, final JsonConfig jsonConfig) {
        this.elements.add(index, this.processValue(value, jsonConfig));
    }
    
    public boolean add(final Object value) {
        return this.add(value, new JsonConfig());
    }
    
    public boolean add(final Object value, final JsonConfig jsonConfig) {
        this.element(value, jsonConfig);
        return true;
    }
    
    public boolean addAll(final Collection collection) {
        return this.addAll(collection, new JsonConfig());
    }
    
    public boolean addAll(final Collection collection, final JsonConfig jsonConfig) {
        if (collection == null || collection.size() == 0) {
            return false;
        }
        final Iterator i = collection.iterator();
        while (i.hasNext()) {
            this.element(i.next(), jsonConfig);
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection collection) {
        return this.addAll(index, collection, new JsonConfig());
    }
    
    public boolean addAll(final int index, final Collection collection, final JsonConfig jsonConfig) {
        if (collection == null || collection.size() == 0) {
            return false;
        }
        int offset = 0;
        final Iterator i = collection.iterator();
        while (i.hasNext()) {
            this.elements.add(index + offset++, this.processValue(i.next(), jsonConfig));
        }
        return true;
    }
    
    public void clear() {
        this.elements.clear();
    }
    
    public int compareTo(final Object obj) {
        if (obj != null && obj instanceof JSONArray) {
            final JSONArray other = (JSONArray)obj;
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
    
    public boolean contains(final Object o) {
        return this.contains(o, new JsonConfig());
    }
    
    public boolean contains(final Object o, final JsonConfig jsonConfig) {
        return this.elements.contains(this.processValue(o, jsonConfig));
    }
    
    public boolean containsAll(final Collection collection) {
        return this.containsAll(collection, new JsonConfig());
    }
    
    public boolean containsAll(final Collection collection, final JsonConfig jsonConfig) {
        return this.elements.containsAll(fromObject(collection, jsonConfig));
    }
    
    public JSONArray discard(final int index) {
        this.elements.remove(index);
        return this;
    }
    
    public JSONArray discard(final Object o) {
        this.elements.remove(o);
        return this;
    }
    
    public JSONArray element(final boolean value) {
        return this.element(value ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONArray element(final Collection value) {
        return this.element(value, new JsonConfig());
    }
    
    public JSONArray element(final Collection value, final JsonConfig jsonConfig) {
        if (value instanceof JSONArray) {
            this.elements.add(value);
            return this;
        }
        return this.element(_fromCollection(value, jsonConfig));
    }
    
    public JSONArray element(final double value) {
        final Double d = new Double(value);
        JSONUtils.testValidity(d);
        return this.element(d);
    }
    
    public JSONArray element(final int value) {
        return this.element(new Integer(value));
    }
    
    public JSONArray element(final int index, final boolean value) {
        return this.element(index, value ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONArray element(final int index, final Collection value) {
        return this.element(index, value, new JsonConfig());
    }
    
    public JSONArray element(final int index, final Collection value, final JsonConfig jsonConfig) {
        if (!(value instanceof JSONArray)) {
            return this.element(index, _fromCollection(value, jsonConfig));
        }
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < this.size()) {
            this.elements.set(index, value);
        }
        else {
            while (index != this.size()) {
                this.element(JSONNull.getInstance());
            }
            this.element(value, jsonConfig);
        }
        return this;
    }
    
    public JSONArray element(final int index, final double value) {
        return this.element(index, new Double(value));
    }
    
    public JSONArray element(final int index, final int value) {
        return this.element(index, new Integer(value));
    }
    
    public JSONArray element(final int index, final long value) {
        return this.element(index, new Long(value));
    }
    
    public JSONArray element(final int index, final Map value) {
        return this.element(index, value, new JsonConfig());
    }
    
    public JSONArray element(final int index, final Map value, final JsonConfig jsonConfig) {
        if (!(value instanceof JSONObject)) {
            return this.element(index, JSONObject.fromObject(value, jsonConfig));
        }
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < this.size()) {
            this.elements.set(index, value);
        }
        else {
            while (index != this.size()) {
                this.element(JSONNull.getInstance());
            }
            this.element(value, jsonConfig);
        }
        return this;
    }
    
    public JSONArray element(final int index, final Object value) {
        return this.element(index, value, new JsonConfig());
    }
    
    public JSONArray element(final int index, final Object value, final JsonConfig jsonConfig) {
        JSONUtils.testValidity(value);
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < this.size()) {
            this.elements.set(index, this.processValue(value, jsonConfig));
        }
        else {
            while (index != this.size()) {
                this.element(JSONNull.getInstance());
            }
            this.element(value, jsonConfig);
        }
        return this;
    }
    
    public JSONArray element(final int index, final String value) {
        return this.element(index, value, new JsonConfig());
    }
    
    public JSONArray element(final int index, final String value, final JsonConfig jsonConfig) {
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < this.size()) {
            if (value == null) {
                this.elements.set(index, "");
            }
            else if (JSONUtils.mayBeJSON(value)) {
                try {
                    this.elements.set(index, JSONSerializer.toJSON((Object)value, jsonConfig));
                }
                catch (JSONException jsone) {
                    this.elements.set(index, JSONUtils.stripQuotes(value));
                }
            }
            else {
                this.elements.set(index, JSONUtils.stripQuotes(value));
            }
        }
        else {
            while (index != this.size()) {
                this.element(JSONNull.getInstance());
            }
            this.element(value, jsonConfig);
        }
        return this;
    }
    
    public JSONArray element(final JSONNull value) {
        this.elements.add(value);
        return this;
    }
    
    public JSONArray element(final JSONObject value) {
        this.elements.add(value);
        return this;
    }
    
    public JSONArray element(final long value) {
        return this.element(JSONUtils.transformNumber(new Long(value)));
    }
    
    public JSONArray element(final Map value) {
        return this.element(value, new JsonConfig());
    }
    
    public JSONArray element(final Map value, final JsonConfig jsonConfig) {
        if (value instanceof JSONObject) {
            this.elements.add(value);
            return this;
        }
        return this.element(JSONObject.fromObject(value, jsonConfig));
    }
    
    public JSONArray element(final Object value) {
        return this.element(value, new JsonConfig());
    }
    
    public JSONArray element(final Object value, final JsonConfig jsonConfig) {
        return this.addValue(value, jsonConfig);
    }
    
    public JSONArray element(final String value) {
        return this.element(value, new JsonConfig());
    }
    
    public JSONArray element(final String value, final JsonConfig jsonConfig) {
        if (value == null) {
            this.elements.add("");
        }
        else if (JSONUtils.hasQuotes(value)) {
            this.elements.add(value);
        }
        else if (JSONNull.getInstance().equals(value)) {
            this.elements.add(JSONNull.getInstance());
        }
        else if (JSONUtils.isJsonKeyword(value, jsonConfig)) {
            if (jsonConfig.isJavascriptCompliant() && "undefined".equals(value)) {
                this.elements.add(JSONNull.getInstance());
            }
            else {
                this.elements.add(value);
            }
        }
        else if (JSONUtils.mayBeJSON(value)) {
            try {
                this.elements.add(JSONSerializer.toJSON((Object)value, jsonConfig));
            }
            catch (JSONException jsone) {
                this.elements.add(value);
            }
        }
        else {
            this.elements.add(value);
        }
        return this;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof JSONArray)) {
            return false;
        }
        final JSONArray other = (JSONArray)obj;
        if (other.size() != this.size()) {
            return false;
        }
        for (int max = this.size(), i = 0; i < max; ++i) {
            final Object o1 = this.get(i);
            final Object o2 = other.get(i);
            if (JSONNull.getInstance().equals(o1)) {
                if (!JSONNull.getInstance().equals(o2)) {
                    return false;
                }
            }
            else {
                if (JSONNull.getInstance().equals(o2)) {
                    return false;
                }
                if (o1 instanceof JSONArray && o2 instanceof JSONArray) {
                    final JSONArray e = (JSONArray)o1;
                    final JSONArray a = (JSONArray)o2;
                    if (!a.equals(e)) {
                        return false;
                    }
                }
                else if (o1 instanceof String && o2 instanceof JSONFunction) {
                    if (!o1.equals(String.valueOf(o2))) {
                        return false;
                    }
                }
                else if (o1 instanceof JSONFunction && o2 instanceof String) {
                    if (!o2.equals(String.valueOf(o1))) {
                        return false;
                    }
                }
                else if (o1 instanceof JSONObject && o2 instanceof JSONObject) {
                    if (!o1.equals(o2)) {
                        return false;
                    }
                }
                else if (o1 instanceof JSONArray && o2 instanceof JSONArray) {
                    if (!o1.equals(o2)) {
                        return false;
                    }
                }
                else if (o1 instanceof JSONFunction && o2 instanceof JSONFunction) {
                    if (!o1.equals(o2)) {
                        return false;
                    }
                }
                else if (o1 instanceof String) {
                    if (!o1.equals(String.valueOf(o2))) {
                        return false;
                    }
                }
                else if (o2 instanceof String) {
                    if (!o2.equals(String.valueOf(o1))) {
                        return false;
                    }
                }
                else {
                    final Morpher m1 = JSONUtils.getMorpherRegistry().getMorpherFor(o1.getClass());
                    final Morpher m2 = JSONUtils.getMorpherRegistry().getMorpherFor(o2.getClass());
                    if (m1 != null && m1 != IdentityObjectMorpher.getInstance()) {
                        if (!o1.equals(JSONUtils.getMorpherRegistry().morph(o1.getClass(), o2))) {
                            return false;
                        }
                    }
                    else if (m2 != null && m2 != IdentityObjectMorpher.getInstance()) {
                        if (!JSONUtils.getMorpherRegistry().morph(o1.getClass(), o1).equals(o2)) {
                            return false;
                        }
                    }
                    else if (!o1.equals(o2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public Object get(final int index) {
        return this.elements.get(index);
    }
    
    public boolean getBoolean(final int index) {
        final Object o = this.get(index);
        if (o != null) {
            if (o.equals(Boolean.FALSE) || (o instanceof String && ((String)o).equalsIgnoreCase("false"))) {
                return false;
            }
            if (o.equals(Boolean.TRUE) || (o instanceof String && ((String)o).equalsIgnoreCase("true"))) {
                return true;
            }
        }
        throw new JSONException("JSONArray[" + index + "] is not a Boolean.");
    }
    
    public double getDouble(final int index) {
        final Object o = this.get(index);
        if (o != null) {
            try {
                return (o instanceof Number) ? ((Number)o).doubleValue() : Double.parseDouble((String)o);
            }
            catch (Exception e) {
                throw new JSONException("JSONArray[" + index + "] is not a number.");
            }
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }
    
    public int getInt(final int index) {
        final Object o = this.get(index);
        if (o != null) {
            return (o instanceof Number) ? ((Number)o).intValue() : ((int)this.getDouble(index));
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }
    
    public JSONArray getJSONArray(final int index) {
        final Object o = this.get(index);
        if (o != null && o instanceof JSONArray) {
            return (JSONArray)o;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final int index) {
        final Object o = this.get(index);
        if (JSONNull.getInstance().equals(o)) {
            return new JSONObject(true);
        }
        if (o instanceof JSONObject) {
            return (JSONObject)o;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
    }
    
    public long getLong(final int index) {
        final Object o = this.get(index);
        if (o != null) {
            return (o instanceof Number) ? ((Number)o).longValue() : ((long)this.getDouble(index));
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }
    
    public String getString(final int index) {
        final Object o = this.get(index);
        if (o != null) {
            return o.toString();
        }
        throw new JSONException("JSONArray[" + index + "] not found.");
    }
    
    @Override
    public int hashCode() {
        int hashcode = 29;
        for (final Object element : this.elements) {
            hashcode += JSONUtils.hashCode(element);
        }
        return hashcode;
    }
    
    public int indexOf(final Object o) {
        return this.elements.indexOf(o);
    }
    
    public boolean isArray() {
        return true;
    }
    
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }
    
    public boolean isExpandElements() {
        return this.expandElements;
    }
    
    public Iterator iterator() {
        return new JSONArrayListIterator();
    }
    
    public String join(final String separator) {
        return this.join(separator, false);
    }
    
    public String join(final String separator, final boolean stripQuotes) {
        final int len = this.size();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; ++i) {
            if (i > 0) {
                sb.append(separator);
            }
            final String value = JSONUtils.valueToString(this.elements.get(i));
            sb.append(stripQuotes ? JSONUtils.stripQuotes(value) : value);
        }
        return sb.toString();
    }
    
    public int lastIndexOf(final Object o) {
        return this.elements.lastIndexOf(o);
    }
    
    public ListIterator listIterator() {
        return this.listIterator(0);
    }
    
    public ListIterator listIterator(final int index) {
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return new JSONArrayListIterator(index);
    }
    
    public Object opt(final int index) {
        return (index < 0 || index >= this.size()) ? null : this.elements.get(index);
    }
    
    public boolean optBoolean(final int index) {
        return this.optBoolean(index, false);
    }
    
    public boolean optBoolean(final int index, final boolean defaultValue) {
        try {
            return this.getBoolean(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public double optDouble(final int index) {
        return this.optDouble(index, Double.NaN);
    }
    
    public double optDouble(final int index, final double defaultValue) {
        try {
            return this.getDouble(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public int optInt(final int index) {
        return this.optInt(index, 0);
    }
    
    public int optInt(final int index, final int defaultValue) {
        try {
            return this.getInt(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public JSONArray optJSONArray(final int index) {
        final Object o = this.opt(index);
        return (o instanceof JSONArray) ? ((JSONArray)o) : null;
    }
    
    public JSONObject optJSONObject(final int index) {
        final Object o = this.opt(index);
        return (o instanceof JSONObject) ? ((JSONObject)o) : null;
    }
    
    public long optLong(final int index) {
        return this.optLong(index, 0L);
    }
    
    public long optLong(final int index, final long defaultValue) {
        try {
            return this.getLong(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public String optString(final int index) {
        return this.optString(index, "");
    }
    
    public String optString(final int index, final String defaultValue) {
        final Object o = this.opt(index);
        return (o != null) ? o.toString() : defaultValue;
    }
    
    public Object remove(final int index) {
        return this.elements.remove(index);
    }
    
    public boolean remove(final Object o) {
        return this.elements.remove(o);
    }
    
    public boolean removeAll(final Collection collection) {
        return this.removeAll(collection, new JsonConfig());
    }
    
    public boolean removeAll(final Collection collection, final JsonConfig jsonConfig) {
        return this.elements.removeAll(fromObject(collection, jsonConfig));
    }
    
    public boolean retainAll(final Collection collection) {
        return this.retainAll(collection, new JsonConfig());
    }
    
    public boolean retainAll(final Collection collection, final JsonConfig jsonConfig) {
        return this.elements.retainAll(fromObject(collection, jsonConfig));
    }
    
    public Object set(final int index, final Object value) {
        return this.set(index, value, new JsonConfig());
    }
    
    public Object set(final int index, final Object value, final JsonConfig jsonConfig) {
        final Object previous = this.get(index);
        this.element(index, value, jsonConfig);
        return previous;
    }
    
    public void setExpandElements(final boolean expandElements) {
        this.expandElements = expandElements;
    }
    
    public int size() {
        return this.elements.size();
    }
    
    public List subList(final int fromIndex, final int toIndex) {
        return this.elements.subList(fromIndex, toIndex);
    }
    
    public Object[] toArray() {
        return this.elements.toArray();
    }
    
    public Object[] toArray(final Object[] array) {
        return this.elements.toArray(array);
    }
    
    public JSONObject toJSONObject(final JSONArray names) {
        if (names == null || names.size() == 0 || this.size() == 0) {
            return null;
        }
        final JSONObject jo = new JSONObject();
        for (int i = 0; i < names.size(); ++i) {
            jo.element(names.getString(i), this.opt(i));
        }
        return jo;
    }
    
    @Override
    public String toString() {
        try {
            return '[' + this.join(",") + ']';
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public String toString(final int indentFactor) {
        if (indentFactor == 0) {
            return this.toString();
        }
        return this.toString(indentFactor, 0);
    }
    
    public String toString(final int indentFactor, final int indent) {
        final int len = this.size();
        if (len == 0) {
            return "[]";
        }
        if (indentFactor == 0) {
            return this.toString();
        }
        final StringBuffer sb = new StringBuffer("[");
        if (len == 1) {
            sb.append(JSONUtils.valueToString(this.elements.get(0), indentFactor, indent));
        }
        else {
            final int newindent = indent + indentFactor;
            sb.append('\n');
            for (int i = 0; i < len; ++i) {
                if (i > 0) {
                    sb.append(",\n");
                }
                for (int j = 0; j < newindent; ++j) {
                    sb.append(' ');
                }
                sb.append(JSONUtils.valueToString(this.elements.get(i), indentFactor, newindent));
            }
            sb.append('\n');
            for (int i = 0; i < indent; ++i) {
                sb.append(' ');
            }
            for (int i = 0; i < indent; ++i) {
                sb.insert(0, ' ');
            }
        }
        sb.append(']');
        return sb.toString();
    }
    
    public Writer write(final Writer writer) {
        try {
            boolean b = false;
            final int len = this.size();
            writer.write(91);
            for (int i = 0; i < len; ++i) {
                if (b) {
                    writer.write(44);
                }
                final Object v = this.elements.get(i);
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
            writer.write(93);
            return writer;
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
    }
    
    protected JSONArray addString(final String str) {
        if (str != null) {
            this.elements.add(str);
        }
        return this;
    }
    
    private JSONArray _addValue(final Object value, final JsonConfig jsonConfig) {
        this.elements.add(value);
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
        if (value instanceof Annotation || (value != null && value.getClass().isAnnotation())) {
            throw new JSONException("Unsupported type");
        }
        return super._processValue(value, jsonConfig);
    }
    
    private JSONArray addValue(final Object value, final JsonConfig jsonConfig) {
        return this._addValue(this.processValue(value, jsonConfig), jsonConfig);
    }
    
    private Object processValue(Object value, final JsonConfig jsonConfig) {
        if (value != null) {
            final JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(value.getClass());
            if (jsonValueProcessor != null) {
                value = jsonValueProcessor.processArrayValue(value, jsonConfig);
                if (!JsonVerifier.isValidJsonValue(value)) {
                    throw new JSONException("Value is not a valid JSON value. " + value);
                }
            }
        }
        return this._processValue(value, jsonConfig);
    }
    
    private class JSONArrayListIterator implements ListIterator
    {
        int currentIndex;
        int lastIndex;
        
        JSONArrayListIterator() {
            this.currentIndex = 0;
            this.lastIndex = -1;
        }
        
        JSONArrayListIterator(final int index) {
            this.currentIndex = 0;
            this.lastIndex = -1;
            this.currentIndex = index;
        }
        
        public boolean hasNext() {
            return this.currentIndex != JSONArray.this.size();
        }
        
        public Object next() {
            try {
                final Object next = JSONArray.this.get(this.currentIndex);
                this.lastIndex = this.currentIndex++;
                return next;
            }
            catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }
        
        public void remove() {
            if (this.lastIndex == -1) {
                throw new IllegalStateException();
            }
            try {
                JSONArray.this.remove(this.lastIndex);
                if (this.lastIndex < this.currentIndex) {
                    --this.currentIndex;
                }
                this.lastIndex = -1;
            }
            catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
        
        public boolean hasPrevious() {
            return this.currentIndex != 0;
        }
        
        public Object previous() {
            try {
                final int index = this.currentIndex - 1;
                final Object previous = JSONArray.this.get(index);
                final int n = index;
                this.currentIndex = n;
                this.lastIndex = n;
                return previous;
            }
            catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }
        
        public int nextIndex() {
            return this.currentIndex;
        }
        
        public int previousIndex() {
            return this.currentIndex - 1;
        }
        
        public void set(final Object obj) {
            if (this.lastIndex == -1) {
                throw new IllegalStateException();
            }
            try {
                JSONArray.this.set(this.lastIndex, obj);
            }
            catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
        
        public void add(final Object obj) {
            try {
                JSONArray.this.add(this.currentIndex++, obj);
                this.lastIndex = -1;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
