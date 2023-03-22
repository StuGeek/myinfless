// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import org.hyperic.sigar.Sigar;
import java.io.File;
import org.hyperic.sigar.SigarException;

public abstract class Win32
{
    public static final String EXE_EXT = ".exe";
    
    public static native String findExecutable(final String p0) throws SigarException;
    
    public static String findScriptExecutable(String name) {
        final int ix = name.lastIndexOf(".");
        if (ix == -1) {
            return null;
        }
        final String ext = name.substring(ix + 1);
        if (ext.equals("exe") || ext.equals("bat") || ext.equals("com")) {
            return null;
        }
        String exe;
        try {
            exe = findExecutable(new File(name).getAbsolutePath());
        }
        catch (SigarException e) {
            return null;
        }
        if (exe == null) {
            return null;
        }
        exe = exe.toLowerCase();
        name = name.toLowerCase();
        if (exe.equals(name) || exe.endsWith(name)) {
            return null;
        }
        final File file = new File(exe);
        if (file.getName().equals("wscript.exe")) {
            exe = file.getParent() + File.separator + "cscript.exe";
        }
        return exe;
    }
    
    public static FileVersion getFileVersion(final String name) {
        final FileVersion version = new FileVersion();
        if (version.gather(name)) {
            return version;
        }
        return null;
    }
    
    public static void main(final String[] args) throws Exception {
        for (int i = 0; i < args.length; ++i) {
            final String file = new File(args[i]).getAbsoluteFile().toString();
            final String exe = findScriptExecutable(file);
            if (exe != null) {
                System.out.println(args[i] + "=" + exe);
            }
        }
    }
    
    static {
        try {
            Sigar.load();
        }
        catch (SigarException ex) {}
    }
}
