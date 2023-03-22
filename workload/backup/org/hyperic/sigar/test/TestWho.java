// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Who;
import java.util.Date;

public class TestWho extends SigarTestCase
{
    public TestWho(final String name) {
        super(name);
    }
    
    public void testWho() throws SigarException {
        this.traceln("");
        final Who[] who = this.getSigar().getWhoList();
        for (int i = 0; i < who.length; ++i) {
            String host = who[i].getHost();
            if (host.length() != 0) {
                host = "(" + host + ")";
            }
            this.traceln(who[i].getUser() + "\t" + who[i].getDevice() + "\t" + new Date(who[i].getTime() * 1000L) + "\t" + host);
            this.assertLengthTrace("user", who[i].getUser());
        }
    }
}
