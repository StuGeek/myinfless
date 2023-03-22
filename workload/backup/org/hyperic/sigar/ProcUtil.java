// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.io.File;

public class ProcUtil
{
    private static boolean isClassName(final String name) {
        final int len = name.length();
        if (len == 0) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            final char c = name.charAt(i);
            if (c != '.' && !Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
    
    public static String getJavaMainClass(final SigarProxy sigar, final long pid) throws SigarException {
        final String[] args = sigar.getProcArgs(pid);
        for (int i = 1; i < args.length; ++i) {
            final String arg = args[i];
            if (isClassName(arg.trim())) {
                return arg;
            }
            if (arg.equals("-jar")) {
                File file = new File(args[i + 1]);
                if (!file.isAbsolute()) {
                    try {
                        final String cwd = sigar.getProcExe(pid).getCwd();
                        file = new File(cwd + File.separator + file);
                    }
                    catch (SigarException ex) {}
                }
                if (file.exists()) {
                    JarFile jar = null;
                    try {
                        jar = new JarFile(file);
                        return jar.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
                    }
                    catch (IOException e) {}
                    finally {
                        if (jar != null) {
                            try {
                                jar.close();
                            }
                            catch (IOException ex2) {}
                        }
                    }
                }
                return file.toString();
            }
        }
        return null;
    }
    
    public static String getDescription(final SigarProxy sigar, final long pid) throws SigarException {
        final ProcState state = sigar.getProcState(pid);
        String name = state.getName();
        String[] args;
        try {
            args = sigar.getProcArgs(pid);
        }
        catch (SigarException e) {
            args = new String[0];
        }
        if (name.equals("java") || name.equals("javaw")) {
            String className = null;
            try {
                className = getJavaMainClass(sigar, pid);
            }
            catch (SigarException ex) {}
            if (className != null) {
                name = name + ":" + className;
            }
        }
        else if (args.length != 0) {
            name = args[0];
        }
        else {
            try {
                final String exe = sigar.getProcExe(pid).getName();
                if (exe.length() != 0) {
                    name = exe;
                }
            }
            catch (SigarException ex2) {}
        }
        return name;
    }
}
