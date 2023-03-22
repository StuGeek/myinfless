// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Sigar;

public class TestSwap extends SigarTestCase
{
    public TestSwap(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final Swap swap = sigar.getSwap();
        this.assertGtEqZeroTrace("Total", swap.getTotal());
        this.assertGtEqZeroTrace("Used", swap.getUsed());
        this.assertGtEqZeroTrace("Free", swap.getFree());
        this.assertEqualsTrace("Total-Used==Free", swap.getTotal() - swap.getUsed(), swap.getFree());
        this.traceln("PageIn=" + swap.getPageIn());
        this.traceln("PageOut=" + swap.getPageOut());
    }
}
