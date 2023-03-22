// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class FileAttrs implements Serializable
{
    private static final long serialVersionUID = 10323L;
    long permissions;
    int type;
    long uid;
    long gid;
    long inode;
    long device;
    long nlink;
    long size;
    long atime;
    long ctime;
    long mtime;
    
    public FileAttrs() {
        this.permissions = 0L;
        this.type = 0;
        this.uid = 0L;
        this.gid = 0L;
        this.inode = 0L;
        this.device = 0L;
        this.nlink = 0L;
        this.size = 0L;
        this.atime = 0L;
        this.ctime = 0L;
        this.mtime = 0L;
    }
    
    public native void gather(final Sigar p0, final String p1) throws SigarException;
    
    static FileAttrs fetch(final Sigar sigar, final String name) throws SigarException {
        final FileAttrs fileAttrs = new FileAttrs();
        fileAttrs.gather(sigar, name);
        return fileAttrs;
    }
    
    public long getPermissions() {
        return this.permissions;
    }
    
    public int getType() {
        return this.type;
    }
    
    public long getUid() {
        return this.uid;
    }
    
    public long getGid() {
        return this.gid;
    }
    
    public long getInode() {
        return this.inode;
    }
    
    public long getDevice() {
        return this.device;
    }
    
    public long getNlink() {
        return this.nlink;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public long getAtime() {
        return this.atime;
    }
    
    public long getCtime() {
        return this.ctime;
    }
    
    public long getMtime() {
        return this.mtime;
    }
    
    void copyTo(final FileAttrs copy) {
        copy.permissions = this.permissions;
        copy.type = this.type;
        copy.uid = this.uid;
        copy.gid = this.gid;
        copy.inode = this.inode;
        copy.device = this.device;
        copy.nlink = this.nlink;
        copy.size = this.size;
        copy.atime = this.atime;
        copy.ctime = this.ctime;
        copy.mtime = this.mtime;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strpermissions = String.valueOf(this.permissions);
        if (!"-1".equals(strpermissions)) {
            map.put("Permissions", strpermissions);
        }
        final String strtype = String.valueOf(this.type);
        if (!"-1".equals(strtype)) {
            map.put("Type", strtype);
        }
        final String struid = String.valueOf(this.uid);
        if (!"-1".equals(struid)) {
            map.put("Uid", struid);
        }
        final String strgid = String.valueOf(this.gid);
        if (!"-1".equals(strgid)) {
            map.put("Gid", strgid);
        }
        final String strinode = String.valueOf(this.inode);
        if (!"-1".equals(strinode)) {
            map.put("Inode", strinode);
        }
        final String strdevice = String.valueOf(this.device);
        if (!"-1".equals(strdevice)) {
            map.put("Device", strdevice);
        }
        final String strnlink = String.valueOf(this.nlink);
        if (!"-1".equals(strnlink)) {
            map.put("Nlink", strnlink);
        }
        final String strsize = String.valueOf(this.size);
        if (!"-1".equals(strsize)) {
            map.put("Size", strsize);
        }
        final String stratime = String.valueOf(this.atime);
        if (!"-1".equals(stratime)) {
            map.put("Atime", stratime);
        }
        final String strctime = String.valueOf(this.ctime);
        if (!"-1".equals(strctime)) {
            map.put("Ctime", strctime);
        }
        final String strmtime = String.valueOf(this.mtime);
        if (!"-1".equals(strmtime)) {
            map.put("Mtime", strmtime);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
