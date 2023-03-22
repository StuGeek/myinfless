// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import net.sf.json.processors.DefaultDefaultValueProcessor;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import java.util.HashSet;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.processors.JsonBeanProcessor;
import net.sf.json.processors.PropertyNameProcessor;
import java.util.Set;
import java.util.Collection;
import net.sf.json.util.JsonEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.json.util.PropertySetStrategy;
import net.sf.json.util.PropertyFilter;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.MultiKeyMap;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JavaIdentifierTransformer;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.processors.PropertyNameProcessorMatcher;
import net.sf.json.util.PropertyExclusionClassMatcher;
import net.sf.json.util.NewBeanInstanceStrategy;
import net.sf.json.processors.JsonValueProcessorMatcher;
import net.sf.json.processors.JsonBeanProcessorMatcher;
import net.sf.json.processors.DefaultValueProcessorMatcher;

public class JsonConfig
{
    public static final DefaultValueProcessorMatcher DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER;
    public static final JsonBeanProcessorMatcher DEFAULT_JSON_BEAN_PROCESSOR_MATCHER;
    public static final JsonValueProcessorMatcher DEFAULT_JSON_VALUE_PROCESSOR_MATCHER;
    public static final NewBeanInstanceStrategy DEFAULT_NEW_BEAN_INSTANCE_STRATEGY;
    public static final PropertyExclusionClassMatcher DEFAULT_PROPERTY_EXCLUSION_CLASS_MATCHER;
    public static final PropertyNameProcessorMatcher DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;
    public static final int MODE_LIST = 1;
    public static final int MODE_OBJECT_ARRAY = 2;
    public static final int MODE_SET = 2;
    private static final Class DEFAULT_COLLECTION_TYPE;
    private static final CycleDetectionStrategy DEFAULT_CYCLE_DETECTION_STRATEGY;
    private static final String[] DEFAULT_EXCLUDES;
    private static final JavaIdentifierTransformer DEFAULT_JAVA_IDENTIFIER_TRANSFORMER;
    private static final DefaultValueProcessor DEFAULT_VALUE_PROCESSOR;
    private static final String[] EMPTY_EXCLUDES;
    private int arrayMode;
    private MultiKeyMap beanKeyMap;
    private Map beanProcessorMap;
    private MultiKeyMap beanTypeMap;
    private Map classMap;
    private Class collectionType;
    private CycleDetectionStrategy cycleDetectionStrategy;
    private Map defaultValueMap;
    private DefaultValueProcessorMatcher defaultValueProcessorMatcher;
    private Class enclosedType;
    private List eventListeners;
    private String[] excludes;
    private Map exclusionMap;
    private boolean handleJettisonEmptyElement;
    private boolean handleJettisonSingleElementArray;
    private boolean ignoreDefaultExcludes;
    private boolean ignoreTransientFields;
    private boolean ignorePublicFields;
    private boolean javascriptCompliant;
    private JavaIdentifierTransformer javaIdentifierTransformer;
    private PropertyFilter javaPropertyFilter;
    private Map javaPropertyNameProcessorMap;
    private PropertyNameProcessorMatcher javaPropertyNameProcessorMatcher;
    private JsonBeanProcessorMatcher jsonBeanProcessorMatcher;
    private PropertyFilter jsonPropertyFilter;
    private Map jsonPropertyNameProcessorMap;
    private PropertyNameProcessorMatcher jsonPropertyNameProcessorMatcher;
    private JsonValueProcessorMatcher jsonValueProcessorMatcher;
    private Map keyMap;
    private NewBeanInstanceStrategy newBeanInstanceStrategy;
    private PropertyExclusionClassMatcher propertyExclusionClassMatcher;
    private PropertySetStrategy propertySetStrategy;
    private Class rootClass;
    private boolean skipJavaIdentifierTransformationInMapKeys;
    private boolean triggerEvents;
    private Map typeMap;
    private List ignoreFieldAnnotations;
    private boolean allowNonStringKeys;
    
