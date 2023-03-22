// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.groovy;

import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import groovy.lang.MetaClass;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GeneratedClosure;
import groovy.lang.Closure;

class GJson$_enhanceJSONObject_closure12 extends Closure implements GeneratedClosure
{
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public GJson$_enhanceJSONObject_closure12(final Object _outerInstance, final Object _thisObject) {
        $getCallSiteArray();
        super(_outerInstance, _thisObject);
    }
    
    public Object doCall(final String s, final Object o) {
        final String key = (String)new Reference((Object)s);
        final Object defaultValue = new Reference(o);
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        final Object previousValue = new Reference($getCallSiteArray[0].call($getCallSiteArray[1].callGroovyObjectGetProperty((Object)this), ((Reference)key).get()));
        if (!DefaultTypeTransformation.booleanUnbox(((Reference)previousValue).get())) {
            $getCallSiteArray[2].call($getCallSiteArray[3].callGroovyObjectGetProperty((Object)this), ((Reference)key).get(), ((Reference)defaultValue).get());
            ((Reference)previousValue).set($getCallSiteArray[4].call($getCallSiteArray[5].callGroovyObjectGetProperty((Object)this), ((Reference)key).get()));
        }
        return ((Reference)previousValue).get();
    }
    
    public Object call(final String s, final Object o) {
        final String key = (String)new Reference((Object)s);
        final Object defaultValue = new Reference(o);
        return $getCallSiteArray()[6].callCurrent((GroovyObject)this, ((Reference)key).get(), ((Reference)defaultValue).get());
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[7];
        $createCallSiteArray_1(array);
        return new CallSiteArray($get$$class$net$sf$json$groovy$GJson$_enhanceJSONObject_closure12(), array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (GJson$_enhanceJSONObject_closure12.$callSiteArray == null || ($createCallSiteArray = GJson$_enhanceJSONObject_closure12.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            GJson$_enhanceJSONObject_closure12.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
}
