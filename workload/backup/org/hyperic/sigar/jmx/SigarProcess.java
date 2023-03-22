// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.jmx;

import org.hyperic.sigar.ProcFd;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Sigar;

public class SigarProcess implements SigarProcessMBean
{
    private Sigar sigarImpl;
    private SigarProxy sigar;
    
    public SigarProcess() {
        this(new Sigar());
    }
    
    public SigarProcess(final Sigar sigar) {
        this.sigarImpl = sigar;
        this.sigar = SigarProxyCache.newInstance(this.sigarImpl);
    }
    
    public void close() {
        this.sigarImpl.close();
    }
    
    private RuntimeException unexpectedError(final String type, final SigarException e) {
        final String msg = "Unexected error in Sigar.get" + type + ": " + e.getMessage();
        return new IllegalArgumentException(msg);
    }
    
    private synchronized ProcMem getMem() {
        try {
            final long pid = this.sigar.getPid();
            return this.sigar.getProcMem(pid);
        }
        catch (SigarException e) {
            throw this.unexpectedError("Mem", e);
        }
    }
    
    private synchronized ProcCpu getCpu() {
        try {
            final long pid = this.sigar.getPid();
            return this.sigar.getProcCpu(pid);
        }
        catch (SigarException e) {
            throw this.unexpectedError("Cpu", e);
        }
    }
    
    private synchronized ProcFd getFd() {
        try {
            final long pid = this.sigar.getPid();
            return this.sigar.getProcFd(pid);
        }
        catch (SigarException e) {
            throw this.unexpectedError("Fd", e);
        }
    }
    
    public Long getMemSize() {
        return new Long(this.getMem().getSize());
    }
    
    public Long getMemVsize() {
        return this.getMemSize();
    }
    
    public Long getMemResident() {
        return new Long(this.getMem().getResident());
    }
    
    public Long getMemShare() {
        return new Long(this.getMem().getShare());
    }
    
    public Long getMemPageFaults() {
        return new Long(this.getMem().getPageFaults());
    }
    
    public Long getTimeUser() {
        return new Long(this.getCpu().getUser());
    }
    
    public Long getTimeSys() {
        return new Long(this.getCpu().getSys());
    }
    
    public Double getCpuUsage() {
        return new Double(this.getCpu().getPercent());
    }
    
    public Long getOpenFd() {
        return new Long(this.getFd().getTotal());
    }
    
    public static void main(final String[] args) {
        final SigarProcessMBean proc = new SigarProcess();
        System.out.println("MemSize=" + proc.getMemSize());
        System.out.println("MemResident=" + proc.getMemResident());
        System.out.println("MemShared=" + proc.getMemShare());
        System.out.println("MemPageFaults=" + proc.getMemPageFaults());
        System.out.println("TimeUser=" + proc.getTimeUser());
        System.out.println("TimeSys=" + proc.getTimeSys());
        System.out.println("OpenFd=" + proc.getOpenFd());
    }
}
