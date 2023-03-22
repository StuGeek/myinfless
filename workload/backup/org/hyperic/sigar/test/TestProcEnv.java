// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import java.util.Map;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarNotImplementedException;
import java.io.File;
import org.hyperic.sigar.SigarException;

public class TestProcEnv extends SigarTestCase
{
    public TestProcEnv(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            sigar.getProcEnv(this.getInvalidPid());
        }
        catch (SigarException ex) {}
        final long pid = sigar.getPid();
        try {
            final Map env = sigar.getProcEnv(pid);
            this.traceln(env.toString());
            String key = "JAVA_HOME";
            String val = env.get(key);
            final String single = sigar.getProcEnv(pid, key);
            if (val != null) {
                assertTrue(new File(val, "bin").exists());
                assertTrue(val.equals(single));
                this.traceln(key + "==>" + single);
            }
            key = "dOeSnOtExIsT";
            val = env.get(key);
            assertTrue(val == null);
            val = sigar.getProcEnv(pid, key);
            assertTrue(val == null);
        }
        catch (SigarNotImplementedException e) {}
        catch (SigarPermissionDeniedException ex2) {}
        final long[] pids = sigar.getProcList();
        for (int i = 0; i < pids.length; ++i) {
            try {
                sigar.getProcEnv(pids[i]);
            }
            catch (SigarException ex3) {}
        }
    }
}
