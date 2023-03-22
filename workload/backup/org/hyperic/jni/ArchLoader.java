// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.jni;

import java.util.StringTokenizer;
import java.net.URLDecoder;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;

public class ArchLoader
{
    private Object loadLock;
    private boolean loaded;
    private static final String osName;
    public static final boolean IS_WIN32;
    public static final boolean IS_AIX;
    public static final boolean IS_HPUX;
    public static final boolean IS_SOLARIS;
    public static final boolean IS_LINUX;
    public static final boolean IS_DARWIN;
    public static final boolean IS_OSF1;
    public static final boolean IS_FREEBSD;
    public static final boolean IS_NETWARE;
    private String packageName;
    private String name;
    private String resourcePath;
    private Class loaderClass;
    private String jarName;
    private String libName;
    private File nativeLibrary;
    private String version;
    
    public ArchLoader() {
        this.loadLock = new Object();
        this.loaded = false;
        this.libName = null;
    }
    
    public ArchLoader(final Class loaderClass) {
        this.loadLock = new Object();
        this.loaded = false;
        this.libName = null;
        this.setLoaderClass(loaderClass);
        String pname = loaderClass.getName();
        int ix = pname.lastIndexOf(".");
        pname = pname.substring(0, ix);
        this.setPackageName(pname);
        ix = pname.lastIndexOf(".");
        this.setName(pname.substring(ix + 1));
        this.setJarName(this.getName() + ".jar");
        this.setResourcePath(this.toResName(pname));
    }
    
    public Class getLoaderClass() {
        return this.loaderClass;
    }
    
    public void setLoaderClass(final Class value) {
        this.loaderClass = value;
    }
    
    public ClassLoader getClassLoader() {
        return this.getLoaderClass().getClassLoader();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String value) {
        this.name = value;
    }
    
    public String getPackageName() {
        return this.packageName;
    }
    
    public void setPackageName(final String value) {
        this.packageName = value;
    }
    
    public String getResourcePath() {
        return this.resourcePath;
    }
    
    public void setResourcePath(final String value) {
        this.resourcePath = value;
    }
    
    public String getJarName() {
        return this.jarName;
    }
    
    public void setJarName(final String value) {
        this.jarName = value;
    }
    
    public String getLibName() {
        return this.libName;
    }
    
    public void setLibName(final String value) {
        this.libName = value;
    }
    
    public String getArchLibName() throws ArchNotSupportedException {
        return this.getName() + "-" + ArchName.getName();
    }
    
    public String getDefaultLibName() throws ArchNotSupportedException {
        return System.getProperty(this.getPackageName() + ".libname", "java" + this.getArchLibName());
    }
    
    public File getNativeLibrary() {
        return this.nativeLibrary;
    }
    
    private String toResName(final String name) {
        final StringBuffer sb = new StringBuffer(name);
        for (int i = 0; i < sb.length(); ++i) {
            if (sb.charAt(i) == '.') {
                sb.setCharAt(i, '/');
            }
        }
        return sb.toString();
    }
    
    public static String getLibraryPrefix() {
        if (ArchLoader.IS_WIN32 || ArchLoader.IS_NETWARE) {
            return "";
        }
        return "lib";
    }
    
    public static String getLibraryExtension() {
        if (ArchLoader.IS_WIN32) {
            return ".dll";
        }
        if (ArchLoader.IS_NETWARE) {
            return ".nlm";
        }
        if (ArchLoader.IS_DARWIN) {
            return ".dylib";
        }
        if (ArchLoader.IS_HPUX) {
            return ".sl";
        }
        return ".so";
    }
    
    public String getLibraryName() throws ArchNotSupportedException {
        String libName;
        if ((libName = this.getLibName()) == null) {
            libName = this.getDefaultLibName();
            this.setLibName(libName);
        }
        final String prefix = getLibraryPrefix();
        final String ext = getLibraryExtension();
        return prefix + libName + ext;
    }
    
    public String getVersionedLibraryName() {
        if (this.version == null) {
            return null;
        }
        try {
            this.getLibraryName();
        }
        catch (ArchNotSupportedException e) {
            return null;
        }
        final String prefix = getLibraryPrefix();
        final String ext = getLibraryExtension();
        return prefix + this.libName + '-' + this.version + ext;
    }
    
    private boolean isJarURL(final URL url) {
        if (url == null) {
            return false;
        }
        final String name = url.getFile();
        String jarName = this.getJarName();
        if (name.indexOf(jarName) != -1) {
            return true;
        }
        int ix = jarName.indexOf(".jar");
        if (ix == -1) {
            return false;
        }
        jarName = jarName.substring(0, ix) + "-";
        ix = name.lastIndexOf(jarName);
        if (ix == -1) {
            return false;
        }
        jarName = name.substring(ix);
        ix = jarName.indexOf(".jar");
        if (ix == -1) {
            return false;
        }
        this.version = jarName.substring(jarName.indexOf(45) + 1, ix);
        jarName = jarName.substring(0, ix + 4);
        this.setJarName(jarName);
        return true;
    }
    
