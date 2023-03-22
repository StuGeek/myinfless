// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.NfsServerV2;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class TestNfsServerV2 extends SigarTestCase
{
    public TestNfsServerV2(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        NfsServerV2 nfs;
        try {
            nfs = sigar.getNfsServerV2();
        }
        catch (SigarException e) {
            return;
        }
        this.traceMethods(nfs);
    }
}
