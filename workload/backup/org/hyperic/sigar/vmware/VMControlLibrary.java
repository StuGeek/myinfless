// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.vmware;

import org.hyperic.jni.ArchName;
import java.io.FileNotFoundException;
import org.hyperic.sigar.SigarLoader;
import java.util.List;
import org.hyperic.sigar.win32.Win32Exception;
import org.hyperic.sigar.win32.RegistryKey;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;

public class VMControlLibrary
{
    private static final boolean IS64;
    public static final String REGISTRY_ROOT = "SOFTWARE\\VMware, Inc.";
    public static final String PROP_VMCONTROL_SHLIB = "vmcontrol.shlib";
    private static final String VMWARE_LIB;
    private static final String VMCONTROL_TAR;
    private static final String VMCONTROL;
    private static final String VMCONTROL_DLL;
    private static final String VMCONTROL_SO;
    private static final String VMCONTROL_OBJ;
    private static final String GCC;
    private static final String TAR;
    private static final String LIBSSL;
    private static final String LIBCRYPTO;
    private static boolean isDebug;
    
    private static String getProperty(final String key, final String defval) {
        return System.getProperty("vmcontrol." + key, defval);
    }
    
    private static File getVMwareLib() {
        final String[] locations = { "/usr/lib/vmware", "/usr/local/lib/vmware" };
        for (int i = 0; i < locations.length; ++i) {
            final File lib = new File(locations[i]);
            if (lib.exists()) {
                return lib;
            }
        }
        for (int i = 0; i < locations.length; ++i) {
            final File lib = new File(locations[i] + "-api");
            if (lib.exists()) {
                return lib;
            }
        }
        return new File(locations[0]);
    }
    
    private static File getLib(final String name) {
        File lib = new File(VMControlLibrary.VMWARE_LIB, "lib/" + name);
        if (lib.isDirectory()) {
            lib = new File(lib, name);
        }
        return lib;
    }
    
    private static File getLibSSL() {
        return getLib(VMControlLibrary.LIBSSL);
    }
    
    private static File getLibCrypto() {
        return getLib(VMControlLibrary.LIBCRYPTO);
    }
    
    private static String toString(final String[] args) {
        final StringBuffer cmd = new StringBuffer();
        for (int i = 0; i < args.length; ++i) {
            if (cmd.length() != 0) {
                cmd.append(' ');
            }
            cmd.append("'").append(args[i]).append("'");
        }
        return cmd.toString();
    }
    
    private static void exec(final String[] args) throws IOException {
        final Process proc = Runtime.getRuntime().exec(args);
        try {
            final int exitVal = proc.waitFor();
            if (exitVal != 0) {
                final String msg = "exec(" + toString(args) + ") failed: " + exitVal;
                throw new IOException(msg);
            }
        }
        catch (InterruptedException ex) {}
        if (VMControlLibrary.isDebug) {
            System.out.println("exec(" + toString(args) + ") OK");
        }
    }
    
    public static String getSharedLibrary() {
        return System.getProperty("vmcontrol.shlib");
    }
    
    public static void setSharedLibrary(final String lib) {
        System.setProperty("vmcontrol.shlib", lib);
    }
    
    public static void link() throws IOException {
        link(VMControlLibrary.VMCONTROL_SO);
    }
    
    private static void linkWin32() {
        final List dlls = new ArrayList();
        RegistryKey root = null;
        try {
            root = RegistryKey.LocalMachine.openSubKey("SOFTWARE\\VMware, Inc.");
            final String[] keys = root.getSubKeyNames();
            for (int i = 0; i < keys.length; ++i) {
                final String name = keys[i];
                if (name.startsWith("VMware ")) {
                    RegistryKey subkey = null;
                    try {
                        subkey = root.openSubKey(name);
                        String path = subkey.getStringValue("InstallPath");
                        if (path != null) {
                            path = path.trim();
                            if (path.length() != 0) {
                                final File dll = new File(path + VMControlLibrary.VMCONTROL_DLL);
                                if (dll.exists()) {
                                    if (name.endsWith(" Server")) {
                                        dlls.add(0, dll.getPath());
                                    }
                                    else if (name.endsWith(" API")) {
                                        dlls.add(dll.getPath());
                                    }
                                }
                            }
                        }
                    }
                    catch (Win32Exception e) {}
                    finally {
                        if (subkey != null) {
                            subkey.close();
                        }
                    }
                }
            }
        }
        catch (Win32Exception e2) {}
        finally {
            if (root != null) {
                root.close();
            }
        }
        if (dlls.size() != 0) {
            setSharedLibrary(dlls.get(0));
        }
    }
    
