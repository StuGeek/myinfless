// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.groovy;

import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import groovy.lang.MetaClass;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GeneratedClosure;
import groovy.lang.Closure;

class GJson$_enhanceMap_closure7 extends Closure implements GeneratedClosure
{
    private static /* synthetic */ SoftReference $callSiteArray;
    private static /* synthetic */ Class $class$java$lang$Object;
    
    public GJson$_enhanceMap_closure7(final Object _outerInstance, final Object _thisObject) {
        $getCallSiteArray();
        super(_outerInstance, _thisObject);
    }
    
    public Object doCall(final Object o) {
        final Object it = new Reference(o);
        return ScriptBytecodeAdapter.compareEqual($getCallSiteArray()[0].callGetProperty(((Reference)it).get()), (Object)"isJsonEnhanced") ? Boolean.TRUE : Boolean.FALSE;
    }
    
    public Object doCall() {
        return $getCallSiteArray()[1].callCurrent((GroovyObject)this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
    }
    
    private static /* synthetic */ CallSiteArray $createCallSiteArray() {
        final String[] array = new String[2];
        $createCallSiteArray_1(array);
        return new CallSiteArray($get$$class$net$sf$json$groovy$GJson$_enhanceMap_closure7(), array);
    }
    
    private static /* synthetic */ CallSite[] $getCallSiteArray() {
        CallSiteArray $createCallSiteArray;
        if (GJson$_enhanceMap_closure7.$callSiteArray == null || ($createCallSiteArray = GJson$_enhanceMap_closure7.$callSiteArray.get()) == null) {
            $createCallSiteArray = $createCallSiteArray();
            GJson$_enhanceMap_closure7.$callSiteArray = new SoftReference($createCallSiteArray);
        }
        return $createCallSiteArray.array;
    }
    
    private static /* synthetic */ Class $get$$class$java$lang$Object() {
        Class $class$java$lang$Object;
        if (($class$java$lang$Object = GJson$_enhanceMap_closure7.$class$java$lang$Object) == null) {
            $class$java$lang$Object = (GJson$_enhanceMap_closure7.$class$java$lang$Object = class$("java.lang.Object"));
        }
        return $class$java$lang$Object;
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
