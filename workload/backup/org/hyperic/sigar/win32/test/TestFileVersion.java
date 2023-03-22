// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32.test;

import org.hyperic.sigar.win32.FileVersion;
import org.hyperic.sigar.win32.Win32;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.test.SigarTestCase;

public class TestFileVersion extends SigarTestCase
{
    public TestFileVersion(final String name) {
        super(name);
    }
    
    private void printExe(final long pid) {
        this.traceln("\npid=" + pid);
        String name;
        try {
            name = this.getSigar().getProcExe(pid).getName();
        }
        catch (SigarException e) {
            return;
        }
        final FileVersion info = Win32.getFileVersion(name);
        if (info != null) {
            this.traceln("exe='" + name + "'");
            this.traceln("version=" + info.getProductVersion());
        }
    }
    
    public void testCreate() throws Exception {
        assertTrue(Win32.getFileVersion("DoEsNoTeXist.exe") == null);
        final long[] pids = this.getSigar().getProcList();
        for (int i = 0; i < pids.length; ++i) {
            this.printExe(pids[i]);
        }
    }
}
