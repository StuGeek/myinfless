// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;

public class TestCpuInfo extends SigarTestCase
{
    public TestCpuInfo(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final CpuInfo[] infos = sigar.getCpuInfoList();
        for (int i = 0; i < infos.length; ++i) {
            final CpuInfo info = infos[i];
            this.traceln("num=" + i);
            this.traceln("vendor=" + info.getVendor());
            this.traceln("model=" + info.getModel());
            this.traceln("mhz=" + info.getMhz());
            this.traceln("cache size=" + info.getCacheSize());
            this.assertGtZeroTrace("totalSockets", info.getTotalSockets());
            this.assertGtZeroTrace("totalCores", info.getTotalCores());
            assertTrue(info.getTotalSockets() <= info.getTotalCores());
        }
        final int mhz = infos[0].getMhz();
        final int current = sigar.getCpuInfoList()[0].getMhz();
        assertEquals("Mhz=" + current + "/" + mhz, current, mhz);
    }
}