    public JsonConfig() {
        this.arrayMode = 1;
        this.beanKeyMap = new MultiKeyMap();
        this.beanProcessorMap = new HashMap();
        this.beanTypeMap = new MultiKeyMap();
        this.collectionType = JsonConfig.DEFAULT_COLLECTION_TYPE;
        this.cycleDetectionStrategy = JsonConfig.DEFAULT_CYCLE_DETECTION_STRATEGY;
        this.defaultValueMap = new HashMap();
        this.defaultValueProcessorMatcher = JsonConfig.DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER;
        this.eventListeners = new ArrayList();
        this.excludes = JsonConfig.EMPTY_EXCLUDES;
        this.exclusionMap = new HashMap();
        this.ignorePublicFields = true;
        this.javaIdentifierTransformer = JsonConfig.DEFAULT_JAVA_IDENTIFIER_TRANSFORMER;
        this.javaPropertyNameProcessorMap = new HashMap();
        this.javaPropertyNameProcessorMatcher = JsonConfig.DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;
        this.jsonBeanProcessorMatcher = JsonConfig.DEFAULT_JSON_BEAN_PROCESSOR_MATCHER;
        this.jsonPropertyNameProcessorMap = new HashMap();
        this.jsonPropertyNameProcessorMatcher = JsonConfig.DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;
        this.jsonValueProcessorMatcher = JsonConfig.DEFAULT_JSON_VALUE_PROCESSOR_MATCHER;
        this.keyMap = new HashMap();
        this.newBeanInstanceStrategy = JsonConfig.DEFAULT_NEW_BEAN_INSTANCE_STRATEGY;
        this.propertyExclusionClassMatcher = JsonConfig.DEFAULT_PROPERTY_EXCLUSION_CLASS_MATCHER;
        this.typeMap = new HashMap();
        this.ignoreFieldAnnotations = new ArrayList();
        this.allowNonStringKeys = false;
    }
    
    public synchronized void addJsonEventListener(final JsonEventListener listener) {
        if (!this.eventListeners.contains(listener)) {
            this.eventListeners.add(listener);
        }
    }
    
    public void clearJavaPropertyNameProcessors() {
        this.javaPropertyNameProcessorMap.clear();
    }
    
    public void clearJsonBeanProcessors() {
        this.beanProcessorMap.clear();
    }
    
    public synchronized void clearJsonEventListeners() {
        this.eventListeners.clear();
    }
    
    public void clearJsonPropertyNameProcessors() {
        this.jsonPropertyNameProcessorMap.clear();
    }
    
    public void clearJsonValueProcessors() {
        this.beanKeyMap.clear();
        this.beanTypeMap.clear();
        this.keyMap.clear();
        this.typeMap.clear();
    }
    
    public void clearPropertyExclusions() {
        this.exclusionMap.clear();
    }
    
    public void clearPropertyNameProcessors() {
        this.clearJavaPropertyNameProcessors();
    }
    
    public JsonConfig copy() {
        final JsonConfig jsc = new JsonConfig();
        jsc.beanKeyMap.putAll(this.beanKeyMap);
        jsc.beanTypeMap.putAll(this.beanTypeMap);
        jsc.classMap = new HashMap();
        if (this.classMap != null) {
            jsc.classMap.putAll(this.classMap);
        }
        jsc.cycleDetectionStrategy = this.cycleDetectionStrategy;
        if (this.eventListeners != null) {
            jsc.eventListeners.addAll(this.eventListeners);
        }
        if (this.excludes != null) {
            jsc.excludes = new String[this.excludes.length];
            System.arraycopy(this.excludes, 0, jsc.excludes, 0, this.excludes.length);
        }
        jsc.handleJettisonEmptyElement = this.handleJettisonEmptyElement;
        jsc.handleJettisonSingleElementArray = this.handleJettisonSingleElementArray;
        jsc.ignoreDefaultExcludes = this.ignoreDefaultExcludes;
        jsc.ignoreTransientFields = this.ignoreTransientFields;
        jsc.ignorePublicFields = this.ignorePublicFields;
        jsc.javaIdentifierTransformer = this.javaIdentifierTransformer;
        jsc.javascriptCompliant = this.javascriptCompliant;
        jsc.keyMap.putAll(this.keyMap);
        jsc.beanProcessorMap.putAll(this.beanProcessorMap);
        jsc.rootClass = this.rootClass;
        jsc.skipJavaIdentifierTransformationInMapKeys = this.skipJavaIdentifierTransformationInMapKeys;
        jsc.triggerEvents = this.triggerEvents;
        jsc.typeMap.putAll(this.typeMap);
        jsc.jsonPropertyFilter = this.jsonPropertyFilter;
        jsc.javaPropertyFilter = this.javaPropertyFilter;
        jsc.jsonBeanProcessorMatcher = this.jsonBeanProcessorMatcher;
        jsc.newBeanInstanceStrategy = this.newBeanInstanceStrategy;
        jsc.defaultValueProcessorMatcher = this.defaultValueProcessorMatcher;
        jsc.defaultValueMap.putAll(this.defaultValueMap);
        jsc.propertySetStrategy = this.propertySetStrategy;
        jsc.collectionType = this.collectionType;
        jsc.enclosedType = this.enclosedType;
        jsc.jsonValueProcessorMatcher = this.jsonValueProcessorMatcher;
        jsc.javaPropertyNameProcessorMatcher = this.javaPropertyNameProcessorMatcher;
        jsc.javaPropertyNameProcessorMap.putAll(this.javaPropertyNameProcessorMap);
        jsc.jsonPropertyNameProcessorMatcher = this.jsonPropertyNameProcessorMatcher;
        jsc.jsonPropertyNameProcessorMap.putAll(this.jsonPropertyNameProcessorMap);
        jsc.propertyExclusionClassMatcher = this.propertyExclusionClassMatcher;
        jsc.exclusionMap.putAll(this.exclusionMap);
        jsc.ignoreFieldAnnotations.addAll(this.ignoreFieldAnnotations);
        jsc.allowNonStringKeys = this.allowNonStringKeys;
        return jsc;
    }
    
