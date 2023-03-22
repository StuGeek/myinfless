// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.hyperic.jni.ArchLoaderException;
import org.hyperic.jni.ArchNotSupportedException;
import java.io.File;
import java.util.StringTokenizer;
import org.hyperic.sigar.ptql.ProcessFinder;

public class Sigar implements SigarProxy
{
    private static String loadError;
    public static final long FIELD_NOTIMPL = -1L;
    public static final String VERSION_STRING = "1.6.4.129";
    public static final String NATIVE_VERSION_STRING;
    public static final String SCM_REVISION = "4b67f57";
    public static final String NATIVE_SCM_REVISION;
    public static final String BUILD_DATE = "04/28/2010 04:26 PM";
    public static final String NATIVE_BUILD_DATE;
    private static boolean enableLogging;
    private static SigarLoader loader;
    private FileSystemMap mounts;
    private boolean open;
    int sigarWrapper;
    long longSigarWrapper;
    private Cpu lastCpu;
    private Cpu[] lastCpuList;
    private ProcessFinder processFinder;
    
    private static void checkVersion(final String nativeVersionString) throws SigarException {
        final StringTokenizer javaVersion = new StringTokenizer("1.6.4.129", ".");
        final StringTokenizer nativeVersion = new StringTokenizer(nativeVersionString, ".");
        final String[] desc = { "major", "minor" };
        for (int i = 0; i < desc.length; ++i) {
            final String jv = javaVersion.hasMoreTokens() ? javaVersion.nextToken() : "0";
            final String nv = nativeVersion.hasMoreTokens() ? nativeVersion.nextToken() : "0";
            if (!jv.equals(nv)) {
                final String msg = desc[i] + " version mismatch: (" + jv + "!=" + nv + ") " + "java=" + "1.6.4.129" + ", native=" + nativeVersionString;
                throw new SigarException(msg);
            }
        }
    }
    
    public static void load() throws SigarException {
        if (Sigar.loadError != null) {
            throw new SigarException(Sigar.loadError);
        }
    }
    
    private static void loadLibrary() throws SigarException {
        try {
            if (SigarLoader.IS_WIN32 && System.getProperty("os.version").equals("4.0")) {
                final String lib = Sigar.loader.findJarPath("pdh.dll") + File.separator + "pdh.dll";
                Sigar.loader.systemLoad(lib);
            }
            Sigar.loader.load();
        }
        catch (ArchNotSupportedException e) {
            throw new SigarException(e.getMessage());
        }
        catch (ArchLoaderException e2) {
            throw new SigarException(e2.getMessage());
        }
        catch (UnsatisfiedLinkError e3) {
            throw new SigarException(e3.getMessage());
        }
    }
    
    public File getNativeLibrary() {
        return Sigar.loader.getNativeLibrary();
    }
    
    public static native String formatSize(final long p0);
    
    private static native String getNativeVersion();
    
    private static native String getNativeBuildDate();
    
    private static native String getNativeScmRevision();
    
    public Sigar() {
        this.mounts = null;
        this.open = false;
        this.sigarWrapper = 0;
        this.longSigarWrapper = 0L;
        this.processFinder = null;
        try {
            this.open();
            this.open = true;
        }
        catch (SigarException e) {
            if (Sigar.enableLogging) {
                e.printStackTrace();
            }
        }
        catch (UnsatisfiedLinkError e2) {
            if (Sigar.enableLogging) {
                e2.printStackTrace();
            }
        }
        if (Sigar.enableLogging) {
            this.enableLogging(true);
        }
    }
    
    protected void finalize() {
        this.close();
    }
    
    private native void open() throws SigarException;
    
    public synchronized void close() {
        if (this.open) {
            this.nativeClose();
            this.open = false;
        }
    }
    
    private native int nativeClose();
    
    public native long getPid();
    
    public native long getServicePid(final String p0) throws SigarException;
    
    public native void kill(final long p0, final int p1) throws SigarException;
    
    public void kill(final long pid, final String signame) throws SigarException {
        int signum;
        if (Character.isDigit(signame.charAt(0))) {
            try {
                signum = Integer.parseInt(signame);
            }
            catch (NumberFormatException e) {
                signum = -1;
            }
        }
        else {
            signum = getSigNum(signame);
        }
        if (signum < 0) {
            throw new SigarException(signame + ": invalid signal specification");
        }
        this.kill(pid, signum);
    }
    
