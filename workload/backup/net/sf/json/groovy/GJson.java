// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.groovy;

import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.GStringImpl;
import groovy.lang.Reference;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import java.lang.ref.SoftReference;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import groovy.lang.GroovyObject;

public class GJson implements GroovyObject
{
    private static /* synthetic */ ClassInfo $staticClassInfo;
    private transient /* synthetic */ MetaClass metaClass;
    public static /* synthetic */ Long __timeStamp;
    public static /* synthetic */ Long __timeStamp__239_neverHappen1292322693142;
    private static /* synthetic */ SoftReference $callSiteArray;
    private static /* synthetic */ Class $class$java$util$HashSet;
    private static /* synthetic */ Class $class$groovy$lang$MetaClass;
    private static /* synthetic */ Class $class$groovy$lang$ExpandoMetaClass;
    private static /* synthetic */ Class $class$java$lang$Boolean;
    private static /* synthetic */ Class $class$java$util$ArrayList;
    private static /* synthetic */ Class $class$java$util$Collection;
    private static /* synthetic */ Class $class$java$lang$String;
    private static /* synthetic */ Class $class$java$util$Map;
    private static /* synthetic */ Class $class$net$sf$json$JSONObject;
    private static /* synthetic */ Class $class$net$sf$json$groovy$GJson;
    
