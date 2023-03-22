// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Tcp;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarNotImplementedException;

public class TestTcpStat extends SigarTestCase
{
    public TestTcpStat(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        Tcp tcp;
        try {
            tcp = sigar.getTcp();
        }
        catch (SigarNotImplementedException e) {
            return;
        }
        catch (SigarPermissionDeniedException e2) {
            return;
        }
        this.traceln("");
        this.assertValidFieldTrace("ActiveOpens", tcp.getActiveOpens());
        this.assertValidFieldTrace("PassiveOpens", tcp.getPassiveOpens());
        this.assertValidFieldTrace("AttemptFails", tcp.getAttemptFails());
        this.assertValidFieldTrace("EstabResets", tcp.getEstabResets());
        this.assertValidFieldTrace("CurrEstab", tcp.getCurrEstab());
        this.assertValidFieldTrace("InSegs", tcp.getInSegs());
        this.assertValidFieldTrace("OutSegs", tcp.getOutSegs());
        this.assertValidFieldTrace("RetransSegs", tcp.getRetransSegs());
        this.assertValidFieldTrace("OutRsts", tcp.getOutRsts());
    }
}