    public static native int getSigNum(final String p0);
    
    public void kill(final String pid, final int signum) throws SigarException {
        this.kill(this.convertPid(pid), signum);
    }
    
    public Mem getMem() throws SigarException {
        return Mem.fetch(this);
    }
    
    public Swap getSwap() throws SigarException {
        return Swap.fetch(this);
    }
    
    public Cpu getCpu() throws SigarException {
        return Cpu.fetch(this);
    }
    
    static void pause(final int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {}
    }
    
    static void pause() {
        pause(500);
    }
    
    public CpuPerc getCpuPerc() throws SigarException {
        Cpu oldCpu;
        if (this.lastCpu == null) {
            oldCpu = this.getCpu();
            pause();
        }
        else {
            oldCpu = this.lastCpu;
        }
        this.lastCpu = this.getCpu();
        return CpuPerc.fetch(this, oldCpu, this.lastCpu);
    }
    
    public CpuPerc[] getCpuPercList() throws SigarException {
        Cpu[] oldCpuList;
        if (this.lastCpuList == null) {
            oldCpuList = this.getCpuList();
            pause();
        }
        else {
            oldCpuList = this.lastCpuList;
        }
        this.lastCpuList = this.getCpuList();
        final int curLen = this.lastCpuList.length;
        final int oldLen = oldCpuList.length;
        final CpuPerc[] perc = new CpuPerc[(curLen < oldLen) ? curLen : oldLen];
        for (int i = 0; i < curLen; ++i) {
            perc[i] = CpuPerc.fetch(this, oldCpuList[i], this.lastCpuList[i]);
        }
        return perc;
    }
    
    public ResourceLimit getResourceLimit() throws SigarException {
        return ResourceLimit.fetch(this);
    }
    
    public Uptime getUptime() throws SigarException {
        return Uptime.fetch(this);
    }
    
    public native double[] getLoadAverage() throws SigarException;
    
    public native long[] getProcList() throws SigarException;
    
    public ProcStat getProcStat() throws SigarException {
        return ProcStat.fetch(this);
    }
    
    private long convertPid(final String pid) throws SigarException {
        if (pid.equals("$$")) {
            return this.getPid();
        }
        if (Character.isDigit(pid.charAt(0))) {
            return Long.parseLong(pid);
        }
        if (this.processFinder == null) {
            this.processFinder = new ProcessFinder(this);
        }
        return this.processFinder.findSingleProcess(pid);
    }
    
    public ProcMem getProcMem(final long pid) throws SigarException {
        return ProcMem.fetch(this, pid);
    }
    
    public ProcMem getProcMem(final String pid) throws SigarException {
        return this.getProcMem(this.convertPid(pid));
    }
    
    public ProcMem getMultiProcMem(final String query) throws SigarException {
        return MultiProcMem.get(this, query);
    }
    
    public ProcState getProcState(final long pid) throws SigarException {
        return ProcState.fetch(this, pid);
    }
    
    public ProcState getProcState(final String pid) throws SigarException {
        return this.getProcState(this.convertPid(pid));
    }
    
    public ProcTime getProcTime(final long pid) throws SigarException {
        return ProcTime.fetch(this, pid);
    }
    
    public ProcTime getProcTime(final String pid) throws SigarException {
        return this.getProcTime(this.convertPid(pid));
    }
    
    public ProcCpu getProcCpu(final long pid) throws SigarException {
        return ProcCpu.fetch(this, pid);
    }
    
    public ProcCpu getProcCpu(final String pid) throws SigarException {
        return this.getProcCpu(this.convertPid(pid));
    }
    
    public MultiProcCpu getMultiProcCpu(final String query) throws SigarException {
        return MultiProcCpu.get(this, query);
    }
    
    public ProcCred getProcCred(final long pid) throws SigarException {
        return ProcCred.fetch(this, pid);
    }
    
    public ProcCred getProcCred(final String pid) throws SigarException {
        return this.getProcCred(this.convertPid(pid));
    }
    
    public ProcCredName getProcCredName(final long pid) throws SigarException {
        return ProcCredName.fetch(this, pid);
    }
    
