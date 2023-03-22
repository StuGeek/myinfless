// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class DirUsage implements Serializable
{
    private static final long serialVersionUID = 9250L;
    long total;
    long files;
    long subdirs;
    long symlinks;
    long chrdevs;
    long blkdevs;
    long sockets;
    long diskUsage;
    
    public DirUsage() {
        this.total = 0L;
        this.files = 0L;
        this.subdirs = 0L;
        this.symlinks = 0L;
        this.chrdevs = 0L;
        this.blkdevs = 0L;
        this.sockets = 0L;
        this.diskUsage = 0L;
    }
    
    public native void gather(final Sigar p0, final String p1) throws SigarException;
    
    static DirUsage fetch(final Sigar sigar, final String name) throws SigarException {
        final DirUsage dirUsage = new DirUsage();
        dirUsage.gather(sigar, name);
        return dirUsage;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    public long getFiles() {
        return this.files;
    }
    
    public long getSubdirs() {
        return this.subdirs;
    }
    
    public long getSymlinks() {
        return this.symlinks;
    }
    
    public long getChrdevs() {
        return this.chrdevs;
    }
    
    public long getBlkdevs() {
        return this.blkdevs;
    }
    
    public long getSockets() {
        return this.sockets;
    }
    
    public long getDiskUsage() {
        return this.diskUsage;
    }
    
    void copyTo(final DirUsage copy) {
        copy.total = this.total;
        copy.files = this.files;
        copy.subdirs = this.subdirs;
        copy.symlinks = this.symlinks;
        copy.chrdevs = this.chrdevs;
        copy.blkdevs = this.blkdevs;
        copy.sockets = this.sockets;
        copy.diskUsage = this.diskUsage;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        final String strfiles = String.valueOf(this.files);
        if (!"-1".equals(strfiles)) {
            map.put("Files", strfiles);
        }
        final String strsubdirs = String.valueOf(this.subdirs);
        if (!"-1".equals(strsubdirs)) {
            map.put("Subdirs", strsubdirs);
        }
        final String strsymlinks = String.valueOf(this.symlinks);
        if (!"-1".equals(strsymlinks)) {
            map.put("Symlinks", strsymlinks);
        }
        final String strchrdevs = String.valueOf(this.chrdevs);
        if (!"-1".equals(strchrdevs)) {
            map.put("Chrdevs", strchrdevs);
        }
        final String strblkdevs = String.valueOf(this.blkdevs);
        if (!"-1".equals(strblkdevs)) {
            map.put("Blkdevs", strblkdevs);
        }
        final String strsockets = String.valueOf(this.sockets);
        if (!"-1".equals(strsockets)) {
            map.put("Sockets", strsockets);
        }
        final String strdiskUsage = String.valueOf(this.diskUsage);
        if (!"-1".equals(strdiskUsage)) {
            map.put("DiskUsage", strdiskUsage);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
