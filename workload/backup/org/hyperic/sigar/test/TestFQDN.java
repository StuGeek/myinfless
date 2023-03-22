// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

public class TestFQDN extends SigarTestCase
{
    public TestFQDN(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final String fqdn = this.getSigar().getFQDN();
        this.traceln("fqdn=" + fqdn);
        final boolean validFQDN = fqdn.indexOf(".") > 0;
        assertTrue(validFQDN);
    }
}
