// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.SigarPermissionDeniedException;

public class TestNetInfo extends SigarTestCase
{
    public TestNetInfo(final String name) {
        super(name);
    }
    
    public void testNetInfo() throws SigarException {
        final NetInfo info = this.getSigar().getNetInfo();
        final NetInterfaceConfig config = this.getSigar().getNetInterfaceConfig(null);
        this.traceln("");
        this.traceln(info.toString());
        this.traceln(config.toString());
        final int flags = 18;
        NetConnection[] connections;
        try {
            connections = this.getSigar().getNetConnectionList(flags);
        }
        catch (SigarPermissionDeniedException e) {
            return;
        }
        for (int i = 0; i < connections.length; ++i) {
            final long port = connections[i].getLocalPort();
            String listenAddress = this.getSigar().getNetListenAddress(port);
            if (NetFlags.isAnyAddress(listenAddress)) {
                listenAddress = "*";
            }
            this.traceln("Listen " + listenAddress + ":" + port);
        }
    }
}