    public void disableEventTriggering() {
        this.triggerEvents = false;
    }
    
    public void enableEventTriggering() {
        this.triggerEvents = true;
    }
    
    public DefaultValueProcessor findDefaultValueProcessor(final Class target) {
        if (!this.defaultValueMap.isEmpty()) {
            final Object key = this.defaultValueProcessorMatcher.getMatch(target, this.defaultValueMap.keySet());
            final DefaultValueProcessor processor = this.defaultValueMap.get(key);
            if (processor != null) {
                return processor;
            }
        }
        return JsonConfig.DEFAULT_VALUE_PROCESSOR;
    }
    
    public PropertyNameProcessor findJavaPropertyNameProcessor(final Class beanClass) {
        if (!this.javaPropertyNameProcessorMap.isEmpty()) {
            final Object key = this.javaPropertyNameProcessorMatcher.getMatch(beanClass, this.javaPropertyNameProcessorMap.keySet());
            return this.javaPropertyNameProcessorMap.get(key);
        }
        return null;
    }
    
    public JsonBeanProcessor findJsonBeanProcessor(final Class target) {
        if (!this.beanProcessorMap.isEmpty()) {
            final Object key = this.jsonBeanProcessorMatcher.getMatch(target, this.beanProcessorMap.keySet());
            return this.beanProcessorMap.get(key);
        }
        return null;
    }
    
    public PropertyNameProcessor findJsonPropertyNameProcessor(final Class beanClass) {
        if (!this.jsonPropertyNameProcessorMap.isEmpty()) {
            final Object key = this.jsonPropertyNameProcessorMatcher.getMatch(beanClass, this.jsonPropertyNameProcessorMap.keySet());
            return this.jsonPropertyNameProcessorMap.get(key);
        }
        return null;
    }
    
    public JsonValueProcessor findJsonValueProcessor(final Class propertyType) {
        if (!this.typeMap.isEmpty()) {
            final Object key = this.jsonValueProcessorMatcher.getMatch(propertyType, this.typeMap.keySet());
            return this.typeMap.get(key);
        }
        return null;
    }
    
    public JsonValueProcessor findJsonValueProcessor(final Class beanClass, final Class propertyType, final String key) {
        JsonValueProcessor jsonValueProcessor = null;
        jsonValueProcessor = (JsonValueProcessor)this.beanKeyMap.get(beanClass, key);
        if (jsonValueProcessor != null) {
            return jsonValueProcessor;
        }
        jsonValueProcessor = (JsonValueProcessor)this.beanTypeMap.get(beanClass, propertyType);
        if (jsonValueProcessor != null) {
            return jsonValueProcessor;
        }
        jsonValueProcessor = this.keyMap.get(key);
        if (jsonValueProcessor != null) {
            return jsonValueProcessor;
        }
        final Object tkey = this.jsonValueProcessorMatcher.getMatch(propertyType, this.typeMap.keySet());
        jsonValueProcessor = this.typeMap.get(tkey);
        if (jsonValueProcessor != null) {
            return jsonValueProcessor;
        }
        return null;
    }
    
