// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.ThreadCpu;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.CpuTimer;
import org.hyperic.sigar.SigarNotImplementedException;

public class TestThreadCpu extends SigarTestCase
{
    public TestThreadCpu(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        ThreadCpu cpu;
        try {
            cpu = sigar.getThreadCpu();
        }
        catch (SigarNotImplementedException e) {
            return;
        }
        this.assertGtEqZeroTrace("User", cpu.getUser());
        this.assertGtEqZeroTrace("Sys", cpu.getSys());
        this.assertGtEqZeroTrace("Total", cpu.getTotal());
        final CpuTimer timer = new CpuTimer(sigar);
        timer.start();
        for (int i = 0; i < 1000000; ++i) {
            System.getProperty("java.home");
        }
        final String sleepTime = System.getProperty("sigar.testThreadCpu.sleep");
        if (sleepTime != null) {
            Thread.sleep(Integer.parseInt(sleepTime) * 1000);
        }
        timer.stop();
        this.traceln("\nUsage...\n");
        this.assertGtEqZeroTrace("User", timer.getCpuUser());
        this.assertGtEqZeroTrace("Sys", timer.getCpuSys());
        this.assertGtEqZeroTrace("Total", timer.getCpuTotal());
        this.assertGtEqZeroTrace("Real Time", timer.getTotalTime());
        this.traceln("Cpu Percent=" + CpuPerc.format(timer.getCpuUsage()));
    }
}
