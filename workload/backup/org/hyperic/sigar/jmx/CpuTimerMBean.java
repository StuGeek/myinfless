// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.jmx;

public interface CpuTimerMBean
{
    long getCpuTotal();
    
    long getCpuUser();
    
    long getCpuSys();
    
    double getCpuUsage();
    
    long getTotalTime();
    
    long getLastSampleTime();
}
