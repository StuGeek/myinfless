// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.jmx;

public interface SigarProcessMBean
{
    Long getMemSize();
    
    Long getMemVsize();
    
    Long getMemResident();
    
    Long getMemShare();
    
    Long getMemPageFaults();
    
    Long getTimeUser();
    
    Long getTimeSys();
    
    Double getCpuUsage();
    
    Long getOpenFd();
}
