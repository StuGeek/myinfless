// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarNotImplementedException;
import java.io.FileInputStream;
import java.io.File;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.SigarException;

public class TestProcFd extends SigarTestCase
{
    public TestProcFd(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            sigar.getProcFd(this.getInvalidPid());
        }
        catch (SigarException ex) {}
        try {
            final long pid = sigar.getPid();
            final long total = sigar.getProcFd(pid).getTotal();
            final SigarLoader loader = new SigarLoader(Sigar.class);
            final String path = loader.findJarPath(null);
            final File file = new File(path, loader.getJarName());
            this.traceln("Opening " + file);
            final FileInputStream is = new FileInputStream(file);
            this.assertEqualsTrace("Total", total + 1L, sigar.getProcFd(pid).getTotal());
            is.close();
            this.assertEqualsTrace("Total", total, sigar.getProcFd(pid).getTotal());
        }
        catch (SigarNotImplementedException e) {}
        catch (SigarPermissionDeniedException ex2) {}
    }
}