    public JsonValueProcessor findJsonValueProcessor(final Class propertyType, final String key) {
        JsonValueProcessor jsonValueProcessor = null;
        jsonValueProcessor = this.keyMap.get(key);
        if (jsonValueProcessor != null) {
            return jsonValueProcessor;
        }
        final Object tkey = this.jsonValueProcessorMatcher.getMatch(propertyType, this.typeMap.keySet());
        jsonValueProcessor = this.typeMap.get(tkey);
        if (jsonValueProcessor != null) {
            return jsonValueProcessor;
        }
        return null;
    }
    
    public PropertyNameProcessor findPropertyNameProcessor(final Class beanClass) {
        return this.findJavaPropertyNameProcessor(beanClass);
    }
    
    public int getArrayMode() {
        return this.arrayMode;
    }
    
    public Map getClassMap() {
        return this.classMap;
    }
    
    public Class getCollectionType() {
        return this.collectionType;
    }
    
    public CycleDetectionStrategy getCycleDetectionStrategy() {
        return this.cycleDetectionStrategy;
    }
    
    public DefaultValueProcessorMatcher getDefaultValueProcessorMatcher() {
        return this.defaultValueProcessorMatcher;
    }
    
    public Class getEnclosedType() {
        return this.enclosedType;
    }
    
    public String[] getExcludes() {
        return this.excludes;
    }
    
    public JavaIdentifierTransformer getJavaIdentifierTransformer() {
        return this.javaIdentifierTransformer;
    }
    
    public PropertyFilter getJavaPropertyFilter() {
        return this.javaPropertyFilter;
    }
    
    public PropertyNameProcessorMatcher getJavaPropertyNameProcessorMatcher() {
        return this.javaPropertyNameProcessorMatcher;
    }
    
    public JsonBeanProcessorMatcher getJsonBeanProcessorMatcher() {
        return this.jsonBeanProcessorMatcher;
    }
    
    public synchronized List getJsonEventListeners() {
        return this.eventListeners;
    }
    
    public PropertyFilter getJsonPropertyFilter() {
        return this.jsonPropertyFilter;
    }
    
    public PropertyNameProcessorMatcher getJsonPropertyNameProcessorMatcher() {
        return this.javaPropertyNameProcessorMatcher;
    }
    
    public JsonValueProcessorMatcher getJsonValueProcessorMatcher() {
        return this.jsonValueProcessorMatcher;
    }
    
    public Collection getMergedExcludes() {
        final Collection exclusions = new HashSet();
        for (int i = 0; i < this.excludes.length; ++i) {
            final String exclusion = this.excludes[i];
            if (!StringUtils.isBlank(this.excludes[i])) {
                exclusions.add(exclusion.trim());
            }
        }
        if (!this.ignoreDefaultExcludes) {
            for (int i = 0; i < JsonConfig.DEFAULT_EXCLUDES.length; ++i) {
                if (!exclusions.contains(JsonConfig.DEFAULT_EXCLUDES[i])) {
                    exclusions.add(JsonConfig.DEFAULT_EXCLUDES[i]);
                }
            }
        }
        return exclusions;
    }
    
    public Collection getMergedExcludes(final Class target) {
        if (target == null) {
            return this.getMergedExcludes();
        }
        final Collection exclusionSet = this.getMergedExcludes();
        if (!this.exclusionMap.isEmpty()) {
            final Object key = this.propertyExclusionClassMatcher.getMatch(target, this.exclusionMap.keySet());
            final Set set = this.exclusionMap.get(key);
            if (set != null && !set.isEmpty()) {
                for (final Object e : set) {
                    if (!exclusionSet.contains(e)) {
                        exclusionSet.add(e);
                    }
                }
            }
        }
        return exclusionSet;
    }
    
    public NewBeanInstanceStrategy getNewBeanInstanceStrategy() {
        return this.newBeanInstanceStrategy;
    }
    
    public PropertyExclusionClassMatcher getPropertyExclusionClassMatcher() {
        return this.propertyExclusionClassMatcher;
    }
    
    public PropertyNameProcessorMatcher getPropertyNameProcessorMatcher() {
        return this.getJavaPropertyNameProcessorMatcher();
    }
    
    public PropertySetStrategy getPropertySetStrategy() {
        return this.propertySetStrategy;
    }
    
    public Class getRootClass() {
        return this.rootClass;
    }
    
    public boolean isAllowNonStringKeys() {
        return this.allowNonStringKeys;
    }
    
