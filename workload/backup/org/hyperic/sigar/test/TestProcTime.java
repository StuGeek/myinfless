// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.Sigar;
import java.util.Date;
import org.hyperic.sigar.SigarException;

public class TestProcTime extends SigarTestCase
{
    public TestProcTime(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            sigar.getProcTime(this.getInvalidPid());
        }
        catch (SigarException ex) {}
        final ProcCpu procTime = sigar.getProcCpu(sigar.getPid());
        this.assertGtEqZeroTrace("StartTime", procTime.getStartTime());
        this.traceln("StartDate=" + new Date(procTime.getStartTime()));
        this.assertGtEqZeroTrace("User", procTime.getUser());
        this.assertGtEqZeroTrace("Sys", procTime.getSys());
        this.assertGtEqZeroTrace("Total", procTime.getTotal());
        final double value = procTime.getPercent() * 100.0;
        this.traceln("Percent=" + value);
        assertTrue(value >= 0.0);
        final int ncpu = sigar.getCpuList().length;
        assertTrue(value <= 100.0 * ncpu);
    }
}