    public static void link(final String name) throws IOException {
        if (SigarLoader.IS_WIN32) {
            linkWin32();
            return;
        }
        File out = new File(name).getAbsoluteFile();
        if (out.isDirectory()) {
            out = new File(out, VMControlLibrary.VMCONTROL_SO);
        }
        final boolean exists = out.exists();
        if (exists) {
            setSharedLibrary(out.getPath());
            return;
        }
        if (!new File(VMControlLibrary.VMCONTROL_TAR).exists()) {
            return;
        }
        final File dir = out.getParentFile();
        if (!dir.isDirectory() || !dir.canWrite()) {
            throw new IOException("Cannot write to: " + dir);
        }
        final File obj = new File(dir, VMControlLibrary.VMCONTROL_OBJ);
        if (!obj.exists()) {
            final String[] extract_args = { VMControlLibrary.TAR, "-xf", VMControlLibrary.VMCONTROL_TAR, "-C", dir.toString(), VMControlLibrary.VMCONTROL_OBJ };
            exec(extract_args);
        }
        final List link_args = new ArrayList();
        link_args.add(VMControlLibrary.GCC);
        link_args.add("-shared");
        link_args.add("-o");
        link_args.add(out.getPath());
        link_args.add(obj.getPath());
        if (VMControlLibrary.IS64) {
            link_args.add("-lcrypto");
            link_args.add("-lssl");
        }
        else {
            final File libssl = getLibSSL();
            final File libcrypto = getLibCrypto();
            if (!libssl.exists()) {
                throw new FileNotFoundException(libssl.toString());
            }
            if (!new File(libssl.getParent(), "libc.so.6").exists()) {
                final String rpath = "-Wl,-rpath";
                link_args.add("-Wl,-rpath");
                link_args.add(libssl.getParent());
                if (!libssl.getParent().equals(libcrypto.getParent())) {
                    link_args.add("-Wl,-rpath");
                    link_args.add(libcrypto.getParent());
                }
            }
            link_args.add(libssl.getPath());
            link_args.add(libcrypto.getPath());
        }
        exec(link_args.toArray(new String[0]));
        setSharedLibrary(out.getPath());
    }
    
    public static boolean isLoaded() {
        return VMwareObject.LOADED;
    }
    
    public static void main(final String[] args) throws Exception {
        VMControlLibrary.isDebug = true;
        if (args.length == 0) {
            link();
        }
        else {
            link(args[0]);
        }
        final String shlib = getSharedLibrary();
        if (shlib == null) {
            System.out.println("No library found");
        }
        else {
            System.out.println("vmcontrol.shlib=" + shlib + " (loaded=" + isLoaded() + ")");
        }
    }
    
    static {
        IS64 = ArchName.is64();
        VMWARE_LIB = getProperty("lib.vmware", getVMwareLib().getPath());
        VMCONTROL_TAR = getProperty("control.tar", VMControlLibrary.VMWARE_LIB + "/perl/control.tar");
        VMCONTROL = "vmcontrol" + (VMControlLibrary.IS64 ? "64" : "");
        VMCONTROL_DLL = VMControlLibrary.VMCONTROL + "lib.dll";
        VMCONTROL_SO = VMControlLibrary.VMCONTROL + ".so";
        VMCONTROL_OBJ = getProperty("vmcontrol.o", "control-only/" + VMControlLibrary.VMCONTROL + ".o");
        GCC = getProperty("bin.gcc", "/usr/bin/gcc");
        TAR = getProperty("bin.tar", "/bin/tar");
        LIBSSL = getProperty("libssl", "libssl.so.0.9.7");
        LIBCRYPTO = getProperty("libcrypto", "libcrypto.so.0.9.7");
        VMControlLibrary.isDebug = false;
    }
}
