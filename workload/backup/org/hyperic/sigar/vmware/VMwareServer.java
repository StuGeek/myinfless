// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.vmware;

import java.util.List;

public class VMwareServer extends VMwareObject
{
    native void destroy();
    
    private native void create();
    
    public native boolean connect(final ConnectParams p0) throws VMwareException;
    
    public native void disconnect();
    
    public native boolean isConnected();
    
    public native boolean isRegistered(final String p0) throws VMwareException;
    
    public native List getRegisteredVmNames() throws VMwareException;
    
    public native String getResource(final String p0) throws VMwareException;
    
    public native String exec(final String p0) throws VMwareException;
    
    public VMwareServer() {
        this.create();
    }
}