    public ProcCredName getProcCredName(final String pid) throws SigarException {
        return this.getProcCredName(this.convertPid(pid));
    }
    
    public ProcFd getProcFd(final long pid) throws SigarException {
        return ProcFd.fetch(this, pid);
    }
    
    public ProcFd getProcFd(final String pid) throws SigarException {
        return this.getProcFd(this.convertPid(pid));
    }
    
    public ProcExe getProcExe(final long pid) throws SigarException {
        return ProcExe.fetch(this, pid);
    }
    
    public ProcExe getProcExe(final String pid) throws SigarException {
        return this.getProcExe(this.convertPid(pid));
    }
    
    public native String[] getProcArgs(final long p0) throws SigarException;
    
    public String[] getProcArgs(final String pid) throws SigarException {
        return this.getProcArgs(this.convertPid(pid));
    }
    
    public Map getProcEnv(final long pid) throws SigarException {
        return ProcEnv.getAll(this, pid);
    }
    
    public Map getProcEnv(final String pid) throws SigarException {
        return this.getProcEnv(this.convertPid(pid));
    }
    
    public String getProcEnv(final long pid, final String key) throws SigarException {
        return ProcEnv.getValue(this, pid, key);
    }
    
    public String getProcEnv(final String pid, final String key) throws SigarException {
        return this.getProcEnv(this.convertPid(pid), key);
    }
    
    private native List getProcModulesNative(final long p0) throws SigarException;
    
    public List getProcModules(final long pid) throws SigarException {
        return this.getProcModulesNative(pid);
    }
    
    public List getProcModules(final String pid) throws SigarException {
        return this.getProcModules(this.convertPid(pid));
    }
    
    public native long getProcPort(final int p0, final long p1) throws SigarException;
    
    public long getProcPort(final String protocol, final String port) throws SigarException {
        return this.getProcPort(NetFlags.getConnectionProtocol(protocol), Integer.parseInt(port));
    }
    
    public ThreadCpu getThreadCpu() throws SigarException {
        return ThreadCpu.fetch(this, 0L);
    }
    
    private native FileSystem[] getFileSystemListNative() throws SigarException;
    
    public FileSystem[] getFileSystemList() throws SigarException {
        final FileSystem[] fslist = this.getFileSystemListNative();
        if (this.mounts != null) {
            this.mounts.init(fslist);
        }
        return fslist;
    }
    
    public FileSystemUsage getFileSystemUsage(final String name) throws SigarException {
        if (name == null) {
            throw new SigarException("name cannot be null");
        }
        return FileSystemUsage.fetch(this, name);
    }
    
    public DiskUsage getDiskUsage(final String name) throws SigarException {
        if (name == null) {
            throw new SigarException("name cannot be null");
        }
        return DiskUsage.fetch(this, name);
    }
    
    public FileSystemUsage getMountedFileSystemUsage(final String name) throws SigarException, NfsUnreachableException {
        final FileSystem fs = this.getFileSystemMap().getFileSystem(name);
        if (fs == null) {
            throw new SigarException(name + " is not a mounted filesystem");
        }
        if (fs instanceof NfsFileSystem) {
            final NfsFileSystem nfs = (NfsFileSystem)fs;
            if (!nfs.ping()) {
                throw nfs.getUnreachableException();
            }
        }
        return FileSystemUsage.fetch(this, name);
    }
    
    public FileSystemMap getFileSystemMap() throws SigarException {
        if (this.mounts == null) {
            this.mounts = new FileSystemMap();
        }
        this.getFileSystemList();
        return this.mounts;
    }
    
    public FileInfo getFileInfo(final String name) throws SigarException {
        return FileInfo.fetchFileInfo(this, name);
    }
    
    public FileInfo getLinkInfo(final String name) throws SigarException {
        return FileInfo.fetchLinkInfo(this, name);
    }
    
    public DirStat getDirStat(final String name) throws SigarException {
        return DirStat.fetch(this, name);
    }
    
    public DirUsage getDirUsage(final String name) throws SigarException {
        return DirUsage.fetch(this, name);
    }
    
    public native CpuInfo[] getCpuInfoList() throws SigarException;
    
    private native Cpu[] getCpuListNative() throws SigarException;
    
    public Cpu[] getCpuList() throws SigarException {
        return this.getCpuListNative();
    }
    
