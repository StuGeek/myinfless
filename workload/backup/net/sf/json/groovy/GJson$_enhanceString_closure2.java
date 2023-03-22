// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.groovy;

import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import groovy.lang.MetaClass;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import groovy.lang.Reference;
import org.codehaus.groovy.runtime.GeneratedClosure;
import groovy.lang.Closure;

class GJson$_enhanceString_closure2 extends Closure implements GeneratedClosure
{
    private Reference<Object> asType;
    private static /* synthetic */ SoftReference $callSiteArray;
    private static /* synthetic */ Class $class$net$sf$json$JSONArray;
    private static /* synthetic */ Class $class$net$sf$json$JSONFunction;
    private static /* synthetic */ Class $class$net$sf$json$JSONSerializer;
    private static /* synthetic */ Class $class$net$sf$json$JSON;
    private static /* synthetic */ Class $class$net$sf$json$JSONObject;
    
    public GJson$_enhanceString_closure2(final Object _outerInstance, final Object _thisObject, final Reference<Object> asType) {
        $getCallSiteArray();
        super(_outerInstance, _thisObject);
        this.asType = asType;
    }
    
    public Object doCall(final Class clazz2) {
        final Class clazz = (Class)new Reference((Object)clazz2);
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        final Object value = ((Reference)clazz).get();
        if (ScriptBytecodeAdapter.isCase(value, (Object)$get$$class$net$sf$json$JSON())) {
            return $getCallSiteArray[0].call((Object)$get$$class$net$sf$json$JSONSerializer(), $getCallSiteArray[1].callGroovyObjectGetProperty((Object)this));
        }
        if (ScriptBytecodeAdapter.isCase(value, (Object)$get$$class$net$sf$json$JSONArray())) {
            return $getCallSiteArray[2].call((Object)$get$$class$net$sf$json$JSONArray(), $getCallSiteArray[3].callGroovyObjectGetProperty((Object)this));
        }
        if (ScriptBytecodeAdapter.isCase(value, (Object)$get$$class$net$sf$json$JSONObject())) {
            return $getCallSiteArray[4].call((Object)$get$$class$net$sf$json$JSONObject(), $getCallSiteArray[5].callGroovyObjectGetProperty((Object)this));
        }
        if (ScriptBytecodeAdapter.isCase(value, (Object)$get$$class$net$sf$json$JSONFunction())) {
            return $getCallSiteArray[6].call((Object)$get$$class$net$sf$json$JSONFunction(), $getCallSiteArray[7].callGroovyObjectGetProperty((Object)this));
        }
        return $getCallSiteArray[8].call(this.asType.get(), ((Reference)clazz).get());
    }
    
    public Object call(final Class clazz2) {
        final Class clazz = (Class)new Reference((Object)clazz2);
        return $getCallSiteArray()[9].callCurrent((GroovyObject)this, ((Reference)clazz).get());
    }
    
    public Object getAsType() {
        $getCallSiteArray();
        return this.asType.get();
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[10];
        $createCallSiteArray_1(array);
        return new CallSiteArray($get$$class$net$sf$json$groovy$GJson$_enhanceString_closure2(), array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (GJson$_enhanceString_closure2.$callSiteArray == null || ($createCallSiteArray = GJson$_enhanceString_closure2.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            GJson$_enhanceString_closure2.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
    
    private static /* synthetic */ Class $get$$class$net$sf$json$JSONArray() {
        Class $class$net$sf$json$JSONArray;
        if (($class$net$sf$json$JSONArray = GJson$_enhanceString_closure2.$class$net$sf$json$JSONArray) == null) {
            $class$net$sf$json$JSONArray = (GJson$_enhanceString_closure2.$class$net$sf$json$JSONArray = class$("net.sf.json.JSONArray"));
        }
        return $class$net$sf$json$JSONArray;
    }
    
    private static /* synthetic */ Class $get$$class$net$sf$json$JSONFunction() {
        Class $class$net$sf$json$JSONFunction;
        if (($class$net$sf$json$JSONFunction = GJson$_enhanceString_closure2.$class$net$sf$json$JSONFunction) == null) {
            $class$net$sf$json$JSONFunction = (GJson$_enhanceString_closure2.$class$net$sf$json$JSONFunction = class$("net.sf.json.JSONFunction"));
        }
        return $class$net$sf$json$JSONFunction;
    }
    
    private static /* synthetic */ Class $get$$class$net$sf$json$JSONSerializer() {
        Class $class$net$sf$json$JSONSerializer;
        if (($class$net$sf$json$JSONSerializer = GJson$_enhanceString_closure2.$class$net$sf$json$JSONSerializer) == null) {
            $class$net$sf$json$JSONSerializer = (GJson$_enhanceString_closure2.$class$net$sf$json$JSONSerializer = class$("net.sf.json.JSONSerializer"));
        }
        return $class$net$sf$json$JSONSerializer;
    }
    
    private static /* synthetic */ Class $get$$class$net$sf$json$JSON() {
        Class $class$net$sf$json$JSON;
        if (($class$net$sf$json$JSON = GJson$_enhanceString_closure2.$class$net$sf$json$JSON) == null) {
            $class$net$sf$json$JSON = (GJson$_enhanceString_closure2.$class$net$sf$json$JSON = class$("net.sf.json.JSON"));
        }
        return $class$net$sf$json$JSON;
    }
    
    private static /* synthetic */ Class $get$$class$net$sf$json$JSONObject() {
        Class $class$net$sf$json$JSONObject;
        if (($class$net$sf$json$JSONObject = GJson$_enhanceString_closure2.$class$net$sf$json$JSONObject) == null) {
            $class$net$sf$json$JSONObject = (GJson$_enhanceString_closure2.$class$net$sf$json$JSONObject = class$("net.sf.json.JSONObject"));
        }
        return $class$net$sf$json$JSONObject;
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
