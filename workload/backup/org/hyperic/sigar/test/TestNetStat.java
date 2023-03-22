// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarNotImplementedException;

public class TestNetStat extends SigarTestCase
{
    public TestNetStat(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        NetStat netstat;
        try {
            netstat = sigar.getNetStat();
        }
        catch (SigarNotImplementedException e) {
            return;
        }
        catch (SigarPermissionDeniedException e2) {
            return;
        }
        this.traceln("");
        this.assertGtEqZeroTrace("Outbound", netstat.getTcpOutboundTotal());
        this.assertGtEqZeroTrace("Inbound", netstat.getTcpInboundTotal());
        final int[] states = netstat.getTcpStates();
        for (int i = 0; i < 14; ++i) {
            this.assertGtEqZeroTrace(NetConnection.getStateString(i), states[i]);
        }
    }
}
