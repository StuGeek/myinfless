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

class GJson$_enhanceCollection_closure5 extends Closure implements GeneratedClosure
{
    private Reference<Object> asType;
    private static /* synthetic */ SoftReference $callSiteArray;
    private static /* synthetic */ Class $class$net$sf$json$JSONArray;
    
    public GJson$_enhanceCollection_closure5(final Object _outerInstance, final Object _thisObject, final Reference<Object> asType) {
        $getCallSiteArray();
        super(_outerInstance, _thisObject);
        this.asType = asType;
    }
    
    public Object doCall(final Class clazz2) {
        final Class clazz = (Class)new Reference((Object)clazz2);
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (ScriptBytecodeAdapter.isCase(((Reference)clazz).get(), (Object)$get$$class$net$sf$json$JSONArray())) {
            return $getCallSiteArray[0].call((Object)$get$$class$net$sf$json$JSONArray(), $getCallSiteArray[1].callGroovyObjectGetProperty((Object)this));
        }
        return $getCallSiteArray[2].call(this.asType.get(), ((Reference)clazz).get());
    }
    
    public Object call(final Class clazz2) {
        final Class clazz = (Class)new Reference((Object)clazz2);
        return $getCallSiteArray()[3].callCurrent((GroovyObject)this, ((Reference)clazz).get());
    }
    
    public Object getAsType() {
        $getCallSiteArray();
        return this.asType.get();
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[4];
        $createCallSiteArray_1(array);
        return new CallSiteArray($get$$class$net$sf$json$groovy$GJson$_enhanceCollection_closure5(), array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (GJson$_enhanceCollection_closure5.$callSiteArray == null || ($createCallSiteArray = GJson$_enhanceCollection_closure5.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            GJson$_enhanceCollection_closure5.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
    
    private static /* synthetic */ Class $get$$class$net$sf$json$JSONArray() {
        Class $class$net$sf$json$JSONArray;
        if (($class$net$sf$json$JSONArray = GJson$_enhanceCollection_closure5.$class$net$sf$json$JSONArray) == null) {
            $class$net$sf$json$JSONArray = (GJson$_enhanceCollection_closure5.$class$net$sf$json$JSONArray = class$("net.sf.json.JSONArray"));
        }
        return $class$net$sf$json$JSONArray;
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