    public GJson() {
        $getCallSiteArray();
        this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType((Object)this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
    }
    
    public static void enhanceClasses() {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        final Object s = $getCallSiteArray[0].callStatic($get$$class$net$sf$json$groovy$GJson());
        final Object l = $getCallSiteArray[1].callStatic($get$$class$net$sf$json$groovy$GJson());
        final Object m = $getCallSiteArray[2].callStatic($get$$class$net$sf$json$groovy$GJson());
        final Object j = $getCallSiteArray[3].callStatic($get$$class$net$sf$json$groovy$GJson());
        if (DefaultTypeTransformation.booleanUnbox((Object)((!DefaultTypeTransformation.booleanUnbox((Object)((!DefaultTypeTransformation.booleanUnbox((Object)((!DefaultTypeTransformation.booleanUnbox(s) && !DefaultTypeTransformation.booleanUnbox(l)) ? Boolean.FALSE : Boolean.TRUE)) && !DefaultTypeTransformation.booleanUnbox(m)) ? Boolean.FALSE : Boolean.TRUE)) && !DefaultTypeTransformation.booleanUnbox(j)) ? Boolean.FALSE : Boolean.TRUE))) {
            $getCallSiteArray[4].call((Object)$get$$class$groovy$lang$ExpandoMetaClass());
        }
    }
    
    private static boolean enhanceString() {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (!DefaultTypeTransformation.booleanUnbox($getCallSiteArray[5].call($getCallSiteArray[6].callGetProperty($getCallSiteArray[7].callGetProperty((Object)$get$$class$java$lang$String())), (Object)new GJson$_enhanceString_closure1($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson())))) {
            final Object asType = new Reference((Object)ScriptBytecodeAdapter.getMethodPointer($getCallSiteArray[8].callGetProperty((Object)$get$$class$java$lang$String()), "asType"));
            ScriptBytecodeAdapter.setProperty((Object)new GJson$_enhanceString_closure2($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson(), (Reference<Object>)asType), $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[9].callGetProperty((Object)$get$$class$java$lang$String()), "asType");
            ScriptBytecodeAdapter.setProperty((Object)new GJson$_enhanceString_closure3($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson()), $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[10].callGetProperty((Object)$get$$class$java$lang$String()), "isJsonEnhanced");
            return DefaultTypeTransformation.booleanUnbox((Object)ScriptBytecodeAdapter.castToType((Object)Boolean.TRUE, $get$$class$java$lang$Boolean()));
        }
        return DefaultTypeTransformation.booleanUnbox((Object)ScriptBytecodeAdapter.castToType((Object)Boolean.FALSE, $get$$class$java$lang$Boolean()));
    }
    
    private static boolean enhanceCollection() {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (!DefaultTypeTransformation.booleanUnbox($getCallSiteArray[11].call($getCallSiteArray[12].callGetProperty($getCallSiteArray[13].callGetProperty((Object)$get$$class$java$util$Collection())), (Object)new GJson$_enhanceCollection_closure4($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson())))) {
            final Object asType = new Reference((Object)ScriptBytecodeAdapter.getMethodPointer($getCallSiteArray[14].callGetProperty((Object)$get$$class$java$util$Collection()), "asType"));
            final Object typeConverter = new GJson$_enhanceCollection_closure5($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson(), (Reference<Object>)asType);
            ScriptBytecodeAdapter.setProperty(typeConverter, $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[15].callGetProperty((Object)$get$$class$java$util$ArrayList()), "asType");
            ScriptBytecodeAdapter.setProperty(typeConverter, $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[16].callGetProperty((Object)$get$$class$java$util$HashSet()), "asType");
            ScriptBytecodeAdapter.setProperty((Object)new GJson$_enhanceCollection_closure6($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson()), $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[17].callGetProperty((Object)$get$$class$java$util$Collection()), "isJsonEnhanced");
            return DefaultTypeTransformation.booleanUnbox((Object)ScriptBytecodeAdapter.castToType((Object)Boolean.TRUE, $get$$class$java$lang$Boolean()));
        }
        return DefaultTypeTransformation.booleanUnbox((Object)ScriptBytecodeAdapter.castToType((Object)Boolean.FALSE, $get$$class$java$lang$Boolean()));
    }
    
    private static boolean enhanceMap() {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (!DefaultTypeTransformation.booleanUnbox($getCallSiteArray[18].call($getCallSiteArray[19].callGetProperty($getCallSiteArray[20].callGetProperty((Object)$get$$class$java$util$Map())), (Object)new GJson$_enhanceMap_closure7($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson())))) {
            final Object asType = new Reference((Object)ScriptBytecodeAdapter.getMethodPointer($getCallSiteArray[21].callGetProperty((Object)$get$$class$java$util$Map()), "asType"));
            ScriptBytecodeAdapter.setProperty((Object)new GJson$_enhanceMap_closure8($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson(), (Reference<Object>)asType), $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[22].callGetProperty((Object)$get$$class$java$util$Map()), "asType");
            ScriptBytecodeAdapter.setProperty((Object)new GJson$_enhanceMap_closure9($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson()), $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[23].callGetProperty((Object)$get$$class$java$util$Map()), "isJsonEnhanced");
            return DefaultTypeTransformation.booleanUnbox((Object)ScriptBytecodeAdapter.castToType((Object)Boolean.TRUE, $get$$class$java$lang$Boolean()));
        }
        return DefaultTypeTransformation.booleanUnbox((Object)ScriptBytecodeAdapter.castToType((Object)Boolean.FALSE, $get$$class$java$lang$Boolean()));
    }
    
    private static boolean enhanceJSONObject() {
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (!DefaultTypeTransformation.booleanUnbox($getCallSiteArray[24].call($getCallSiteArray[25].callGetProperty($getCallSiteArray[26].callGetProperty((Object)$get$$class$net$sf$json$JSONObject())), (Object)new GJson$_enhanceJSONObject_closure10($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson())))) {
            ScriptBytecodeAdapter.setProperty((Object)new GJson$_enhanceJSONObject_closure11($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson()), $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[27].callGetProperty((Object)$get$$class$net$sf$json$JSONObject()), "leftShift");
            ScriptBytecodeAdapter.setProperty((Object)new GJson$_enhanceJSONObject_closure12($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson()), $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[28].callGetProperty((Object)$get$$class$net$sf$json$JSONObject()), "get");
            ScriptBytecodeAdapter.setProperty((Object)new GJson$_enhanceJSONObject_closure13($get$$class$net$sf$json$groovy$GJson(), $get$$class$net$sf$json$groovy$GJson()), $get$$class$net$sf$json$groovy$GJson(), $getCallSiteArray[29].callGetProperty((Object)$get$$class$net$sf$json$JSONObject()), "isJsonEnhanced");
            return DefaultTypeTransformation.booleanUnbox((Object)ScriptBytecodeAdapter.castToType((Object)Boolean.TRUE, $get$$class$java$lang$Boolean()));
        }
        return DefaultTypeTransformation.booleanUnbox((Object)ScriptBytecodeAdapter.castToType((Object)Boolean.FALSE, $get$$class$java$lang$Boolean()));
    }
    
    protected /* synthetic */ MetaClass $getStaticMetaClass() {
        if (this.getClass() == $get$$class$net$sf$json$groovy$GJson()) {
            return ScriptBytecodeAdapter.initMetaClass((Object)this);
        }
        ClassInfo $staticClassInfo = GJson.$staticClassInfo;
        if ($staticClassInfo == null) {
            $staticClassInfo = (GJson.$staticClassInfo = ClassInfo.getClassInfo((Class)this.getClass()));
        }
        return $staticClassInfo.getMetaClass();
    }
    
    static {
        GJson.__timeStamp__239_neverHappen1292322693142 = 0L;
        GJson.__timeStamp = 1292322693142L;
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[30];
        $createCallSiteArray_1(array);
        return new CallSiteArray($get$$class$net$sf$json$groovy$GJson(), array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (GJson.$callSiteArray == null || ($createCallSiteArray = GJson.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            GJson.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
    
    private static /* synthetic */ Class $get$$class$java$util$HashSet() {
        Class $class$java$util$HashSet;
        if (($class$java$util$HashSet = GJson.$class$java$util$HashSet) == null) {
            $class$java$util$HashSet = (GJson.$class$java$util$HashSet = class$("java.util.HashSet"));
        }
        return $class$java$util$HashSet;
    }
    
    private static /* synthetic */ Class $get$$class$groovy$lang$MetaClass() {
        Class $class$groovy$lang$MetaClass;
        if (($class$groovy$lang$MetaClass = GJson.$class$groovy$lang$MetaClass) == null) {
            $class$groovy$lang$MetaClass = (GJson.$class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass"));
        }
        return $class$groovy$lang$MetaClass;
    }
    
    private static /* synthetic */ Class $get$$class$groovy$lang$ExpandoMetaClass() {
        Class $class$groovy$lang$ExpandoMetaClass;
        if (($class$groovy$lang$ExpandoMetaClass = GJson.$class$groovy$lang$ExpandoMetaClass) == null) {
            $class$groovy$lang$ExpandoMetaClass = (GJson.$class$groovy$lang$ExpandoMetaClass = class$("groovy.lang.ExpandoMetaClass"));
        }
        return $class$groovy$lang$ExpandoMetaClass;
    }
    
    private static /* synthetic */ Class $get$$class$java$lang$Boolean() {
        Class $class$java$lang$Boolean;
        if (($class$java$lang$Boolean = GJson.$class$java$lang$Boolean) == null) {
            $class$java$lang$Boolean = (GJson.$class$java$lang$Boolean = class$("java.lang.Boolean"));
        }
        return $class$java$lang$Boolean;
    }
    
    private static /* synthetic */ Class $get$$class$java$util$ArrayList() {
        Class $class$java$util$ArrayList;
        if (($class$java$util$ArrayList = GJson.$class$java$util$ArrayList) == null) {
            $class$java$util$ArrayList = (GJson.$class$java$util$ArrayList = class$("java.util.ArrayList"));
        }
        return $class$java$util$ArrayList;
    }
    
    private static /* synthetic */ Class $get$$class$java$util$Collection() {
        Class $class$java$util$Collection;
        if (($class$java$util$Collection = GJson.$class$java$util$Collection) == null) {
            $class$java$util$Collection = (GJson.$class$java$util$Collection = class$("java.util.Collection"));
        }
        return $class$java$util$Collection;
    }
    
    private static /* synthetic */ Class $get$$class$java$lang$String() {
        Class $class$java$lang$String;
        if (($class$java$lang$String = GJson.$class$java$lang$String) == null) {
            $class$java$lang$String = (GJson.$class$java$lang$String = class$("java.lang.String"));
        }
        return $class$java$lang$String;
    }
    
    private static /* synthetic */ Class $get$$class$java$util$Map() {
        Class $class$java$util$Map;
        if (($class$java$util$Map = GJson.$class$java$util$Map) == null) {
            $class$java$util$Map = (GJson.$class$java$util$Map = class$("java.util.Map"));
        }
        return $class$java$util$Map;
    }
    
    private static /* synthetic */ Class $get$$class$net$sf$json$JSONObject() {
        Class $class$net$sf$json$JSONObject;
        if (($class$net$sf$json$JSONObject = GJson.$class$net$sf$json$JSONObject) == null) {
            $class$net$sf$json$JSONObject = (GJson.$class$net$sf$json$JSONObject = class$("net.sf.json.JSONObject"));
        }
        return $class$net$sf$json$JSONObject;
    }
    
    private static /* synthetic */ Class $get$$class$net$sf$json$groovy$GJson() {
        Class $class$net$sf$json$groovy$GJson;
        if (($class$net$sf$json$groovy$GJson = GJson.$class$net$sf$json$groovy$GJson) == null) {
            $class$net$sf$json$groovy$GJson = (GJson.$class$net$sf$json$groovy$GJson = class$("net.sf.json.groovy.GJson"));
        }
        return $class$net$sf$json$groovy$GJson;
    }
    
    static /* synthetic */ Class class$(final String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
}
