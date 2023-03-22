// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;

public class TestSignal extends SigarTestCase
{
    public TestSignal(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final String[] signals = { "HUP", "INT", "KILL", "QUIT", "TERM", "USR1", "USR2" };
        for (int i = 0; i < signals.length; ++i) {
            final String sig = signals[i];
            this.traceln(sig + "=" + Sigar.getSigNum(sig));
        }
    }
}
