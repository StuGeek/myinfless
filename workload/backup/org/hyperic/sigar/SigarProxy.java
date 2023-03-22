// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.List;
import java.util.Map;

public interface SigarProxy
{
    long getPid();
    
    long getServicePid(final String p0) throws SigarException;
    
    Mem getMem() throws SigarException;
    
    Swap getSwap() throws SigarException;
    
    Cpu getCpu() throws SigarException;
    
    CpuPerc getCpuPerc() throws SigarException;
    
    Uptime getUptime() throws SigarException;
    
    ResourceLimit getResourceLimit() throws SigarException;
    
    double[] getLoadAverage() throws SigarException;
    
    long[] getProcList() throws SigarException;
    
    ProcStat getProcStat() throws SigarException;
    
    ProcMem getProcMem(final long p0) throws SigarException;
    
    ProcMem getProcMem(final String p0) throws SigarException;
    
    ProcMem getMultiProcMem(final String p0) throws SigarException;
    
    ProcState getProcState(final long p0) throws SigarException;
    
    ProcState getProcState(final String p0) throws SigarException;
    
    ProcTime getProcTime(final long p0) throws SigarException;
    
    ProcTime getProcTime(final String p0) throws SigarException;
    
    ProcCpu getProcCpu(final long p0) throws SigarException;
    
    ProcCpu getProcCpu(final String p0) throws SigarException;
    
    MultiProcCpu getMultiProcCpu(final String p0) throws SigarException;
    
    ProcCred getProcCred(final long p0) throws SigarException;
    
    ProcCred getProcCred(final String p0) throws SigarException;
    
    ProcCredName getProcCredName(final long p0) throws SigarException;
    
    ProcCredName getProcCredName(final String p0) throws SigarException;
    
    ProcFd getProcFd(final long p0) throws SigarException;
    
    ProcFd getProcFd(final String p0) throws SigarException;
    
    ProcExe getProcExe(final long p0) throws SigarException;
    
    ProcExe getProcExe(final String p0) throws SigarException;
    
    String[] getProcArgs(final long p0) throws SigarException;
    
    String[] getProcArgs(final String p0) throws SigarException;
    
    Map getProcEnv(final long p0) throws SigarException;
    
    Map getProcEnv(final String p0) throws SigarException;
    
    String getProcEnv(final long p0, final String p1) throws SigarException;
    
    String getProcEnv(final String p0, final String p1) throws SigarException;
    
    List getProcModules(final long p0) throws SigarException;
    
    List getProcModules(final String p0) throws SigarException;
    
    long getProcPort(final int p0, final long p1) throws SigarException;
    
    long getProcPort(final String p0, final String p1) throws SigarException;
    
    FileSystem[] getFileSystemList() throws SigarException;
    
    FileSystemMap getFileSystemMap() throws SigarException;
    
    FileSystemUsage getMountedFileSystemUsage(final String p0) throws SigarException;
    
    FileSystemUsage getFileSystemUsage(final String p0) throws SigarException;
    
    DiskUsage getDiskUsage(final String p0) throws SigarException;
    
    FileInfo getFileInfo(final String p0) throws SigarException;
    
    FileInfo getLinkInfo(final String p0) throws SigarException;
    
    DirStat getDirStat(final String p0) throws SigarException;
    
    DirUsage getDirUsage(final String p0) throws SigarException;
    
    CpuInfo[] getCpuInfoList() throws SigarException;
    
    Cpu[] getCpuList() throws SigarException;
    
    CpuPerc[] getCpuPercList() throws SigarException;
    
    NetRoute[] getNetRouteList() throws SigarException;
    
    NetInterfaceConfig getNetInterfaceConfig(final String p0) throws SigarException;
    
    NetInterfaceConfig getNetInterfaceConfig() throws SigarException;
    
    NetInterfaceStat getNetInterfaceStat(final String p0) throws SigarException;
    
    String[] getNetInterfaceList() throws SigarException;
    
    NetConnection[] getNetConnectionList(final int p0) throws SigarException;
    
    String getNetListenAddress(final long p0) throws SigarException;
    
    String getNetListenAddress(final String p0) throws SigarException;
    
    NetStat getNetStat() throws SigarException;
    
    String getNetServicesName(final int p0, final long p1);
    
    Who[] getWhoList() throws SigarException;
    
    Tcp getTcp() throws SigarException;
    
    NfsClientV2 getNfsClientV2() throws SigarException;
    
    NfsServerV2 getNfsServerV2() throws SigarException;
    
    NfsClientV3 getNfsClientV3() throws SigarException;
    
    NfsServerV3 getNfsServerV3() throws SigarException;
    
    NetInfo getNetInfo() throws SigarException;
    
    String getFQDN() throws SigarException;
}
