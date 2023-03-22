// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.NetFlags;
import java.util.Arrays;
import org.hyperic.sigar.SigarException;
import java.util.Collection;

public class Ifconfig extends SigarCommandBase
{
    public Ifconfig(final Shell shell) {
        super(shell);
    }
    
    public Ifconfig() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length <= 1;
    }
    
    public String getSyntaxArgs() {
        return "[interface]";
    }
    
    public String getUsageShort() {
        return "Network interface information";
    }
    
    public Collection getCompletions() {
        String[] ifNames;
        try {
            ifNames = this.proxy.getNetInterfaceList();
        }
        catch (SigarException e) {
            return super.getCompletions();
        }
        return Arrays.asList(ifNames);
    }
    
    public void output(final String[] args) throws SigarException {
        String[] ifNames;
        if (args.length == 1) {
            ifNames = args;
        }
        else {
            ifNames = this.proxy.getNetInterfaceList();
        }
        for (int i = 0; i < ifNames.length; ++i) {
            try {
                this.output(ifNames[i]);
            }
            catch (SigarException e) {
                this.println(ifNames[i] + "\t" + e.getMessage());
            }
        }
    }
    
    public void output(final String name) throws SigarException {
        final NetInterfaceConfig ifconfig = this.sigar.getNetInterfaceConfig(name);
        final long flags = ifconfig.getFlags();
        String hwaddr = "";
        if (!"00:00:00:00:00:00".equals(ifconfig.getHwaddr())) {
            hwaddr = " HWaddr " + ifconfig.getHwaddr();
        }
        if (!ifconfig.getName().equals(ifconfig.getDescription())) {
            this.println(ifconfig.getDescription());
        }
        this.println(ifconfig.getName() + "\t" + "Link encap:" + ifconfig.getType() + hwaddr);
        String ptp = "";
        if ((flags & 0x10L) > 0L) {
            ptp = "  P-t-P:" + ifconfig.getDestination();
        }
        String bcast = "";
        if ((flags & 0x2L) > 0L) {
            bcast = "  Bcast:" + ifconfig.getBroadcast();
        }
        this.println("\tinet addr:" + ifconfig.getAddress() + ptp + bcast + "  Mask:" + ifconfig.getNetmask());
        this.println("\t" + NetFlags.getIfFlagsString(flags) + " MTU:" + ifconfig.getMtu() + "  Metric:" + ifconfig.getMetric());
        try {
            final NetInterfaceStat ifstat = this.sigar.getNetInterfaceStat(name);
            this.println("\tRX packets:" + ifstat.getRxPackets() + " errors:" + ifstat.getRxErrors() + " dropped:" + ifstat.getRxDropped() + " overruns:" + ifstat.getRxOverruns() + " frame:" + ifstat.getRxFrame());
            this.println("\tTX packets:" + ifstat.getTxPackets() + " errors:" + ifstat.getTxErrors() + " dropped:" + ifstat.getTxDropped() + " overruns:" + ifstat.getTxOverruns() + " carrier:" + ifstat.getTxCarrier());
            this.println("\tcollisions:" + ifstat.getTxCollisions());
            final long rxBytes = ifstat.getRxBytes();
            final long txBytes = ifstat.getTxBytes();
            this.println("\tRX bytes:" + rxBytes + " (" + Sigar.formatSize(rxBytes) + ")" + "  " + "TX bytes:" + txBytes + " (" + Sigar.formatSize(txBytes) + ")");
        }
        catch (SigarException ex) {}
        this.println("");
    }
    
    public static void main(final String[] args) throws Exception {
        new Ifconfig().processCommand(args);
    }
}
