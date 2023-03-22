// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;

public class Humidor
{
    private static final Humidor INSTANCE;
    private Object LOCK;
    private InvocationHandler _handler;
    private SigarProxy _sigar;
    private Sigar _impl;
    private Sigar _inst;
    
    private Humidor() {
        this.LOCK = new Object();
    }
    
    public Humidor(final Sigar sigar) {
        this.LOCK = new Object();
        this._impl = sigar;
    }
    
    public SigarProxy getSigar() {
        synchronized (this.LOCK) {
            if (this._sigar == null) {
                if (this._impl == null) {
                    final Sigar sigar = new Sigar();
                    this._impl = sigar;
                    this._inst = sigar;
                }
                this._handler = new MyHandler(this._impl);
                this._sigar = (SigarProxy)Proxy.newProxyInstance(Humidor.class.getClassLoader(), new Class[] { SigarProxy.class }, this._handler);
            }
            return this._sigar;
        }
    }
    
    public static Humidor getInstance() {
        return Humidor.INSTANCE;
    }
    
    public void close() {
        if (this._inst != null) {
            this._inst.close();
            final Sigar sigar = null;
            this._inst = sigar;
            this._impl = sigar;
        }
        this._sigar = null;
    }
    
    static {
        INSTANCE = new Humidor();
    }
    
    private static class MyHandler implements InvocationHandler
    {
        private SigarProxy _sigar;
        private Object _lock;
        
        public MyHandler(final SigarProxy sigar) {
            this._lock = new Object();
            this._sigar = sigar;
        }
        
        public Object invoke(final Object proxy, final Method meth, final Object[] args) throws Throwable {
            try {
                synchronized (this._lock) {
                    return meth.invoke(this._sigar, args);
                }
            }
            catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
