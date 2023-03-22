// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Uptime;
import org.hyperic.sigar.Sigar;
import java.util.Date;

public class TestUptime extends SigarTestCase
{
    public TestUptime(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final Uptime uptime = sigar.getUptime();
        final long now = System.currentTimeMillis();
        this.traceln("\nboottime=" + new Date(now - (long)uptime.getUptime() * 1000L));
        assertTrue(uptime.getUptime() > 0.0);
    }
}
