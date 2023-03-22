// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarNotImplementedException;

public class TestLoadAverage extends SigarTestCase
{
    public TestLoadAverage(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        try {
            final double[] loadavg = sigar.getLoadAverage();
            assertTrue(loadavg.length == 3);
            this.traceln("1min=" + loadavg[0]);
            this.traceln("5min=" + loadavg[1]);
            this.traceln("15min=" + loadavg[2]);
        }
        catch (SigarNotImplementedException ex) {}
    }
}
