// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.jni;

public class ArchName
{
    static boolean useDmalloc;
    
    public static String getName() throws ArchNotSupportedException {
        String name = getArchName();
        if (ArchName.useDmalloc) {
            name += "-dmalloc";
        }
        return name;
    }
    
    public static boolean is64() {
        return "64".equals(System.getProperty("sun.arch.data.model"));
    }
    
    private static String getArchName() throws ArchNotSupportedException {
        final String name = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        final String version = System.getProperty("os.version");
        String majorVersion = version.substring(0, 1);
        if (arch.endsWith("86")) {
            arch = "x86";
        }
        if (name.equals("Linux")) {
            return arch + "-linux";
        }
        if (name.indexOf("Windows") > -1) {
            return arch + "-winnt";
        }
        if (name.equals("SunOS")) {
            if (arch.startsWith("sparcv") && is64()) {
                arch = "sparc64";
            }
            return arch + "-solaris";
        }
        if (name.equals("HP-UX")) {
            if (arch.startsWith("IA64")) {
                arch = "ia64";
            }
            else {
                arch = "pa";
                if (is64()) {
                    arch += "64";
                }
            }
            if (version.indexOf("11") > -1) {
                return arch + "-hpux-11";
            }
        }
        else {
            if (name.equals("AIX")) {
                if (majorVersion.equals("6")) {
                    majorVersion = "5";
                }
                return arch + "-aix-" + majorVersion;
            }
            if (name.equals("Mac OS X") || name.equals("Darwin")) {
                if (is64()) {
                    return "universal64-macosx";
                }
                return "universal-macosx";
            }
            else {
                if (name.equals("FreeBSD")) {
                    return arch + "-freebsd-" + majorVersion;
                }
                if (name.equals("OpenBSD")) {
                    return arch + "-openbsd-" + majorVersion;
                }
                if (name.equals("NetBSD")) {
                    return arch + "-netbsd-" + majorVersion;
                }
                if (name.equals("OSF1")) {
                    return "alpha-osf1-" + majorVersion;
                }
                if (name.equals("NetWare")) {
                    return "x86-netware-" + majorVersion;
                }
            }
        }
        final String desc = arch + "-" + name + "-" + version;
        throw new ArchNotSupportedException("platform (" + desc + ") not supported");
    }
    
    private ArchName() {
    }
    
    static {
        ArchName.useDmalloc = (System.getProperty("jni.dmalloc") != null);
    }
}
