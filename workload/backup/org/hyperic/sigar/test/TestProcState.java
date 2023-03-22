// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Sigar;

public class TestProcState extends SigarTestCase
{
    public TestProcState(final String name) {
        super(name);
    }
    
    private void traceState(final Sigar sigar, final long pid) {
        try {
            final ProcState procState = sigar.getProcState(pid);
            this.traceln("[pid=" + pid + "] " + procState);
        }
        catch (SigarException e) {
            this.traceln("pid " + pid + ": " + e.getMessage());
        }
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            sigar.getProcState(this.getInvalidPid());
        }
        catch (SigarException ex) {}
        final ProcState procState = sigar.getProcState(sigar.getPid());
        this.traceState(sigar, sigar.getPid());
        final char state = procState.getState();
        assertTrue(state == 'R' || state == 'S');
        assertTrue(procState.getName().indexOf("java") != -1);
        final long[] pids = sigar.getProcList();
        for (int i = 0; i < pids.length; ++i) {
            this.traceState(sigar, pids[i]);
        }
    }
}
