// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class NfsClientV3 implements Serializable
{
    private static final long serialVersionUID = 23335L;
    long _null;
    long getattr;
    long setattr;
    long lookup;
    long access;
    long readlink;
    long read;
    long write;
    long create;
    long mkdir;
    long symlink;
    long mknod;
    long remove;
    long rmdir;
    long rename;
    long link;
    long readdir;
    long readdirplus;
    long fsstat;
    long fsinfo;
    long pathconf;
    long commit;
    
    public NfsClientV3() {
        this._null = 0L;
        this.getattr = 0L;
        this.setattr = 0L;
        this.lookup = 0L;
        this.access = 0L;
        this.readlink = 0L;
        this.read = 0L;
        this.write = 0L;
        this.create = 0L;
        this.mkdir = 0L;
        this.symlink = 0L;
        this.mknod = 0L;
        this.remove = 0L;
        this.rmdir = 0L;
        this.rename = 0L;
        this.link = 0L;
        this.readdir = 0L;
        this.readdirplus = 0L;
        this.fsstat = 0L;
        this.fsinfo = 0L;
        this.pathconf = 0L;
        this.commit = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static NfsClientV3 fetch(final Sigar sigar) throws SigarException {
        final NfsClientV3 nfsClientV3 = new NfsClientV3();
        nfsClientV3.gather(sigar);
        return nfsClientV3;
    }
    
    public long getNull() {
        return this._null;
    }
    
    public long getGetattr() {
        return this.getattr;
    }
    
    public long getSetattr() {
        return this.setattr;
    }
    
    public long getLookup() {
        return this.lookup;
    }
    
    public long getAccess() {
        return this.access;
    }
    
    public long getReadlink() {
        return this.readlink;
    }
    
    public long getRead() {
        return this.read;
    }
    
    public long getWrite() {
        return this.write;
    }
    
    public long getCreate() {
        return this.create;
    }
    
    public long getMkdir() {
        return this.mkdir;
    }
    
    public long getSymlink() {
        return this.symlink;
    }
    
    public long getMknod() {
        return this.mknod;
    }
    
    public long getRemove() {
        return this.remove;
    }
    
    public long getRmdir() {
        return this.rmdir;
    }
    
    public long getRename() {
        return this.rename;
    }
    
    public long getLink() {
        return this.link;
    }
    
    public long getReaddir() {
        return this.readdir;
    }
    
    public long getReaddirplus() {
        return this.readdirplus;
    }
    
    public long getFsstat() {
        return this.fsstat;
    }
    
    public long getFsinfo() {
        return this.fsinfo;
    }
    
    public long getPathconf() {
        return this.pathconf;
    }
    
    public long getCommit() {
        return this.commit;
    }
    
    void copyTo(final NfsClientV3 copy) {
        copy._null = this._null;
        copy.getattr = this.getattr;
        copy.setattr = this.setattr;
        copy.lookup = this.lookup;
        copy.access = this.access;
        copy.readlink = this.readlink;
        copy.read = this.read;
        copy.write = this.write;
        copy.create = this.create;
        copy.mkdir = this.mkdir;
        copy.symlink = this.symlink;
        copy.mknod = this.mknod;
        copy.remove = this.remove;
        copy.rmdir = this.rmdir;
        copy.rename = this.rename;
        copy.link = this.link;
        copy.readdir = this.readdir;
        copy.readdirplus = this.readdirplus;
        copy.fsstat = this.fsstat;
        copy.fsinfo = this.fsinfo;
        copy.pathconf = this.pathconf;
        copy.commit = this.commit;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String str_null = String.valueOf(this._null);
        if (!"-1".equals(str_null)) {
            map.put("_null", str_null);
        }
        final String strgetattr = String.valueOf(this.getattr);
        if (!"-1".equals(strgetattr)) {
            map.put("Getattr", strgetattr);
        }
        final String strsetattr = String.valueOf(this.setattr);
        if (!"-1".equals(strsetattr)) {
            map.put("Setattr", strsetattr);
        }
        final String strlookup = String.valueOf(this.lookup);
        if (!"-1".equals(strlookup)) {
            map.put("Lookup", strlookup);
        }
        final String straccess = String.valueOf(this.access);
        if (!"-1".equals(straccess)) {
            map.put("Access", straccess);
        }
        final String strreadlink = String.valueOf(this.readlink);
        if (!"-1".equals(strreadlink)) {
            map.put("Readlink", strreadlink);
        }
        final String strread = String.valueOf(this.read);
        if (!"-1".equals(strread)) {
            map.put("Read", strread);
        }
        final String strwrite = String.valueOf(this.write);
        if (!"-1".equals(strwrite)) {
            map.put("Write", strwrite);
        }
        final String strcreate = String.valueOf(this.create);
        if (!"-1".equals(strcreate)) {
            map.put("Create", strcreate);
        }
        final String strmkdir = String.valueOf(this.mkdir);
        if (!"-1".equals(strmkdir)) {
            map.put("Mkdir", strmkdir);
        }
        final String strsymlink = String.valueOf(this.symlink);
        if (!"-1".equals(strsymlink)) {
            map.put("Symlink", strsymlink);
        }
        final String strmknod = String.valueOf(this.mknod);
        if (!"-1".equals(strmknod)) {
            map.put("Mknod", strmknod);
        }
        final String strremove = String.valueOf(this.remove);
        if (!"-1".equals(strremove)) {
            map.put("Remove", strremove);
        }
        final String strrmdir = String.valueOf(this.rmdir);
        if (!"-1".equals(strrmdir)) {
            map.put("Rmdir", strrmdir);
        }
        final String strrename = String.valueOf(this.rename);
        if (!"-1".equals(strrename)) {
            map.put("Rename", strrename);
        }
        final String strlink = String.valueOf(this.link);
        if (!"-1".equals(strlink)) {
            map.put("Link", strlink);
        }
        final String strreaddir = String.valueOf(this.readdir);
        if (!"-1".equals(strreaddir)) {
            map.put("Readdir", strreaddir);
        }
        final String strreaddirplus = String.valueOf(this.readdirplus);
        if (!"-1".equals(strreaddirplus)) {
            map.put("Readdirplus", strreaddirplus);
        }
        final String strfsstat = String.valueOf(this.fsstat);
        if (!"-1".equals(strfsstat)) {
            map.put("Fsstat", strfsstat);
        }
        final String strfsinfo = String.valueOf(this.fsinfo);
        if (!"-1".equals(strfsinfo)) {
            map.put("Fsinfo", strfsinfo);
        }
        final String strpathconf = String.valueOf(this.pathconf);
        if (!"-1".equals(strpathconf)) {
            map.put("Pathconf", strpathconf);
        }
        final String strcommit = String.valueOf(this.commit);
        if (!"-1".equals(strcommit)) {
            map.put("Commit", strcommit);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
