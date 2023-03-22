// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarException;
import java.util.List;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.Sigar;

public class TestProcModules extends SigarTestCase
{
    public TestProcModules(final String name) {
        super(name);
    }
    
    private void printModules(final Sigar sigar, final long pid) throws SigarException {
        this.traceln("\npid=" + pid);
        try {
            final List modules = sigar.getProcModules(pid);
            for (int i = 0; i < modules.size(); ++i) {
                this.traceln(i + "=" + modules.get(i));
            }
        }
        catch (SigarNotImplementedException ex) {}
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            this.printModules(sigar, this.getInvalidPid());
        }
        catch (SigarException ex) {}
        try {
            this.printModules(sigar, sigar.getPid());
        }
        catch (SigarNotImplementedException e2) {
            return;
        }
        final long[] pids = sigar.getProcList();
        for (int i = 0; i < pids.length; ++i) {
            try {
                this.printModules(sigar, pids[i]);
            }
            catch (SigarException e) {
                this.traceln(pids[i] + ": " + e.getMessage());
            }
        }
    }
}