    public boolean isEventTriggeringEnabled() {
        return this.triggerEvents;
    }
    
    public boolean isHandleJettisonEmptyElement() {
        return this.handleJettisonEmptyElement;
    }
    
    public boolean isHandleJettisonSingleElementArray() {
        return this.handleJettisonSingleElementArray;
    }
    
    public boolean isIgnoreDefaultExcludes() {
        return this.ignoreDefaultExcludes;
    }
    
    public boolean isIgnoreJPATransient() {
        return this.ignoreFieldAnnotations.contains("javax.persistence.Transient");
    }
    
    public boolean isIgnoreTransientFields() {
        return this.ignoreTransientFields;
    }
    
    public boolean isIgnorePublicFields() {
        return this.ignorePublicFields;
    }
    
    public boolean isJavascriptCompliant() {
        return this.javascriptCompliant;
    }
    
    public boolean isSkipJavaIdentifierTransformationInMapKeys() {
        return this.skipJavaIdentifierTransformationInMapKeys;
    }
    
    public void registerDefaultValueProcessor(final Class target, final DefaultValueProcessor defaultValueProcessor) {
        if (target != null && defaultValueProcessor != null) {
            this.defaultValueMap.put(target, defaultValueProcessor);
        }
    }
    
    public void registerJavaPropertyNameProcessor(final Class target, final PropertyNameProcessor propertyNameProcessor) {
        if (target != null && propertyNameProcessor != null) {
            this.javaPropertyNameProcessorMap.put(target, propertyNameProcessor);
        }
    }
    
    public void registerJsonBeanProcessor(final Class target, final JsonBeanProcessor jsonBeanProcessor) {
        if (target != null && jsonBeanProcessor != null) {
            this.beanProcessorMap.put(target, jsonBeanProcessor);
        }
    }
    
    public void registerJsonPropertyNameProcessor(final Class target, final PropertyNameProcessor propertyNameProcessor) {
        if (target != null && propertyNameProcessor != null) {
            this.jsonPropertyNameProcessorMap.put(target, propertyNameProcessor);
        }
    }
    
    public void registerJsonValueProcessor(final Class beanClass, final Class propertyType, final JsonValueProcessor jsonValueProcessor) {
        if (beanClass != null && propertyType != null && jsonValueProcessor != null) {
            this.beanTypeMap.put(beanClass, propertyType, jsonValueProcessor);
        }
    }
    
    public void registerJsonValueProcessor(final Class propertyType, final JsonValueProcessor jsonValueProcessor) {
        if (propertyType != null && jsonValueProcessor != null) {
            this.typeMap.put(propertyType, jsonValueProcessor);
        }
    }
    
    public void registerJsonValueProcessor(final Class beanClass, final String key, final JsonValueProcessor jsonValueProcessor) {
        if (beanClass != null && key != null && jsonValueProcessor != null) {
            this.beanKeyMap.put(beanClass, key, jsonValueProcessor);
        }
    }
    
    public void registerJsonValueProcessor(final String key, final JsonValueProcessor jsonValueProcessor) {
        if (key != null && jsonValueProcessor != null) {
            this.keyMap.put(key, jsonValueProcessor);
        }
    }
    
    public void registerPropertyExclusion(final Class target, final String propertyName) {
        if (target != null && propertyName != null) {
            Set set = this.exclusionMap.get(target);
            if (set == null) {
                set = new HashSet();
                this.exclusionMap.put(target, set);
            }
            if (!set.contains(propertyName)) {
                set.add(propertyName);
            }
        }
    }
    
    public void registerPropertyExclusions(final Class target, final String[] properties) {
        if (target != null && properties != null && properties.length > 0) {
            Set set = this.exclusionMap.get(target);
            if (set == null) {
                set = new HashSet();
                this.exclusionMap.put(target, set);
            }
            for (int i = 0; i < properties.length; ++i) {
                if (!set.contains(properties[i])) {
                    set.add(properties[i]);
                }
            }
        }
    }
    
    public void registerPropertyNameProcessor(final Class target, final PropertyNameProcessor propertyNameProcessor) {
        this.registerJavaPropertyNameProcessor(target, propertyNameProcessor);
    }
    
    public synchronized void removeJsonEventListener(final JsonEventListener listener) {
        this.eventListeners.remove(listener);
    }
    
