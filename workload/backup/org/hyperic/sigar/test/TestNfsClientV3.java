// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.NfsClientV3;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class TestNfsClientV3 extends SigarTestCase
{
    public TestNfsClientV3(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        NfsClientV3 nfs;
        try {
            nfs = sigar.getNfsClientV3();
        }
        catch (SigarException e) {
            return;
        }
        this.traceMethods(nfs);
    }
}
