// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

public class NetServices
{
    private static NetServices instance;
    private Sigar sigar;
    
    private NetServices() {
        this.sigar = new Sigar();
    }
    
    protected void finalize() {
        this.sigar.close();
    }
    
    private static NetServices getInstance() {
        if (NetServices.instance == null) {
            NetServices.instance = new NetServices();
        }
        return NetServices.instance;
    }
    
    private static String getServiceName(final int protocol, final long port) {
        return getInstance().sigar.getNetServicesName(protocol, port);
    }
    
    public static String getName(final String protocol, final long port) {
        if (protocol.equals("tcp")) {
            return getTcpName(port);
        }
        if (protocol.equals("udp")) {
            return getUdpName(port);
        }
        return String.valueOf(port);
    }
    
    public static String getTcpName(final long port) {
        return getServiceName(16, port);
    }
    
    public static String getUdpName(final long port) {
        return getServiceName(32, port);
    }
}
