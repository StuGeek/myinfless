// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Properties;
import java.util.Map;

public class OperatingSystem extends SysInfo
{
    public static final String NAME_LINUX = "Linux";
    public static final String NAME_SOLARIS = "Solaris";
    public static final String NAME_HPUX = "HPUX";
    public static final String NAME_AIX = "AIX";
    public static final String NAME_MACOSX = "MacOSX";
    public static final String NAME_FREEBSD = "FreeBSD";
    public static final String NAME_OPENBSD = "OpenBSD";
    public static final String NAME_NETBSD = "NetBSD";
    public static final String NAME_WIN32 = "Win32";
    public static final String NAME_NETWARE = "NetWare";
    public static final String[] UNIX_NAMES;
    public static final String[] WIN32_NAMES;
    public static final String[] NAMES;
    public static final boolean IS_WIN32;
    private static final Map supportedPlatforms;
    private static OperatingSystem instance;
    private String dataModel;
    private String cpuEndian;
    
    public static boolean isSupported(final String name) {
        return OperatingSystem.supportedPlatforms.get(name) == Boolean.TRUE;
    }
    
    public static boolean isWin32(final String name) {
        return "Win32".equals(name);
    }
    
    private OperatingSystem() {
    }
    
    public static synchronized OperatingSystem getInstance() {
        if (OperatingSystem.instance == null) {
            final Sigar sigar = new Sigar();
            final OperatingSystem os = new OperatingSystem();
            try {
                os.gather(sigar);
            }
            catch (SigarException e) {
                throw new IllegalStateException(e.getMessage());
            }
            finally {
                sigar.close();
            }
            final Properties props = System.getProperties();
            os.dataModel = props.getProperty("sun.arch.data.model");
            os.cpuEndian = props.getProperty("sun.cpu.endian");
            OperatingSystem.instance = os;
        }
        return OperatingSystem.instance;
    }
    
    public String getDataModel() {
        return this.dataModel;
    }
    
    public String getCpuEndian() {
        return this.cpuEndian;
    }
    
    public static void main(final String[] args) {
        System.out.println("all.............." + Arrays.asList(OperatingSystem.NAMES));
        final OperatingSystem os = getInstance();
        System.out.println("description......" + os.getDescription());
        System.out.println("name............." + os.name);
        System.out.println("version.........." + os.version);
        System.out.println("arch............." + os.arch);
        System.out.println("patch level......" + os.patchLevel);
        System.out.println("vendor..........." + os.vendor);
        System.out.println("vendor name......" + os.vendorName);
        System.out.println("vendor version..." + os.vendorVersion);
    }
    
    static {
        UNIX_NAMES = new String[] { "Linux", "Solaris", "HPUX", "AIX", "MacOSX", "FreeBSD", "OpenBSD", "NetBSD" };
        WIN32_NAMES = new String[] { "Win32" };
        IS_WIN32 = (System.getProperty("os.name").indexOf("Windows") != -1);
        supportedPlatforms = new HashMap();
        final int len = OperatingSystem.UNIX_NAMES.length + OperatingSystem.WIN32_NAMES.length;
        final String[] all = new String[len];
        System.arraycopy(OperatingSystem.UNIX_NAMES, 0, all, 0, OperatingSystem.UNIX_NAMES.length);
        all[len - 1] = "Win32";
        NAMES = all;
        for (int i = 0; i < OperatingSystem.NAMES.length; ++i) {
            OperatingSystem.supportedPlatforms.put(OperatingSystem.NAMES[i], Boolean.TRUE);
        }
        OperatingSystem.instance = null;
    }
}
