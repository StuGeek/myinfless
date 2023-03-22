// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import junit.framework.AssertionFailedError;
import java.io.File;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.Sigar;

public class TestProcExe extends SigarTestCase
{
    public TestProcExe(final String name) {
        super(name);
    }
    
    private void printExe(final Sigar sigar, final long pid) throws SigarException {
        this.traceln("\npid=" + pid);
        try {
            final ProcExe exe = sigar.getProcExe(pid);
            final String cwd = exe.getCwd();
            this.traceln("cwd='" + cwd + "'");
            this.traceln("exe='" + exe.getName() + "'");
        }
        catch (SigarNotImplementedException ex) {}
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            sigar.getProcExe(this.getInvalidPid());
        }
        catch (SigarException ex) {}
        try {
            final ProcExe exe = sigar.getProcExe(sigar.getPid());
            final File exeFile = new File(exe.getName());
            final String cwd = exe.getCwd();
            this.traceln("cwd='" + cwd + "'");
            if (cwd.length() > 0) {
                assertTrue(new File(cwd).isDirectory());
            }
            this.traceln("exe='" + exe.getName() + "'");
            if (exe.getName().length() > 0) {
                assertTrue(exeFile.exists());
                assertTrue(exeFile.getName().startsWith("java"));
            }
        }
        catch (SigarNotImplementedException ex2) {}
        final long[] pids = sigar.getProcList();
        for (int i = 0; i < pids.length; ++i) {
            try {
                this.printExe(sigar, pids[i]);
            }
            catch (SigarException e) {}
            catch (AssertionFailedError assertionFailedError) {}
        }
    }
}
