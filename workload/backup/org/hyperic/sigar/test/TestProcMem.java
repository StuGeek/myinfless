// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Sigar;

public class TestProcMem extends SigarTestCase
{
    public TestProcMem(final String name) {
        super(name);
    }
    
    private void traceMem(final Sigar sigar, final long pid) throws Exception {
        ProcMem procMem;
        try {
            procMem = sigar.getProcMem(pid);
        }
        catch (SigarException e) {
            this.traceln("pid " + pid + ": " + e.getMessage());
            return;
        }
        this.traceln("Pid=" + pid);
        this.traceln("Size=" + Sigar.formatSize(procMem.getSize()));
        this.traceln("Resident=" + Sigar.formatSize(procMem.getResident()));
        this.traceln("Share=" + Sigar.formatSize(procMem.getShare()));
        this.traceln("MinorFaults=" + procMem.getMinorFaults());
        this.traceln("MajorFaults=" + procMem.getMajorFaults());
        this.traceln("PageFaults=" + procMem.getPageFaults());
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            sigar.getProcMem(this.getInvalidPid());
        }
        catch (SigarException ex) {}
        final long[] pids = sigar.getProcList();
        for (int i = 0; i < pids.length; ++i) {
            this.traceMem(sigar, pids[i]);
        }
    }
}
