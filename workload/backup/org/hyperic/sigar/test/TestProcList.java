// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;
import java.util.ArrayList;

public class TestProcList extends SigarTestCase
{
    public TestProcList(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final ArrayList traceList = new ArrayList();
        final long[] pids = sigar.getProcList();
        assertTrue(pids.length > 1);
        final long pid = sigar.getPid();
        boolean foundPid = false;
        for (int i = 0; i < pids.length; ++i) {
            traceList.add(new Long(pids[i]));
            if (pid == pids[i]) {
                foundPid = true;
            }
        }
        this.traceln("pids=" + traceList);
        assertTrue(foundPid);
    }
}
