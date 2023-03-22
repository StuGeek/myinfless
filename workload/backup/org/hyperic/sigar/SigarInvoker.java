// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.Arrays;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;
import java.util.HashMap;

public class SigarInvoker
{
    private static HashMap attrCache;
    private static HashMap compatTypes;
    private static HashMap compatAttrs;
    private static final Class[] VOID_SIGNATURE;
    private Class[] ARG_SIGNATURE;
    private Class[] ARG2_SIGNATURE;
    private static final Object[] VOID_ARGS;
    private Object[] ARG_ARGS;
    private String type;
    private boolean typeIsArray;
    private int arrayIdx;
    private boolean hasArrayIdx;
    private int typeArrayType;
    private static final int ARRAY_TYPE_OBJECT = 1;
    private static final int ARRAY_TYPE_DOUBLE = 2;
    private static final int ARRAY_TYPE_LONG = 3;
    private Method typeMethod;
    private SigarProxy sigarProxy;
    private SigarProxyCache handler;
    
    protected SigarInvoker() {
        this.ARG_SIGNATURE = new Class[] { String.class };
        this.ARG2_SIGNATURE = new Class[] { String.class, String.class };
        this.ARG_ARGS = new Object[1];
        this.type = null;
        this.typeIsArray = false;
        this.arrayIdx = -1;
        this.hasArrayIdx = false;
    }
    
    public SigarInvoker(final SigarProxy proxy, final String type) {
        this.ARG_SIGNATURE = new Class[] { String.class };
        this.ARG2_SIGNATURE = new Class[] { String.class, String.class };
        this.ARG_ARGS = new Object[1];
        this.type = null;
        this.typeIsArray = false;
        this.arrayIdx = -1;
        this.hasArrayIdx = false;
        this.setProxy(proxy);
        this.setType(type);
    }
    
