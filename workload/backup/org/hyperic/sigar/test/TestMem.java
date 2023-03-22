// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

public class TestMem extends SigarTestCase
{
    public TestMem(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final Mem mem = sigar.getMem();
        this.assertGtZeroTrace("Total", mem.getTotal());
        this.assertGtZeroTrace("Used", mem.getUsed());
        this.traceln("UsedPercent=" + mem.getUsedPercent());
        this.assertGtZeroTrace("(long)UsedPercent", (long)mem.getUsedPercent());
        assertTrue(mem.getUsedPercent() <= 100.0);
        this.traceln("FreePercent=" + mem.getFreePercent());
        this.assertGtEqZeroTrace("(long)FreePercent", (long)mem.getFreePercent());
        assertTrue(mem.getFreePercent() < 100.0);
        this.assertGtZeroTrace("Free", mem.getFree());
        this.assertGtZeroTrace("ActualUsed", mem.getActualUsed());
        this.assertGtZeroTrace("ActualFree", mem.getActualFree());
        this.assertGtZeroTrace("Ram", mem.getRam());
        assertTrue(mem.getRam() % 8L == 0L);
    }
}
