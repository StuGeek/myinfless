// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.NfsServerV3;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class TestNfsServerV3 extends SigarTestCase
{
    public TestNfsServerV3(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        NfsServerV3 nfs;
        try {
            nfs = sigar.getNfsServerV3();
        }
        catch (SigarException e) {
            return;
        }
        this.traceMethods(nfs);
    }
}
