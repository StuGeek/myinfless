// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Cpu;

public class TestCpu extends SigarTestCase
{
    public TestCpu(final String name) {
        super(name);
    }
    
    private void checkCpu(final Cpu cpu) {
        this.traceln("User..." + cpu.getUser());
        assertTrue(cpu.getUser() >= 0L);
        this.traceln("Sys...." + cpu.getSys());
        assertTrue(cpu.getSys() >= 0L);
        this.traceln("Idle..." + cpu.getIdle());
        assertTrue(cpu.getIdle() >= 0L);
        this.traceln("Wait..." + cpu.getWait());
        assertTrue(cpu.getWait() >= 0L);
        this.traceln("Irq..." + cpu.getIrq());
        assertTrue(cpu.getIrq() >= 0L);
        this.traceln("SIrq.." + cpu.getSoftIrq());
        assertTrue(cpu.getSoftIrq() >= 0L);
        this.traceln("Stl..." + cpu.getStolen());
        assertTrue(cpu.getStolen() >= 0L);
        this.traceln("Total.." + cpu.getTotal());
        assertTrue(cpu.getTotal() > 0L);
        try {
            final long current = this.getSigar().getProcState("$$").getProcessor();
            this.traceln("last run cpu=" + current);
        }
        catch (SigarException e) {
            e.printStackTrace();
        }
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final Cpu cpu = sigar.getCpu();
        this.traceln("getCpu:");
        this.checkCpu(cpu);
        try {
            final Cpu[] cpuList = sigar.getCpuList();
            for (int i = 0; i < cpuList.length; ++i) {
                this.traceln("Cpu " + i + ":");
                this.checkCpu(cpuList[i]);
            }
        }
        catch (SigarNotImplementedException ex) {}
    }
    
    private static void printCpu(final String prefix, final CpuPerc cpu) {
        System.out.println(prefix + CpuPerc.format(cpu.getUser()) + "\t" + CpuPerc.format(cpu.getSys()) + "\t" + CpuPerc.format(cpu.getWait()) + "\t" + CpuPerc.format(cpu.getNice()) + "\t" + CpuPerc.format(cpu.getIdle()) + "\t" + CpuPerc.format(cpu.getCombined()));
    }
    
    public static void main(final String[] args) throws Exception {
        final String HEADER = "   User\tSys\tWait\tNice\tIdle\tTotal";
        int interval = 1;
        if (args.length > 0) {
            interval = Integer.parseInt(args[0]);
        }
        final int sleep = 60000 * interval;
        final Sigar sigar = new Sigar();
        while (true) {
            System.out.println("   User\tSys\tWait\tNice\tIdle\tTotal");
            printCpu("   ", sigar.getCpuPerc());
            final CpuPerc[] cpuList = sigar.getCpuPercList();
            for (int i = 0; i < cpuList.length; ++i) {
                printCpu(i + ": ", cpuList[i]);
            }
            Thread.sleep(sleep);
        }
    }
}
