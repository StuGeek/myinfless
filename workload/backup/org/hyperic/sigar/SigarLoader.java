// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import org.hyperic.jni.ArchLoaderException;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.jni.ArchName;
import org.hyperic.jni.ArchLoader;

public class SigarLoader extends ArchLoader
{
    public static final String PROP_SIGAR_JAR_NAME = "sigar.jar.name";
    private static String location;
    private static String nativeName;
    
    public SigarLoader(final Class loaderClass) {
        super(loaderClass);
    }
    
    public String getArchLibName() throws ArchNotSupportedException {
        return this.getName() + "-" + ArchName.getName();
    }
    
    public String getDefaultLibName() throws ArchNotSupportedException {
        return this.getArchLibName();
    }
    
    protected void systemLoadLibrary(final String name) {
        System.loadLibrary(name);
    }
    
    protected void systemLoad(final String name) {
        System.load(name);
    }
    
    public String getJarName() {
        return System.getProperty("sigar.jar.name", super.getJarName());
    }
    
    public static void setSigarJarName(final String jarName) {
        System.setProperty("sigar.jar.name", jarName);
    }
    
    public static String getSigarJarName() {
        return System.getProperty("sigar.jar.name");
    }
    
    public static synchronized String getLocation() {
        if (SigarLoader.location == null) {
            final SigarLoader loader = new SigarLoader(Sigar.class);
            try {
                SigarLoader.location = loader.findJarPath(getSigarJarName());
            }
            catch (ArchLoaderException e) {
                SigarLoader.location = ".";
            }
        }
        return SigarLoader.location;
    }
    
    public static synchronized String getNativeLibraryName() {
        if (SigarLoader.nativeName == null) {
            final SigarLoader loader = new SigarLoader(Sigar.class);
            try {
                SigarLoader.nativeName = loader.getLibraryName();
            }
            catch (ArchNotSupportedException e) {
                SigarLoader.nativeName = null;
            }
        }
        return SigarLoader.nativeName;
    }
    
    static {
        SigarLoader.location = null;
        SigarLoader.nativeName = null;
    }
}
