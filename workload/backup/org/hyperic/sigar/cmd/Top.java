// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.ProcCpu;
import java.util.List;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.ProcStat;

public class Top
{
    private static final int SLEEP_TIME = 5000;
    private static final String HEADER = "PID\tUSER\tSTIME\tSIZE\tRSS\tSHARE\tSTATE\tTIME\t%CPU\tCOMMAND";
    
    private static String toString(final ProcStat stat) {
        return stat.getTotal() + " processes: " + stat.getSleeping() + " sleeping, " + stat.getRunning() + " running, " + stat.getZombie() + " zombie, " + stat.getStopped() + " stopped... " + stat.getThreads() + " threads";
    }
    
    public static void main(final String[] args) throws Exception {
        final Sigar sigarImpl = new Sigar();
        final SigarProxy sigar = SigarProxyCache.newInstance(sigarImpl, 5000);
        while (true) {
            Shell.clearScreen();
            System.out.println(Uptime.getInfo(sigar));
            System.out.println(toString(sigar.getProcStat()));
            System.out.println(sigar.getCpuPerc());
            System.out.println(sigar.getMem());
            System.out.println(sigar.getSwap());
            System.out.println();
            System.out.println("PID\tUSER\tSTIME\tSIZE\tRSS\tSHARE\tSTATE\tTIME\t%CPU\tCOMMAND");
            final long[] pids = Shell.getPids(sigar, args);
            for (int i = 0; i < pids.length; ++i) {
                final long pid = pids[i];
                String cpuPerc = "?";
                List info;
                try {
                    info = Ps.getInfo(sigar, pid);
                }
                catch (SigarException e) {
                    continue;
                }
                try {
                    final ProcCpu cpu = sigar.getProcCpu(pid);
                    cpuPerc = CpuPerc.format(cpu.getPercent());
                }
                catch (SigarException ex) {}
                info.add(info.size() - 1, cpuPerc);
                System.out.println(Ps.join(info));
            }
            Thread.sleep(5000L);
            SigarProxyCache.clear(sigar);
        }
    }
}
