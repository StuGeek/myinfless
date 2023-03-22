// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import java.lang.reflect.InvocationTargetException;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.Sigar;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.io.FileFilter;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class Runner
{
    private static HashMap wantedJars;
    private static final String JAR_EXT = ".jar";
    
    private static void printMissingJars() {
        for (final Map.Entry entry : Runner.wantedJars.entrySet()) {
            final String jar = entry.getKey();
            if (Runner.wantedJars.get(jar) == Boolean.FALSE) {
                System.out.println("Unable to locate: " + jar + ".jar");
            }
        }
    }
    
    private static boolean missingJars() {
        for (final Map.Entry entry : Runner.wantedJars.entrySet()) {
            final String jar = entry.getKey();
            if (Runner.wantedJars.get(jar) == Boolean.FALSE) {
                return true;
            }
        }
        return false;
    }
    
    public static URL[] getLibJars(final String dir) throws Exception {
        final File[] jars = new File(dir).listFiles(new FileFilter() {
            public boolean accept(final File file) {
                String name = file.getName();
                final int jarIx = name.indexOf(".jar");
                if (jarIx == -1) {
                    return false;
                }
                final int ix = name.indexOf(45);
                if (ix != -1) {
                    name = name.substring(0, ix);
                }
                else {
                    name = name.substring(0, jarIx);
                }
                if (Runner.wantedJars.get(name) != null) {
                    Runner.wantedJars.put(name, Boolean.TRUE);
                    return true;
                }
                return false;
            }
        });
        if (jars == null) {
            return new URL[0];
        }
        final URL[] urls = new URL[jars.length];
        for (int i = 0; i < jars.length; ++i) {
            final URL url = new URL("jar", null, "file:" + jars[i].getAbsolutePath() + "!/");
            urls[i] = url;
        }
        return urls;
    }
    
    private static void addURLs(final URL[] jars) throws Exception {
        final URLClassLoader loader = (URLClassLoader)Thread.currentThread().getContextClassLoader();
        final Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        for (int i = 0; i < jars.length; ++i) {
            addURL.invoke(loader, jars[i]);
        }
    }
    
    private static boolean addJarDir(final String dir) throws Exception {
        final URL[] jars = getLibJars(dir);
        addURLs(jars);
        return !missingJars();
    }
    
    private static String getenv(final String key) {
        try {
            return System.getenv("ANT_HOME");
        }
        catch (Error e) {
            final Sigar sigar = new Sigar();
            try {
                return sigar.getProcEnv("$$", "ANT_HOME");
            }
            catch (Exception se) {
                return null;
            }
            finally {
                sigar.close();
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            args = new String[] { "Shell" };
        }
        else if (Character.isLowerCase(args[0].charAt(0))) {
            final String[] nargs = new String[args.length + 1];
            System.arraycopy(args, 0, nargs, 1, args.length);
            nargs[0] = "Shell";
            args = nargs;
        }
        final String name = args[0];
        final String[] pargs = new String[args.length - 1];
        System.arraycopy(args, 1, pargs, 0, args.length - 1);
        final String sigarLib = SigarLoader.getLocation();
        final String[] dirs = { sigarLib, "lib", "." };
        for (int i = 0; i < dirs.length && !addJarDir(dirs[i]); ++i) {}
        if (missingJars()) {
            final File[] subdirs = new File(".").listFiles(new FileFilter() {
                public boolean accept(final File file) {
                    return file.isDirectory();
                }
            });
            for (int j = 0; j < subdirs.length; ++j) {
                final File lib = new File(subdirs[j], "lib");
                if (lib.exists() && addJarDir(lib.getAbsolutePath())) {
                    break;
                }
            }
            if (missingJars()) {
                final String home = getenv("ANT_HOME");
                if (home != null) {
                    addJarDir(home + "/lib");
                }
            }
        }
        Class cmd = null;
        final String[] packages = { "org.hyperic.sigar.cmd.", "org.hyperic.sigar.test.", "org.hyperic.sigar.", "org.hyperic.sigar.win32.", "org.hyperic.sigar.jmx." };
        int k = 0;
        while (k < packages.length) {
            try {
                cmd = Class.forName(packages[k] + name);
            }
            catch (ClassNotFoundException e2) {
                ++k;
                continue;
            }
            break;
        }
        if (cmd == null) {
            System.out.println("Unknown command: " + args[0]);
            return;
        }
        final Method main = cmd.getMethod("main", String[].class);
        try {
            main.invoke(null, pargs);
        }
        catch (InvocationTargetException e) {
            final Throwable t = e.getTargetException();
            if (t instanceof NoClassDefFoundError) {
                System.out.println("Class Not Found: " + t.getMessage());
                printMissingJars();
            }
            else {
                t.printStackTrace();
            }
        }
    }
    
    static {
        (Runner.wantedJars = new HashMap()).put("junit", Boolean.FALSE);
        Runner.wantedJars.put("log4j", Boolean.FALSE);
    }
}