    protected void setProxy(final SigarProxy proxy) {
        try {
            this.handler = (SigarProxyCache)Proxy.getInvocationHandler(proxy);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.sigarProxy = proxy;
    }
    
    protected void setType(String val) {
        final String alias = SigarInvoker.compatTypes.get(val);
        if (alias != null) {
            val = alias;
        }
        this.type = val;
    }
    
    public String getType() {
        return this.type;
    }
    
    private int getAttributeIndex(final String attr) {
        try {
            return Integer.valueOf(attr);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private Method getTypeMethod(final Object[] args) throws SigarException {
        if (this.typeMethod == null) {
            Class[] sig = SigarInvoker.VOID_SIGNATURE;
            boolean argIsArrayIdx = false;
            int argLength = 0;
            final String getter = "get" + this.getType();
            if (args != null) {
                argLength = args.length;
                switch (argLength) {
                    case 1: {
                        sig = this.ARG_SIGNATURE;
                        break;
                    }
                    case 2: {
                        sig = this.ARG2_SIGNATURE;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException();
                    }
                }
            }
            try {
                this.typeMethod = Sigar.class.getMethod(getter, (Class[])sig);
            }
            catch (Exception e) {
                try {
                    this.typeMethod = Sigar.class.getMethod(getter, (Class[])SigarInvoker.VOID_SIGNATURE);
                    if (argLength == 1) {
                        argIsArrayIdx = true;
                    }
                }
                catch (Exception e2) {
                    final String msg = "Unable to determine getter for " + this.type;
                    throw new SigarException(msg);
                }
            }
            final Class typeClass = this.typeMethod.getReturnType();
            if (typeClass.isArray()) {
                this.typeIsArray = true;
                if (argIsArrayIdx) {
                    try {
                        this.arrayIdx = Integer.parseInt((String)args[0]);
                    }
                    catch (NumberFormatException ne) {
                        final String msg = this.getType() + ": '" + args[0] + "' is not a number";
                        throw new SigarException(msg);
                    }
                    this.hasArrayIdx = true;
                }
                final Class componentClass = typeClass.getComponentType();
                if (componentClass.isPrimitive()) {
                    if (componentClass == Double.TYPE) {
                        this.typeArrayType = 2;
                    }
                    else {
                        if (componentClass != Long.TYPE) {
                            throw new SigarException("unsupported array type: " + componentClass.getName());
                        }
                        this.typeArrayType = 3;
                    }
                }
                else {
                    this.typeArrayType = 1;
                }
            }
            else {
                this.typeIsArray = false;
            }
        }
        return this.typeMethod;
    }
    
    public Object invoke(final Object arg, final String attr) throws SigarException, SigarNotImplementedException, SigarPermissionDeniedException {
        Object[] args = null;
        if (arg != null) {
            args = this.ARG_ARGS;
            args[0] = arg;
        }
        return this.invoke(args, attr);
    }
    
    private String aobMsg(final int idx, final int length) {
        return "Array index " + idx + " out of bounds " + length;
    }
    
    public Object invoke(Object[] args, final String attr) throws SigarException, SigarNotImplementedException, SigarPermissionDeniedException {
        final Method typeGetter = this.getTypeMethod(args);
        if (this.hasArrayIdx) {
            args = null;
        }
        Object typeObject;
        try {
            typeObject = this.handler.invoke(this.sigarProxy, typeGetter, args);
        }
        catch (Throwable t) {
            final String parms = (args == null) ? "" : Arrays.asList(args).toString();
            final String msg = "Failed to invoke " + typeGetter.getName() + parms + ": " + t.getMessage();
            if (t instanceof SigarNotImplementedException) {
                throw (SigarNotImplementedException)t;
            }
            if (t instanceof SigarPermissionDeniedException) {
                throw (SigarPermissionDeniedException)t;
            }
            throw new SigarException(msg);
        }
        if (attr == null) {
            return typeObject;
        }
        if (this.typeIsArray) {
            if (this.hasArrayIdx) {
                final Object[] array = (Object[])typeObject;
                if (this.arrayIdx >= array.length) {
                    throw new SigarException(this.aobMsg(this.arrayIdx, array.length));
                }
                typeObject = array[this.arrayIdx];
            }
            else {
                final int idx = this.getAttributeIndex(attr);
                if (idx < 0) {
                    throw new SigarException("Invalid array index: " + attr);
                }
                switch (this.typeArrayType) {
                    case 2: {
                        final double[] d_array = (double[])typeObject;
                        if (idx >= d_array.length) {
                            throw new SigarException(this.aobMsg(idx, d_array.length));
                        }
                        return new Double(d_array[idx]);
                    }
                    case 3: {
                        final long[] l_array = (long[])typeObject;
                        if (idx >= l_array.length) {
                            throw new SigarException(this.aobMsg(idx, l_array.length));
                        }
                        return new Long(l_array[idx]);
                    }
                    case 1: {
                        final Object[] o_array = (Object[])typeObject;
                        if (idx >= o_array.length) {
                            throw new SigarException(this.aobMsg(idx, o_array.length));
                        }
                        return o_array[idx];
                    }
                }
            }
        }
        final Method attrGetter = this.getAttributeMethod(attr);
        try {
            return attrGetter.invoke(typeObject, SigarInvoker.VOID_ARGS);
        }
        catch (Throwable t) {
            throw new SigarException(t.getMessage());
        }
    }
    
    private Method getAttributeMethod(String attr) throws SigarException {
        final String alias = SigarInvoker.compatAttrs.get(attr);
        if (alias != null) {
            attr = alias;
        }
        Class type = this.getTypeMethod(null).getReturnType();
        if (this.hasArrayIdx) {
            type = type.getComponentType();
        }
        HashMap attrs;
        synchronized (SigarInvoker.attrCache) {
            attrs = SigarInvoker.attrCache.get(type);
            if (attrs == null) {
                attrs = new HashMap();
                SigarInvoker.attrCache.put(type, attrs);
            }
            else {
                final Method attrMethod;
                if ((attrMethod = attrs.get(attr)) != null) {
                    return attrMethod;
                }
            }
        }
        Method attrMethod;
        try {
            attrMethod = type.getMethod("get" + attr, (Class[])SigarInvoker.VOID_SIGNATURE);
        }
        catch (Exception e) {
            final String msg = "Failed to invoke get" + attr + ": " + e.getMessage();
            throw new SigarException(msg);
        }
        synchronized (attrs) {
            attrs.put(attr, attrMethod);
        }
        return attrMethod;
    }
    
    static {
        SigarInvoker.attrCache = new HashMap();
        SigarInvoker.compatTypes = new HashMap();
        SigarInvoker.compatAttrs = new HashMap();
        SigarInvoker.compatTypes.put("NetIfconfig", "NetInterfaceConfig");
        SigarInvoker.compatTypes.put("NetIfstat", "NetInterfaceStat");
        SigarInvoker.compatTypes.put("DirStats", "DirStat");
        SigarInvoker.compatAttrs.put("Utime", "User");
        SigarInvoker.compatAttrs.put("Stime", "Sys");
        VOID_SIGNATURE = new Class[0];
        VOID_ARGS = new Object[0];
    }
}
