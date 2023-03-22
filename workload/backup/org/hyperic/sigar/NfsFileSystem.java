// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.io.Serializable;

public class NfsFileSystem extends FileSystem implements Serializable
{
    private static final long serialVersionUID = 607239L;
    private static final int NFS_PROGRAM = 100003;
    String hostname;
    
    public NfsFileSystem() {
        this.hostname = null;
    }
    
    public String getHostname() {
        if (this.hostname == null) {
            final String dev = this.getDevName();
            final int ix = dev.indexOf(":");
            if (ix != -1) {
                final String host = dev.substring(0, ix);
                try {
                    final InetAddress addr = InetAddress.getByName(host);
                    this.hostname = addr.getHostAddress();
                }
                catch (UnknownHostException e) {
                    this.hostname = host;
                }
            }
        }
        return this.hostname;
    }
    
    public boolean ping() {
        final String hostname = this.getHostname();
        return RPC.ping(hostname, 16, 100003L, 2L) == 0 || RPC.ping(hostname, 32, 100003L, 2L) == 0;
    }
    
    public String getUnreachableMessage() {
        return this.getDevName() + " nfs server unreachable";
    }
    
    public NfsUnreachableException getUnreachableException() {
        return new NfsUnreachableException(this.getUnreachableMessage());
    }
    
    public static void main(final String[] args) throws Exception {
        Sigar.load();
        System.out.println(RPC.ping(args[0], "nfs"));
    }
}
