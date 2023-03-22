// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.vmware;

import java.io.File;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;

abstract class VMwareObject
{
    public static final boolean LOADED;
    int ptr;
    long ptr64;
    
    VMwareObject() {
        this.ptr = 0;
        this.ptr64 = 0L;
    }
    
    private static native boolean init(final String p0);
    
    private static boolean loadLibraries() {
        if (!SigarLoader.IS_LINUX && !SigarLoader.IS_WIN32) {
            return false;
        }
        try {
            Sigar.load();
            final String lib = VMControlLibrary.getSharedLibrary();
            if (lib == null) {
                return false;
            }
            if (SigarLoader.IS_WIN32) {
                final File root = new File(lib).getParentFile();
                final String[] libs = { "libeay32.dll", "ssleay32.dll" };
                for (int i = 0; i < libs.length; ++i) {
                    final File ssllib = new File(root, libs[i]);
                    if (!ssllib.exists()) {
                        return false;
                    }
                    try {
                        System.load(ssllib.getPath());
                    }
                    catch (UnsatisfiedLinkError e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return init(lib);
        }
        catch (Exception e2) {
            return false;
        }
    }
    
    abstract void destroy();
    
    public void dispose() {
        if (this.ptr != 0 || this.ptr64 != 0L) {
            this.destroy();
            this.ptr = 0;
            this.ptr64 = 0L;
        }
    }
    
    protected void finalize() {
        this.dispose();
    }
    
    static {
        LOADED = loadLibraries();
    }
}
