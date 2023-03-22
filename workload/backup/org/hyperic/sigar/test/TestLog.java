// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;

public class TestLog extends SigarTestCase
{
    public TestLog(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = new Sigar();
        sigar.enableLogging(true);
        sigar.enableLogging(false);
        sigar.enableLogging(true);
        sigar.close();
    }
}
