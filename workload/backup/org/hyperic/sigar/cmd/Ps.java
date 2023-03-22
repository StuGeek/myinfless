// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import java.util.Date;
import java.text.SimpleDateFormat;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcCredName;
import org.hyperic.sigar.ProcTime;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import java.util.ArrayList;
import org.hyperic.sigar.SigarProxy;
import java.util.Iterator;
import java.util.List;
import org.hyperic.sigar.SigarException;

public class Ps extends SigarCommandBase
{
    public Ps(final Shell shell) {
        super(shell);
    }
    
    public Ps() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getSyntaxArgs() {
        return "[pid|query]";
    }
    
    public String getUsageShort() {
        return "Show process status";
    }
    
    public boolean isPidCompleter() {
        return true;
    }
    
    public void output(final String[] args) throws SigarException {
        long[] pids;
        if (args.length == 0) {
            pids = this.proxy.getProcList();
        }
        else {
            pids = this.shell.findPids(args);
        }
        for (int i = 0; i < pids.length; ++i) {
            final long pid = pids[i];
            try {
                this.output(pid);
            }
            catch (SigarException e) {
                this.err.println("Exception getting process info for " + pid + ": " + e.getMessage());
            }
        }
    }
    
    public static String join(final List info) {
        final StringBuffer buf = new StringBuffer();
        final Iterator i = info.iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            buf.append(i.next());
            hasNext = i.hasNext();
            if (hasNext) {
                buf.append("\t");
            }
        }
        return buf.toString();
    }
    
    public static List getInfo(final SigarProxy sigar, final long pid) throws SigarException {
        final ProcState state = sigar.getProcState(pid);
        ProcTime time = null;
        final String unknown = "???";
        final List info = new ArrayList();
        info.add(String.valueOf(pid));
        try {
            final ProcCredName cred = sigar.getProcCredName(pid);
            info.add(cred.getUser());
        }
        catch (SigarException e) {
            info.add(unknown);
        }
        try {
            time = sigar.getProcTime(pid);
            info.add(getStartTime(time.getStartTime()));
        }
        catch (SigarException e) {
            info.add(unknown);
        }
        try {
            final ProcMem mem = sigar.getProcMem(pid);
            info.add(Sigar.formatSize(mem.getSize()));
            info.add(Sigar.formatSize(mem.getRss()));
            info.add(Sigar.formatSize(mem.getShare()));
        }
        catch (SigarException e) {
            info.add(unknown);
        }
        info.add(String.valueOf(state.getState()));
        if (time != null) {
            info.add(getCpuTime(time));
        }
        else {
            info.add(unknown);
        }
        final String name = ProcUtil.getDescription(sigar, pid);
        info.add(name);
        return info;
    }
    
    public void output(final long pid) throws SigarException {
        this.println(join(getInfo(this.proxy, pid)));
    }
    
    public static String getCpuTime(final long total) {
        final long t = total / 1000L;
        return t / 60L + ":" + t % 60L;
    }
    
    public static String getCpuTime(final ProcTime time) {
        return getCpuTime(time.getTotal());
    }
    
    private static String getStartTime(final long time) {
        if (time == 0L) {
            return "00:00";
        }
        final long timeNow = System.currentTimeMillis();
        String fmt = "MMMd";
        if (timeNow - time < 86400000L) {
            fmt = "HH:mm";
        }
        return new SimpleDateFormat(fmt).format(new Date(time));
    }
    
    public static void main(final String[] args) throws Exception {
        new Ps().processCommand(args);
    }
}