    public void reset() {
        this.excludes = JsonConfig.EMPTY_EXCLUDES;
        this.ignoreDefaultExcludes = false;
        this.ignoreTransientFields = false;
        this.ignorePublicFields = true;
        this.javascriptCompliant = false;
        this.javaIdentifierTransformer = JsonConfig.DEFAULT_JAVA_IDENTIFIER_TRANSFORMER;
        this.cycleDetectionStrategy = JsonConfig.DEFAULT_CYCLE_DETECTION_STRATEGY;
        this.skipJavaIdentifierTransformationInMapKeys = false;
        this.triggerEvents = false;
        this.handleJettisonEmptyElement = false;
        this.handleJettisonSingleElementArray = false;
        this.arrayMode = 1;
        this.rootClass = null;
        this.classMap = null;
        this.keyMap.clear();
        this.typeMap.clear();
        this.beanKeyMap.clear();
        this.beanTypeMap.clear();
        this.jsonPropertyFilter = null;
        this.javaPropertyFilter = null;
        this.jsonBeanProcessorMatcher = JsonConfig.DEFAULT_JSON_BEAN_PROCESSOR_MATCHER;
        this.newBeanInstanceStrategy = JsonConfig.DEFAULT_NEW_BEAN_INSTANCE_STRATEGY;
        this.defaultValueProcessorMatcher = JsonConfig.DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER;
        this.defaultValueMap.clear();
        this.propertySetStrategy = null;
        this.collectionType = JsonConfig.DEFAULT_COLLECTION_TYPE;
        this.enclosedType = null;
        this.jsonValueProcessorMatcher = JsonConfig.DEFAULT_JSON_VALUE_PROCESSOR_MATCHER;
        this.javaPropertyNameProcessorMap.clear();
        this.javaPropertyNameProcessorMatcher = JsonConfig.DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;
        this.jsonPropertyNameProcessorMap.clear();
        this.jsonPropertyNameProcessorMatcher = JsonConfig.DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;
        this.beanProcessorMap.clear();
        this.propertyExclusionClassMatcher = JsonConfig.DEFAULT_PROPERTY_EXCLUSION_CLASS_MATCHER;
        this.exclusionMap.clear();
        this.ignoreFieldAnnotations.clear();
        this.allowNonStringKeys = false;
    }
    
    public void setAllowNonStringKeys(final boolean allowNonStringKeys) {
        this.allowNonStringKeys = allowNonStringKeys;
    }
    
    public void setArrayMode(final int arrayMode) {
        if (arrayMode == 2) {
            this.arrayMode = arrayMode;
        }
        else if (arrayMode == 2) {
            this.arrayMode = arrayMode;
            this.collectionType = Set.class;
        }
        else {
            this.arrayMode = 1;
            this.enclosedType = JsonConfig.DEFAULT_COLLECTION_TYPE;
        }
    }
    
    public void setClassMap(final Map classMap) {
        this.classMap = classMap;
    }
    
    public void setCollectionType(Class collectionType) {
        if (collectionType != null) {
            if (!Collection.class.isAssignableFrom(collectionType)) {
                throw new JSONException("The configured collectionType is not a Collection: " + collectionType.getName());
            }
            this.collectionType = collectionType;
        }
        else {
            collectionType = JsonConfig.DEFAULT_COLLECTION_TYPE;
        }
    }
    
    public void setCycleDetectionStrategy(final CycleDetectionStrategy cycleDetectionStrategy) {
        this.cycleDetectionStrategy = ((cycleDetectionStrategy == null) ? JsonConfig.DEFAULT_CYCLE_DETECTION_STRATEGY : cycleDetectionStrategy);
    }
    
    public void setDefaultValueProcessorMatcher(final DefaultValueProcessorMatcher defaultValueProcessorMatcher) {
        this.defaultValueProcessorMatcher = ((defaultValueProcessorMatcher == null) ? JsonConfig.DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER : defaultValueProcessorMatcher);
    }
    
    public void setEnclosedType(final Class enclosedType) {
        this.enclosedType = enclosedType;
    }
    
    public void setExcludes(final String[] excludes) {
        this.excludes = ((excludes == null) ? JsonConfig.EMPTY_EXCLUDES : excludes);
    }
    
    public void setHandleJettisonEmptyElement(final boolean handleJettisonEmptyElement) {
        this.handleJettisonEmptyElement = handleJettisonEmptyElement;
    }
    
    public void setHandleJettisonSingleElementArray(final boolean handleJettisonSingleElementArray) {
        this.handleJettisonSingleElementArray = handleJettisonSingleElementArray;
    }
    
