// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.Serializable;

public class NetStat implements Serializable
{
    private static final long serialVersionUID = 1070087L;
    protected int[] tcpStates;
    protected int tcpInboundTotal;
    protected int tcpOutboundTotal;
    protected int allInboundTotal;
    protected int allOutboundTotal;
    
    native void stat(final Sigar p0, final int p1, final byte[] p2, final long p3) throws SigarException;
    
    public void stat(final Sigar sigar) throws SigarException {
        final int flags = 19;
        this.stat(sigar, flags, null, -1L);
    }
    
    public void stat(final Sigar sigar, final byte[] address, final long port) throws SigarException {
        final int flags = 17;
        this.stat(sigar, flags, address, port);
    }
    
    public int getTcpInboundTotal() {
        return this.tcpInboundTotal;
    }
    
    public int getTcpOutboundTotal() {
        return this.tcpOutboundTotal;
    }
    
    public int getAllInboundTotal() {
        return this.allInboundTotal;
    }
    
    public int getAllOutboundTotal() {
        return this.allOutboundTotal;
    }
    
    public int[] getTcpStates() {
        return this.tcpStates;
    }
    
    public int getTcpEstablished() {
        return this.tcpStates[1];
    }
    
    public int getTcpSynSent() {
        return this.tcpStates[2];
    }
    
    public int getTcpSynRecv() {
        return this.tcpStates[3];
    }
    
    public int getTcpFinWait1() {
        return this.tcpStates[4];
    }
    
    public int getTcpFinWait2() {
        return this.tcpStates[5];
    }
    
    public int getTcpTimeWait() {
        return this.tcpStates[6];
    }
    
    public int getTcpClose() {
        return this.tcpStates[7];
    }
    
    public int getTcpCloseWait() {
        return this.tcpStates[8];
    }
    
    public int getTcpLastAck() {
        return this.tcpStates[9];
    }
    
    public int getTcpListen() {
        return this.tcpStates[10];
    }
    
    public int getTcpClosing() {
        return this.tcpStates[11];
    }
    
    public int getTcpIdle() {
        return this.tcpStates[12];
    }
    
    public int getTcpBound() {
        return this.tcpStates[13];
    }
}
