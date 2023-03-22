// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.NetInterfaceConfig;
import java.util.ArrayList;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarNotImplementedException;
import java.net.InetAddress;
import org.hyperic.sigar.Sigar;

public class TestNetStatPort extends SigarTestCase
{
    public TestNetStatPort(final String name) {
        super(name);
    }
    
    private void netstat(final Sigar sigar, final String addr, final long port) throws Exception {
        final InetAddress address = InetAddress.getByName(addr);
        this.traceln("");
        this.traceln("using address=" + address + ":" + port);
        NetStat netstat;
        try {
            netstat = sigar.getNetStat(address.getAddress(), port);
        }
        catch (SigarNotImplementedException e) {
            return;
        }
        catch (SigarPermissionDeniedException e2) {
            return;
        }
        this.assertGtEqZeroTrace("AllOutbound", netstat.getAllOutboundTotal());
        this.assertGtEqZeroTrace("Outbound", netstat.getTcpOutboundTotal());
        this.assertGtEqZeroTrace("Inbound", netstat.getTcpInboundTotal());
        this.assertGtEqZeroTrace("AllInbound", netstat.getAllInboundTotal());
        final int[] states = netstat.getTcpStates();
        for (int i = 0; i < 14; ++i) {
            this.assertGtEqZeroTrace(NetConnection.getStateString(i), states[i]);
        }
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(null);
        final ArrayList addrs = new ArrayList();
        addrs.add(ifconfig.getAddress());
        addrs.add("127.0.0.1");
        if (TestNetStatPort.JDK_14_COMPAT) {
            addrs.add("::1");
        }
        for (int i = 0; i < addrs.size(); ++i) {
            final String addr = addrs.get(i);
            this.netstat(sigar, addr, 22L);
        }
    }
}