    public native NetRoute[] getNetRouteList() throws SigarException;
    
    public native NetConnection[] getNetConnectionList(final int p0) throws SigarException;
    
    public native String getNetListenAddress(final long p0) throws SigarException;
    
    public String getNetListenAddress(final String port) throws SigarException {
        return this.getNetListenAddress(Long.parseLong(port));
    }
    
    public native String getNetServicesName(final int p0, final long p1);
    
    public NetStat getNetStat() throws SigarException {
        final NetStat netstat = new NetStat();
        netstat.stat(this);
        return netstat;
    }
    
    public NetStat getNetStat(final byte[] address, final long port) throws SigarException {
        final NetStat netstat = new NetStat();
        netstat.stat(this, address, port);
        return netstat;
    }
    
    public native Who[] getWhoList() throws SigarException;
    
    public Tcp getTcp() throws SigarException {
        return Tcp.fetch(this);
    }
    
    public NfsClientV2 getNfsClientV2() throws SigarException {
        return NfsClientV2.fetch(this);
    }
    
    public NfsServerV2 getNfsServerV2() throws SigarException {
        return NfsServerV2.fetch(this);
    }
    
    public NfsClientV3 getNfsClientV3() throws SigarException {
        return NfsClientV3.fetch(this);
    }
    
    public NfsServerV3 getNfsServerV3() throws SigarException {
        return NfsServerV3.fetch(this);
    }
    
    public NetInfo getNetInfo() throws SigarException {
        return NetInfo.fetch(this);
    }
    
    public NetInterfaceConfig getNetInterfaceConfig(final String name) throws SigarException {
        return NetInterfaceConfig.fetch(this, name);
    }
    
    public NetInterfaceConfig getNetInterfaceConfig() throws SigarException {
        final String[] interfaces = this.getNetInterfaceList();
        for (int i = 0; i < interfaces.length; ++i) {
            final String name = interfaces[i];
            NetInterfaceConfig ifconfig;
            try {
                ifconfig = this.getNetInterfaceConfig(name);
            }
            catch (SigarException e) {
                continue;
            }
            final long flags = ifconfig.getFlags();
            if ((flags & 0x1L) > 0L) {
                if ((flags & 0x10L) <= 0L) {
                    if ((flags & 0x8L) <= 0L) {
                        return ifconfig;
                    }
                }
            }
        }
        final String msg = "No ethernet interface available";
        throw new SigarException(msg);
    }
    
    public NetInterfaceStat getNetInterfaceStat(final String name) throws SigarException {
        return NetInterfaceStat.fetch(this, name);
    }
    
    public native String[] getNetInterfaceList() throws SigarException;
    
    static native String getPasswordNative(final String p0) throws IOException, SigarNotImplementedException;
    
    public static String getPassword(final String prompt) throws IOException {
        try {
            return getPasswordNative(prompt);
        }
        catch (IOException e) {
            throw e;
        }
        catch (SigarNotImplementedException e2) {
            System.out.print(prompt);
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        }
    }
    
    public native String getFQDN() throws SigarException;
    
    public void enableLogging(final boolean value) {
        if (value) {
            SigarLog.enable(this);
        }
        else {
            SigarLog.disable(this);
        }
    }
    
    static {
        Sigar.loadError = null;
        Sigar.enableLogging = "true".equals(System.getProperty("sigar.nativeLogging"));
        Sigar.loader = new SigarLoader(Sigar.class);
        String nativeVersion = "unknown";
        String nativeBuildDate = "unknown";
        String nativeScmRevision = "unknown";
        try {
            loadLibrary();
            nativeVersion = getNativeVersion();
            nativeBuildDate = getNativeBuildDate();
            nativeScmRevision = getNativeScmRevision();
            checkVersion(nativeVersion);
        }
        catch (SigarException e) {
            Sigar.loadError = e.getMessage();
            try {
                SigarLog.debug(Sigar.loadError, e);
            }
            catch (NoClassDefFoundError ne) {
                System.err.println(Sigar.loadError);
                e.printStackTrace();
            }
        }
        NATIVE_VERSION_STRING = nativeVersion;
        NATIVE_BUILD_DATE = nativeBuildDate;
        NATIVE_SCM_REVISION = nativeScmRevision;
    }
}
