// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.Sigar;

public class TestNetIf extends SigarTestCase
{
    public TestNetIf(final String name) {
        super(name);
    }
    
    private void getNetIflist(final Sigar sigar, final boolean getStats) throws Exception {
        final String[] ifNames = sigar.getNetInterfaceList();
        for (int i = 0; i < ifNames.length; ++i) {
            final String name = ifNames[i];
            final NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            this.traceln("name=" + name);
            this.assertTrueTrace("Address", ifconfig.getAddress());
            this.assertTrueTrace("Netmask", ifconfig.getNetmask());
            if (getStats) {
                if ((ifconfig.getFlags() & 0x1L) <= 0L) {
                    this.traceln("!IFF_UP...skipping getNetInterfaceStat");
                }
                else {
                    try {
                        final NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
                        this.assertGtEqZeroTrace("RxPackets", ifstat.getRxPackets());
                        this.assertGtEqZeroTrace("TxPackets", ifstat.getTxPackets());
                        this.traceMethods(ifstat);
                    }
                    catch (SigarNotImplementedException e2) {}
                    catch (SigarException e) {
                        if (name.indexOf(58) == -1) {
                            fail("getNetInterfaceStat(" + name + "): " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
    
    private void getGarbage(final Sigar sigar) {
        try {
            this.traceln("testing bogus getNetInterfaceStat");
            sigar.getNetInterfaceStat("were switching to night vision");
            fail("switched to night vision");
        }
        catch (SigarException ex) {}
        try {
            this.traceln("testing bogus getNetInterfaceConfig");
            sigar.getNetInterfaceConfig("happy meal");
            fail("unexpected treat in happy meal");
        }
        catch (SigarException ex2) {}
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        this.getNetIflist(sigar, false);
        this.getNetIflist(sigar, false);
        this.getNetIflist(sigar, true);
        this.traceln("Default IP=" + sigar.getNetInterfaceConfig().getAddress());
        this.getGarbage(sigar);
    }
}
