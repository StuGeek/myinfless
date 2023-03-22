// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.NetRoute;
import org.hyperic.sigar.SigarNotImplementedException;

public class TestNetRoute extends SigarTestCase
{
    public TestNetRoute(final String name) {
        super(name);
    }
    
    public void testNetRoute() throws SigarException {
        NetRoute[] routes;
        try {
            routes = this.getSigar().getNetRouteList();
        }
        catch (SigarNotImplementedException e) {
            return;
        }
        assertTrue(routes.length > 0);
        for (int i = 0; i < routes.length; ++i) {
            final NetRoute route = routes[i];
        }
    }
}