    public void setIgnoreDefaultExcludes(final boolean ignoreDefaultExcludes) {
        this.ignoreDefaultExcludes = ignoreDefaultExcludes;
    }
    
    public void setIgnoreJPATransient(final boolean ignoreJPATransient) {
        if (ignoreJPATransient) {
            this.addIgnoreFieldAnnotation("javax.persistence.Transient");
        }
        else {
            this.removeIgnoreFieldAnnotation("javax.persistence.Transient");
        }
    }
    
    public void addIgnoreFieldAnnotation(final String annotationClassName) {
        if (annotationClassName != null && !this.ignoreFieldAnnotations.contains(annotationClassName)) {
            this.ignoreFieldAnnotations.add(annotationClassName);
        }
    }
    
    public void removeIgnoreFieldAnnotation(final String annotationClassName) {
        if (annotationClassName != null) {
            this.ignoreFieldAnnotations.remove(annotationClassName);
        }
    }
    
    public void addIgnoreFieldAnnotation(final Class annotationClass) {
        if (annotationClass != null && !this.ignoreFieldAnnotations.contains(annotationClass.getName())) {
            this.ignoreFieldAnnotations.add(annotationClass.getName());
        }
    }
    
    public void removeIgnoreFieldAnnotation(final Class annotationClass) {
        if (annotationClass != null) {
            this.ignoreFieldAnnotations.remove(annotationClass.getName());
        }
    }
    
    public List getIgnoreFieldAnnotations() {
        return Collections.unmodifiableList((List<?>)this.ignoreFieldAnnotations);
    }
    
    public void setIgnoreTransientFields(final boolean ignoreTransientFields) {
        this.ignoreTransientFields = ignoreTransientFields;
    }
    
    public void setIgnorePublicFields(final boolean ignorePublicFields) {
        this.ignorePublicFields = ignorePublicFields;
    }
    
    public void setJavascriptCompliant(final boolean javascriptCompliant) {
        this.javascriptCompliant = javascriptCompliant;
    }
    
    public void setJavaIdentifierTransformer(final JavaIdentifierTransformer javaIdentifierTransformer) {
        this.javaIdentifierTransformer = ((javaIdentifierTransformer == null) ? JsonConfig.DEFAULT_JAVA_IDENTIFIER_TRANSFORMER : javaIdentifierTransformer);
    }
    
    public void setJavaPropertyFilter(final PropertyFilter javaPropertyFilter) {
        this.javaPropertyFilter = javaPropertyFilter;
    }
    
    public void setJavaPropertyNameProcessorMatcher(final PropertyNameProcessorMatcher propertyNameProcessorMatcher) {
        this.javaPropertyNameProcessorMatcher = ((propertyNameProcessorMatcher == null) ? JsonConfig.DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER : propertyNameProcessorMatcher);
    }
    
    public void setJsonBeanProcessorMatcher(final JsonBeanProcessorMatcher jsonBeanProcessorMatcher) {
        this.jsonBeanProcessorMatcher = ((jsonBeanProcessorMatcher == null) ? JsonConfig.DEFAULT_JSON_BEAN_PROCESSOR_MATCHER : jsonBeanProcessorMatcher);
    }
    
    public void setJsonPropertyFilter(final PropertyFilter jsonPropertyFilter) {
        this.jsonPropertyFilter = jsonPropertyFilter;
    }
    
    public void setJsonPropertyNameProcessorMatcher(final PropertyNameProcessorMatcher propertyNameProcessorMatcher) {
        this.jsonPropertyNameProcessorMatcher = ((propertyNameProcessorMatcher == null) ? JsonConfig.DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER : propertyNameProcessorMatcher);
    }
    
    public void setJsonValueProcessorMatcher(final JsonValueProcessorMatcher jsonValueProcessorMatcher) {
        this.jsonValueProcessorMatcher = ((jsonValueProcessorMatcher == null) ? JsonConfig.DEFAULT_JSON_VALUE_PROCESSOR_MATCHER : jsonValueProcessorMatcher);
    }
    
    public void setNewBeanInstanceStrategy(final NewBeanInstanceStrategy newBeanInstanceStrategy) {
        this.newBeanInstanceStrategy = ((newBeanInstanceStrategy == null) ? JsonConfig.DEFAULT_NEW_BEAN_INSTANCE_STRATEGY : newBeanInstanceStrategy);
    }
    
