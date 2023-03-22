// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.NetConnection;
import java.util.List;
import java.util.ArrayList;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Tcp;
import java.net.UnknownHostException;
import java.net.InetAddress;
import org.hyperic.sigar.NetFlags;

public class Netstat extends SigarCommandBase
{
    private static final int LADDR_LEN = 20;
    private static final int RADDR_LEN = 35;
    private static final String[] HEADER;
    private static boolean isNumeric;
    private static boolean wantPid;
    private static boolean isStat;
    
    public Netstat(final Shell shell) {
        super(shell);
    }
    
    public Netstat() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getUsageShort() {
        return "Display network connections";
    }
    
    public static int getFlags(final String[] args, int flags) {
        int proto_flags = 0;
        Netstat.isNumeric = false;
        Netstat.wantPid = false;
        Netstat.isStat = false;
        for (int i = 0; i < args.length; ++i) {
            final String arg = args[i];
            int j = 0;
            while (j < arg.length()) {
                switch (arg.charAt(j++)) {
                    case '-': {
                        continue;
                    }
                    case 'l': {
                        flags &= 0xFFFFFFFE;
                        flags |= 0x2;
                        continue;
                    }
                    case 'a': {
                        flags |= 0x3;
                        continue;
                    }
                    case 'n': {
                        Netstat.isNumeric = true;
                        continue;
                    }
                    case 'p': {
                        Netstat.wantPid = true;
                        continue;
                    }
                    case 's': {
                        Netstat.isStat = true;
                        continue;
                    }
                    case 't': {
                        proto_flags |= 0x10;
                        continue;
                    }
                    case 'u': {
                        proto_flags |= 0x20;
                        continue;
                    }
                    case 'w': {
                        proto_flags |= 0x40;
                        continue;
                    }
                    case 'x': {
                        proto_flags |= 0x80;
                        continue;
                    }
                    default: {
                        System.err.println("unknown option");
                        continue;
                    }
                }
            }
        }
        if (proto_flags != 0) {
            flags &= 0xFFFFFF0F;
            flags |= proto_flags;
        }
        return flags;
    }
    
    private String formatPort(final int proto, final long port) {
        if (port == 0L) {
            return "*";
        }
        if (!Netstat.isNumeric) {
            final String service = this.sigar.getNetServicesName(proto, port);
            if (service != null) {
                return service;
            }
        }
        return String.valueOf(port);
    }
    
    private String formatAddress(final int proto, final String ip, final long portnum, int max) {
        final String port = this.formatPort(proto, portnum);
        String address;
        if (NetFlags.isAnyAddress(ip)) {
            address = "*";
        }
        else if (Netstat.isNumeric) {
            address = ip;
        }
        else {
            try {
                address = InetAddress.getByName(ip).getHostName();
            }
            catch (UnknownHostException e) {
                address = ip;
            }
        }
        max -= port.length() + 1;
        if (address.length() > max) {
            address = address.substring(0, max);
        }
        return address + ":" + port;
    }
    
    private void outputTcpStats() throws SigarException {
        final Tcp stat = this.sigar.getTcp();
        final String dnt = "    ";
        this.println("    " + stat.getActiveOpens() + " active connections openings");
        this.println("    " + stat.getPassiveOpens() + " passive connection openings");
        this.println("    " + stat.getAttemptFails() + " failed connection attempts");
        this.println("    " + stat.getEstabResets() + " connection resets received");
        this.println("    " + stat.getCurrEstab() + " connections established");
        this.println("    " + stat.getInSegs() + " segments received");
        this.println("    " + stat.getOutSegs() + " segments send out");
        this.println("    " + stat.getRetransSegs() + " segments retransmited");
        this.println("    " + stat.getInErrs() + " bad segments received.");
        this.println("    " + stat.getOutRsts() + " resets sent");
    }
    
    private void outputStats(final int flags) throws SigarException {
        if ((flags & 0x10) != 0x0) {
            this.println("Tcp:");
            try {
                this.outputTcpStats();
            }
            catch (SigarException e) {
                this.println("    " + e);
            }
        }
    }
    
    public void output(final String[] args) throws SigarException {
        int flags = 241;
        if (args.length > 0) {
            flags = getFlags(args, flags);
            if (Netstat.isStat) {
                this.outputStats(flags);
                return;
            }
        }
        final NetConnection[] connections = this.sigar.getNetConnectionList(flags);
        this.printf(Netstat.HEADER);
        for (int i = 0; i < connections.length; ++i) {
            final NetConnection conn = connections[i];
            final String proto = conn.getTypeString();
            String state;
            if (conn.getType() == 32) {
                state = "";
            }
            else {
                state = conn.getStateString();
            }
            final ArrayList items = new ArrayList();
            items.add(proto);
            items.add(this.formatAddress(conn.getType(), conn.getLocalAddress(), conn.getLocalPort(), 20));
            items.add(this.formatAddress(conn.getType(), conn.getRemoteAddress(), conn.getRemotePort(), 35));
            items.add(state);
            String process = null;
            if (Netstat.wantPid && conn.getState() == 10) {
                try {
                    final long pid = this.sigar.getProcPort(conn.getType(), conn.getLocalPort());
                    if (pid != 0L) {
                        final String name = this.sigar.getProcState(pid).getName();
                        process = pid + "/" + name;
                    }
                }
                catch (SigarException ex) {}
            }
            if (process == null) {
                process = "";
            }
            items.add(process);
            this.printf(items);
        }
    }
    
    public static void main(final String[] args) throws Exception {
        new Netstat().processCommand(args);
    }
    
    static {
        HEADER = new String[] { "Proto", "Local Address", "Foreign Address", "State", "" };
    }
}
