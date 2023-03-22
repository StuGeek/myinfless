// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.vmware;

public class ConnectParams extends VMwareObject
{
    private native void create(final String p0, final int p1, final String p2, final String p3);
    
    native void destroy();
    
    public ConnectParams() {
        this(null, 0, null, null);
    }
    
    public ConnectParams(final String host, final int port, final String user, final String pass) {
        this.create(host, port, user, pass);
    }
}
