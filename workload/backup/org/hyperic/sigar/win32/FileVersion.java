// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import java.util.LinkedHashMap;
import java.util.Map;

public class FileVersion
{
    private int product_major;
    private int product_minor;
    private int product_build;
    private int product_revision;
    private int file_major;
    private int file_minor;
    private int file_build;
    private int file_revision;
    private Map string_file_info;
    
    native boolean gather(final String p0);
    
    FileVersion() {
        this.string_file_info = new LinkedHashMap();
    }
    
    public int getProductMajor() {
        return this.product_major;
    }
    
    public int getProductMinor() {
        return this.product_minor;
    }
    
    public int getProductBuild() {
        return this.product_build;
    }
    
    public int getProductRevision() {
        return this.product_revision;
    }
    
    public int getFileMajor() {
        return this.file_major;
    }
    
    public int getFileMinor() {
        return this.file_minor;
    }
    
    public int getFileBuild() {
        return this.file_build;
    }
    
    public int getFileRevision() {
        return this.file_revision;
    }
    
    public Map getInfo() {
        return this.string_file_info;
    }
    
    private String toVersion(final int major, final int minor, final int build, final int revision) {
        return major + "." + minor + "." + build + "." + revision;
    }
    
    public String getProductVersion() {
        return this.toVersion(this.product_major, this.product_minor, this.product_build, this.product_revision);
    }
    
    public String getFileVersion() {
        return this.toVersion(this.file_major, this.file_minor, this.file_build, this.file_revision);
    }
}
