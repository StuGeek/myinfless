// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.jni;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.tools.ant.BuildException;
import java.util.Arrays;
import java.io.FileFilter;
import java.io.File;
import org.apache.tools.ant.Task;

public class ArchNameTask extends Task
{
    public void execute() throws BuildException {
        final String osArch = System.getProperty("os.arch");
        final String osVers = System.getProperty("os.version");
        if (this.getProject().getProperty("jni.dmalloc") != null) {
            ArchName.useDmalloc = true;
        }
        String archName;
        try {
            archName = ArchName.getName();
        }
        catch (ArchNotSupportedException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(archName);
        this.getProject().setProperty("jni.libarch", archName);
        this.getProject().setProperty("jni.libpre", ArchLoader.getLibraryPrefix());
        this.getProject().setProperty("jni.libext", ArchLoader.getLibraryExtension());
        String compiler;
        if (ArchLoader.IS_WIN32) {
            compiler = "msvc";
        }
        else if (ArchLoader.IS_HPUX) {
            compiler = "hp";
        }
        else if (ArchLoader.IS_AIX) {
            compiler = "xlc_r";
        }
        else {
            compiler = "gcc";
            this.getProject().setProperty("jni.compiler.isgcc", "true");
        }
        this.getProject().setProperty("jni.compiler", compiler);
        if (ArchName.is64()) {
            this.getProject().setProperty("jni.arch64", "true");
            if (ArchLoader.IS_LINUX && !osArch.equals("ia64")) {
                this.getProject().setProperty("jni.gccm", "-m64");
            }
        }
        else if (ArchLoader.IS_LINUX && osArch.equals("s390")) {
            this.getProject().setProperty("jni.gccm", "-m31");
        }
        if (ArchLoader.IS_DARWIN) {
            final File[] sdks = new File("/Developer/SDKs").listFiles(new FileFilter() {
                public boolean accept(final File file) {
                    final String name = file.getName();
                    return name.startsWith("MacOSX10.") && name.endsWith(".sdk");
                }
            });
            if (sdks != null) {
                Arrays.sort(sdks);
                String prop = "uni.sdk";
                String sdk = this.getProject().getProperty(prop);
                String defaultMin = "10.3";
                if (sdk == null) {
                    final int ix = sdks.length - 1;
                    sdk = sdks[ix].getPath();
                    if (sdk.indexOf("10.6") != -1 && ix > 0) {
                        sdk = sdks[ix - 1].getPath();
                        defaultMin = "10.5";
                    }
                    this.getProject().setProperty(prop, sdk);
                }
                final String version = osVers.substring(0, 4);
                final int minorVers = Integer.parseInt(osVers.substring(3, 4));
                final boolean usingLatestSDK = sdk.indexOf(version) != -1;
                System.out.println("Using SDK=" + sdk);
                if (minorVers >= 6 && ArchName.is64() && usingLatestSDK) {
                    this.getProject().setProperty("jni.cc", "jni-cc");
                    this.getProject().setProperty("uni.arch", "i386");
                    System.out.println("Note: SDK version does not support ppc64");
                }
                prop = "osx.min";
                String min = this.getProject().getProperty(prop);
                if (min == null) {
                    min = defaultMin;
                    this.getProject().setProperty(prop, min);
                }
                System.out.println("Using -mmacosx-version-min=" + min);
            }
        }
        this.getProject().setProperty("jni.scmrev", this.getSourceRevision());
        String home = this.getProject().getProperty("jni.javahome");
        if (home == null) {
            home = System.getProperty("java.home");
        }
        File dir = new File(home);
        if (!new File(dir, "include").exists()) {
            dir = dir.getParentFile();
        }
        this.getProject().setProperty("jni.javahome", dir.getPath());
    }
    
    private String readLine(final String filename) {
        Reader reader = null;
        try {
            reader = new FileReader(filename);
            return new BufferedReader(reader).readLine();
        }
        catch (Exception e) {}
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Exception ex) {}
            }
        }
        return null;
    }
    
    private String getSourceRevision() {
        final String exported = "exported";
        final String sha1 = this.getGitSourceRevision();
        if (sha1 == null) {
            return "exported";
        }
        return sha1;
    }
    
    private String getGitSourceRevision() {
        String git = this.getProject().getProperty("jni.git");
        if (git == null) {
            git = ".git";
        }
        if (new File(git).exists()) {
            final String head = this.readLine(git + "/HEAD");
            if (head != null) {
                final String refp = "ref: ";
                String sha1;
                if (head.startsWith("ref: ")) {
                    final String ref = head.substring("ref: ".length()).trim();
                    sha1 = this.readLine(git + "/" + ref);
                }
                else {
                    sha1 = head;
                }
                return sha1.substring(0, 7);
            }
        }
        return null;
    }
}
