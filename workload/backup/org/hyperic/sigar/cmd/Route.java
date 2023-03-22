// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.NetRoute;
import java.util.List;
import java.util.ArrayList;

public class Route extends SigarCommandBase
{
    private static final String OUTPUT_FORMAT = "%-15s %-15s %-15s %-5s %-6s %-3s %-s";
    private static final String[] HEADER;
    
    public Route(final Shell shell) {
        super(shell);
        this.setOutputFormat("%-15s %-15s %-15s %-5s %-6s %-3s %-s");
    }
    
    public Route() {
        this.setOutputFormat("%-15s %-15s %-15s %-5s %-6s %-3s %-s");
    }
    
    private static String flags(final long flags) {
        final StringBuffer sb = new StringBuffer();
        if ((flags & 0x1L) != 0x0L) {
            sb.append('U');
        }
        if ((flags & 0x2L) != 0x0L) {
            sb.append('G');
        }
        return sb.toString();
    }
    
    public String getUsageShort() {
        return "Kernel IP routing table";
    }
    
    public void output(final String[] args) throws SigarException {
        final NetRoute[] routes = this.sigar.getNetRouteList();
        this.printf(Route.HEADER);
        for (int i = 0; i < routes.length; ++i) {
            final NetRoute route = routes[i];
            final ArrayList items = new ArrayList();
            items.add(route.getDestination());
            items.add(route.getGateway());
            items.add(route.getMask());
            items.add(flags(route.getFlags()));
            items.add(String.valueOf(route.getMetric()));
            items.add(String.valueOf(route.getRefcnt()));
            items.add(route.getIfname());
            this.printf(items);
        }
    }
    
    public static void main(final String[] args) throws Exception {
        new Route().processCommand(args);
    }
    
    static {
        HEADER = new String[] { "Destination", "Gateway", "Genmask", "Flags", "Metric", "Ref", "Iface" };
    }
}