    public void setPropertyExclusionClassMatcher(final PropertyExclusionClassMatcher propertyExclusionClassMatcher) {
        this.propertyExclusionClassMatcher = ((propertyExclusionClassMatcher == null) ? JsonConfig.DEFAULT_PROPERTY_EXCLUSION_CLASS_MATCHER : propertyExclusionClassMatcher);
    }
    
    public void setPropertyNameProcessorMatcher(final PropertyNameProcessorMatcher propertyNameProcessorMatcher) {
        this.setJavaPropertyNameProcessorMatcher(propertyNameProcessorMatcher);
    }
    
    public void setPropertySetStrategy(final PropertySetStrategy propertySetStrategy) {
        this.propertySetStrategy = propertySetStrategy;
    }
    
    public void setRootClass(final Class rootClass) {
        this.rootClass = rootClass;
    }
    
    public void setSkipJavaIdentifierTransformationInMapKeys(final boolean skipJavaIdentifierTransformationInMapKeys) {
        this.skipJavaIdentifierTransformationInMapKeys = skipJavaIdentifierTransformationInMapKeys;
    }
    
    public void unregisterDefaultValueProcessor(final Class target) {
        if (target != null) {
            this.defaultValueMap.remove(target);
        }
    }
    
    public void unregisterJavaPropertyNameProcessor(final Class target) {
        if (target != null) {
            this.javaPropertyNameProcessorMap.remove(target);
        }
    }
    
    public void unregisterJsonBeanProcessor(final Class target) {
        if (target != null) {
            this.beanProcessorMap.remove(target);
        }
    }
    
    public void unregisterJsonPropertyNameProcessor(final Class target) {
        if (target != null) {
            this.jsonPropertyNameProcessorMap.remove(target);
        }
    }
    
    public void unregisterJsonValueProcessor(final Class propertyType) {
        if (propertyType != null) {
            this.typeMap.remove(propertyType);
        }
    }
    
    public void unregisterJsonValueProcessor(final Class beanClass, final Class propertyType) {
        if (beanClass != null && propertyType != null) {
            this.beanTypeMap.remove((Object)beanClass, (Object)propertyType);
        }
    }
    
    public void unregisterJsonValueProcessor(final Class beanClass, final String key) {
        if (beanClass != null && key != null) {
            this.beanKeyMap.remove((Object)beanClass, (Object)key);
        }
    }
    
    public void unregisterJsonValueProcessor(final String key) {
        if (key != null) {
            this.keyMap.remove(key);
        }
    }
    
    public void unregisterPropertyExclusion(final Class target, final String propertyName) {
        if (target != null && propertyName != null) {
            Set set = this.exclusionMap.get(target);
            if (set == null) {
                set = new HashSet();
                this.exclusionMap.put(target, set);
            }
            set.remove(propertyName);
        }
    }
    
    public void unregisterPropertyExclusions(final Class target) {
        if (target != null) {
            final Set set = this.exclusionMap.get(target);
            if (set != null) {
                set.clear();
            }
        }
    }
    
    public void unregisterPropertyNameProcessor(final Class target) {
        this.unregisterJavaPropertyNameProcessor(target);
    }
    
    static {
        DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER = DefaultValueProcessorMatcher.DEFAULT;
        DEFAULT_JSON_BEAN_PROCESSOR_MATCHER = JsonBeanProcessorMatcher.DEFAULT;
        DEFAULT_JSON_VALUE_PROCESSOR_MATCHER = JsonValueProcessorMatcher.DEFAULT;
        DEFAULT_NEW_BEAN_INSTANCE_STRATEGY = NewBeanInstanceStrategy.DEFAULT;
        DEFAULT_PROPERTY_EXCLUSION_CLASS_MATCHER = PropertyExclusionClassMatcher.DEFAULT;
        DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER = PropertyNameProcessorMatcher.DEFAULT;
        DEFAULT_COLLECTION_TYPE = List.class;
        DEFAULT_CYCLE_DETECTION_STRATEGY = CycleDetectionStrategy.STRICT;
        DEFAULT_EXCLUDES = new String[] { "class", "declaringClass", "metaClass" };
        DEFAULT_JAVA_IDENTIFIER_TRANSFORMER = JavaIdentifierTransformer.NOOP;
        DEFAULT_VALUE_PROCESSOR = new DefaultDefaultValueProcessor();
        EMPTY_EXCLUDES = new String[0];
    }
}
