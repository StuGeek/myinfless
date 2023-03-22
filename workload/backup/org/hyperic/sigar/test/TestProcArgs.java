// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarException;

public class TestProcArgs extends SigarTestCase
{
    public TestProcArgs(final String name) {
        super(name);
    }
    
    private boolean findArg(final String[] args, final String what) {
        boolean found = false;
        this.traceln("find=" + what);
        for (int i = 0; i < args.length; ++i) {
            this.traceln("   " + i + "=" + args[i]);
            if (args[i].equals(what)) {
                found = true;
            }
        }
        return found;
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            sigar.getProcArgs(this.getInvalidPid());
        }
        catch (SigarException ex) {}
        try {
            final String[] args = sigar.getProcArgs(sigar.getPid());
            if (getVerbose()) {
                this.findArg(args, TestProcArgs.class.getName());
            }
            if (args.length > 0) {
                assertTrue(args[0].indexOf("java") != -1);
            }
            if (!System.getProperty("os.name").equals("HP-UX")) {}
        }
        catch (SigarNotImplementedException ex2) {}
        final long[] pids = sigar.getProcList();
        for (int i = 0; i < pids.length; ++i) {
            final String pidTrace = "pid=" + pids[i];
            try {
                final String[] args2 = sigar.getProcArgs(pids[i]);
                this.traceln(pidTrace);
                for (int j = 0; j < args2.length; ++j) {
                    this.traceln("   " + j + "=>" + args2[j] + "<==");
                }
            }
            catch (SigarException e) {
                this.traceln(pidTrace + ": " + e.getMessage());
            }
        }
    }
}
