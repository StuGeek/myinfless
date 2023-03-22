// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.groovy;

import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import groovy.lang.MetaClass;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import java.util.List;
import java.util.Map;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GeneratedClosure;
import groovy.lang.Closure;

class GJson$_enhanceJSONObject_closure11 extends Closure implements GeneratedClosure
{
    private static final /* synthetic */ Integer $const$0;
    private static final /* synthetic */ Integer $const$1;
    private static final /* synthetic */ Integer $const$2;
    private static /* synthetic */ SoftReference $callSiteArray;
    
    public GJson$_enhanceJSONObject_closure11(final Object _outerInstance, final Object _thisObject) {
        $getCallSiteArray();
        super(_outerInstance, _thisObject);
    }
    
    public Object doCall(final Object o) {
        final Object args = new Reference(o);
        final CallSite[] $getCallSiteArray = $getCallSiteArray();
        if (((Reference)args).get() instanceof Map) {
            $getCallSiteArray[0].call($getCallSiteArray[1].callGroovyObjectGetProperty((Object)this), ((Reference)args).get());
            return $getCallSiteArray[2].callGroovyObjectGetProperty((Object)this);
        }
        if (!DefaultTypeTransformation.booleanUnbox((Object)((((Reference)args).get() instanceof List && ScriptBytecodeAdapter.compareGreaterThanEqual($getCallSiteArray[3].call(((Reference)args).get()), (Object)GJson$_enhanceJSONObject_closure11.$const$0)) ? Boolean.TRUE : Boolean.FALSE))) {
            return null;
        }
        final Object key = new Reference($getCallSiteArray[4].call(((Reference)args).get(), (Object)GJson$_enhanceJSONObject_closure11.$const$1));
        if (ScriptBytecodeAdapter.compareEqual($getCallSiteArray[5].call(((Reference)args).get()), (Object)GJson$_enhanceJSONObject_closure11.$const$2)) {
            return $getCallSiteArray[6].call($getCallSiteArray[7].callGroovyObjectGetProperty((Object)this), ((Reference)key).get(), $getCallSiteArray[8].call(((Reference)args).get(), (Object)GJson$_enhanceJSONObject_closure11.$const$1));
        }
        return $getCallSiteArray[9].call($getCallSiteArray[10].callGroovyObjectGetProperty((Object)this), ((Reference)key).get(), ((Reference)args).get());
    }
    
    static {
        $const$2 = 1;
        $const$1 = 0;
        $const$0 = 2;
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[11];
        $createCallSiteArray_1(array);
        return new CallSiteArray($get$$class$net$sf$json$groovy$GJson$_enhanceJSONObject_closure11(), array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (GJson$_enhanceJSONObject_closure11.$callSiteArray == null || ($createCallSiteArray = GJson$_enhanceJSONObject_closure11.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            GJson$_enhanceJSONObject_closure11.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
}
