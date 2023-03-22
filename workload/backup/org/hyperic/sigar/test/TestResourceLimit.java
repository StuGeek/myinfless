// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ResourceLimit;

public class TestResourceLimit extends SigarTestCase
{
    public TestResourceLimit(final String name) {
        super(name);
    }
    
    public void testResourceLimit() throws SigarException {
        final ResourceLimit limit = this.getSigar().getResourceLimit();
    }
}
