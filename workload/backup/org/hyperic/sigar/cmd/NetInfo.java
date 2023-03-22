// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.NetInterfaceConfig;

public class NetInfo extends SigarCommandBase
{
    public NetInfo(final Shell shell) {
        super(shell);
    }
    
    public NetInfo() {
    }
    
    public String getUsageShort() {
        return "Display network info";
    }
    
    public void output(final String[] args) throws SigarException {
        final NetInterfaceConfig config = this.sigar.getNetInterfaceConfig(null);
        this.println("primary interface....." + config.getName());
        this.println("primary ip address...." + config.getAddress());
        this.println("primary mac address..." + config.getHwaddr());
        this.println("primary netmask......." + config.getNetmask());
        final org.hyperic.sigar.NetInfo info = this.sigar.getNetInfo();
        this.println("host name............." + info.getHostName());
        this.println("domain name..........." + info.getDomainName());
        this.println("default gateway......." + info.getDefaultGateway());
        this.println("primary dns..........." + info.getPrimaryDns());
        this.println("secondary dns........." + info.getSecondaryDns());
    }
    
    public static void main(final String[] args) throws Exception {
        new NetInfo().processCommand(args);
    }
}
