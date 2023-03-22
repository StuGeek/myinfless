// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.NfsClientV2;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class TestNfsClientV2 extends SigarTestCase
{
    public TestNfsClientV2(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        NfsClientV2 nfs;
        try {
            nfs = sigar.getNfsClientV2();
        }
        catch (SigarException e) {
            return;
        }
        this.traceMethods(nfs);
    }
}
