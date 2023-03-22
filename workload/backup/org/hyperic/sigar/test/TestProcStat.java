// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.CurrentProcessSummary;
import org.hyperic.sigar.SigarProxyCache;

public class TestProcStat extends SigarTestCase
{
    public TestProcStat(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final ProcStat stat = sigar.getProcStat();
        final long[] pids = sigar.getProcList();
        assertTrue(stat.getTotal() > 1L);
        this.traceln(stat.toString());
        final SigarProxy proxy = SigarProxyCache.newInstance(this.getSigar());
        this.traceln(CurrentProcessSummary.get(proxy).toString());
    }
}