    public String findJarPath(final String libName) throws ArchLoaderException {
        return this.findJarPath(libName, true);
    }
    
    private String findJarPath(String libName, final boolean isRequired) throws ArchLoaderException {
        if (this.getJarName() == null) {
            throw new ArchLoaderException("jarName is null");
        }
        String path = this.getResourcePath();
        final ClassLoader loader = this.getClassLoader();
        URL url = loader.getResource(path);
        if (!this.isJarURL(url)) {
            url = null;
        }
        if (url == null && loader instanceof URLClassLoader) {
            final URL[] urls = ((URLClassLoader)loader).getURLs();
            for (int i = 0; i < urls.length; ++i) {
                if (this.isJarURL(urls[i])) {
                    url = urls[i];
                    break;
                }
            }
        }
        if (url != null) {
            path = url.getFile();
            if (path.startsWith("file:")) {
                path = path.substring(5);
            }
            File file;
            String jarName;
            for (file = new File(path), jarName = this.getJarName(); file != null && !file.getName().startsWith(jarName); file = file.getParentFile()) {}
            if (libName == null) {
                libName = jarName;
            }
            if (file != null && (file = file.getParentFile()) != null) {
                final String dir = URLDecoder.decode(file.toString());
                if (this.findNativeLibrary(dir, libName)) {
                    return dir;
                }
            }
            return null;
        }
        if (isRequired) {
            throw new ArchLoaderException("Unable to find " + this.getJarName());
        }
        return null;
    }
    
    protected void systemLoadLibrary(final String name) {
        System.loadLibrary(name);
    }
    
    protected void systemLoad(final String name) {
        System.load(name);
    }
    
    protected boolean containsNativeLibrary(final File dir, final String name) {
        if (name == null) {
            return false;
        }
        final File file = new File(dir, name);
        if (file.exists()) {
            this.nativeLibrary = file;
            return true;
        }
        return false;
    }
    
    protected boolean findNativeLibrary(final String dir, final String name) {
        final File path = new File(dir).getAbsoluteFile();
        return this.containsNativeLibrary(path, name) || this.containsNativeLibrary(path, this.getVersionedLibraryName()) || this.containsNativeLibrary(path, getLibraryPrefix() + this.getName() + getLibraryExtension());
    }
    
    protected boolean findInJavaLibraryPath(final String libName) {
        String path = System.getProperty("java.library.path", "");
        final StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);
        while (tok.hasMoreTokens()) {
            path = tok.nextToken();
            if (this.findNativeLibrary(path, libName)) {
                return true;
            }
        }
        return false;
    }
    
    protected void loadLibrary(String path) throws ArchNotSupportedException, ArchLoaderException {
        try {
            final String libName = this.getLibraryName();
            if (path == null) {
                path = System.getProperty(this.getPackageName() + ".path");
            }
            if (path != null) {
                if (path.equals("-")) {
                    return;
                }
                this.findJarPath(null, false);
                this.findNativeLibrary(path, libName);
            }
            else if (this.findJarPath(libName, false) == null) {
                this.findInJavaLibraryPath(libName);
            }
            if (this.nativeLibrary != null) {
                this.systemLoad(this.nativeLibrary.toString());
            }
            else {
                this.systemLoadLibrary(libName);
            }
        }
        catch (RuntimeException e) {
            String reason = e.getMessage();
            if (reason == null) {
                reason = e.getClass().getName();
            }
            final String msg = "Failed to load " + this.libName + ": " + reason;
            throw new ArchLoaderException(msg);
        }
    }
    
    public void load() throws ArchNotSupportedException, ArchLoaderException {
        this.load(null);
    }
    
    public void load(final String path) throws ArchNotSupportedException, ArchLoaderException {
        synchronized (this.loadLock) {
            if (this.loaded) {
                return;
            }
            this.loadLibrary(path);
            this.loaded = true;
        }
    }
    
    static {
        osName = System.getProperty("os.name");
        IS_WIN32 = ArchLoader.osName.startsWith("Windows");
        IS_AIX = ArchLoader.osName.equals("AIX");
        IS_HPUX = ArchLoader.osName.equals("HP-UX");
        IS_SOLARIS = ArchLoader.osName.equals("SunOS");
        IS_LINUX = ArchLoader.osName.equals("Linux");
        IS_DARWIN = (ArchLoader.osName.equals("Mac OS X") || ArchLoader.osName.equals("Darwin"));
        IS_OSF1 = ArchLoader.osName.equals("OSF1");
        IS_FREEBSD = ArchLoader.osName.equals("FreeBSD");
        IS_NETWARE = ArchLoader.osName.equals("NetWare");
    }
}
