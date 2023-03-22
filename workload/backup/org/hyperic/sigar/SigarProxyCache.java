// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.hyperic.sigar.util.ReferenceMap;
import java.util.Map;
import java.lang.reflect.InvocationHandler;

public class SigarProxyCache implements InvocationHandler
{
    private Sigar sigar;
    private Map cache;
    public static final int EXPIRE_DEFAULT = 30000;
    private int expire;
    private static final boolean debugEnabled;
    
    public SigarProxyCache(final Sigar sigar, final int expire) {
        this.cache = ReferenceMap.newInstance();
        this.sigar = sigar;
        this.expire = expire;
    }
    
    public static SigarProxy newInstance() {
        return newInstance(new Sigar());
    }
    
    public static SigarProxy newInstance(final Sigar sigar) {
        return newInstance(sigar, 30000);
    }
    
    public static SigarProxy newInstance(final Sigar sigar, final int expire) {
        final SigarProxyCache handler = new SigarProxyCache(sigar, expire);
        final SigarProxy proxy = (SigarProxy)Proxy.newProxyInstance(SigarProxy.class.getClassLoader(), new Class[] { SigarProxy.class }, handler);
        return proxy;
    }
    
    private void debug(final String msg) {
        SigarLog.getLogger("SigarProxyCache").debug((Object)msg);
    }
    
    public static void setExpire(final SigarProxy proxy, final String type, final int expire) throws SigarException {
    }
    
    private static SigarProxyCache getHandler(final Object proxy) {
        return (SigarProxyCache)Proxy.getInvocationHandler(proxy);
    }
    
    public static void clear(final Object proxy) {
        getHandler(proxy).cache.clear();
    }
    
    public static Sigar getSigar(final Object proxy) {
        if (proxy.getClass() == Sigar.class) {
            return (Sigar)proxy;
        }
        return getHandler(proxy).sigar;
    }
    
    private String getDebugArgs(final Object[] args, final Object argKey) {
        if (args.length == 0) {
            return null;
        }
        final StringBuffer dargs = new StringBuffer(args[0].toString());
        for (int i = 1; i < args.length; ++i) {
            dargs.append(',').append(args[i].toString());
        }
        if (!dargs.toString().equals(argKey.toString())) {
            dargs.append('/').append(argKey);
        }
        return dargs.toString();
    }
    
    public synchronized Object invoke(final Object proxy, final Method method, final Object[] args) throws SigarException, SigarNotImplementedException {
        SigarCacheObject cacheVal = null;
        Object argKey = null;
        Map argMap = null;
        final long timeNow = System.currentTimeMillis();
        if (args != null) {
            if (args.length == 1) {
                argKey = args[0];
            }
            else {
                int hashCode = 0;
                for (int i = 0; i < args.length; ++i) {
                    hashCode ^= args[i].hashCode();
                }
                argKey = new Integer(hashCode);
            }
            argMap = this.cache.get(method);
            if (argMap == null) {
                argMap = ReferenceMap.newInstance();
            }
            else {
                cacheVal = argMap.get(argKey);
            }
        }
        else {
            cacheVal = this.cache.get(method);
        }
        if (cacheVal == null) {
            cacheVal = new SigarCacheObject();
        }
        String argDebug = "";
        if (SigarProxyCache.debugEnabled && args != null && args.length != 0) {
            argDebug = " with args=" + this.getDebugArgs(args, argKey);
        }
        if (cacheVal.value != null) {
            if (SigarProxyCache.debugEnabled) {
                this.debug("found " + method.getName() + " in cache" + argDebug);
            }
            if (timeNow - cacheVal.timestamp > this.expire) {
                if (SigarProxyCache.debugEnabled) {
                    this.debug("expiring " + method.getName() + " from cache" + argDebug);
                }
                cacheVal.value = null;
            }
        }
        else if (SigarProxyCache.debugEnabled) {
            this.debug(method.getName() + " NOT in cache" + argDebug);
        }
        Object retval;
        if (cacheVal.value == null) {
            try {
                retval = method.invoke(this.sigar, args);
            }
            catch (InvocationTargetException e) {
                final Throwable t = e.getTargetException();
                String msg;
                if (t instanceof SigarException) {
                    msg = "";
                }
                else {
                    msg = t.getClass().getName() + ": ";
                }
                msg += t.getMessage();
                if (argKey != null) {
                    msg = msg + ": " + this.getDebugArgs(args, argKey);
                }
                if (t instanceof SigarNotImplementedException) {
                    throw new SigarNotImplementedException(msg);
                }
                if (t instanceof SigarPermissionDeniedException) {
                    throw new SigarPermissionDeniedException(msg);
                }
                throw new SigarException(msg);
            }
            catch (Exception e2) {
                String msg2 = e2.getClass().getName() + ": " + e2.getMessage();
                if (argKey != null) {
                    msg2 = msg2 + ": " + this.getDebugArgs(args, argKey);
                }
                throw new SigarException(msg2);
            }
            cacheVal.value = retval;
            cacheVal.timestamp = timeNow;
            if (args == null) {
                this.cache.put(method, cacheVal);
            }
            else {
                argMap.put(argKey, cacheVal);
                this.cache.put(method, argMap);
            }
        }
        else {
            retval = cacheVal.value;
        }
        return retval;
    }
    
    static {
        debugEnabled = "debug".equals(System.getProperty("sigar.log"));
    }
}
