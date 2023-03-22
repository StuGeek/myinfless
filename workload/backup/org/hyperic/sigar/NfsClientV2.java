// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class NfsClientV2 implements Serializable
{
    private static final long serialVersionUID = 18751L;
    long _null;
    long getattr;
    long setattr;
    long root;
    long lookup;
    long readlink;
    long read;
    long writecache;
    long write;
    long create;
    long remove;
    long rename;
    long link;
    long symlink;
    long mkdir;
    long rmdir;
    long readdir;
    long fsstat;
    
    public NfsClientV2() {
        this._null = 0L;
        this.getattr = 0L;
        this.setattr = 0L;
        this.root = 0L;
        this.lookup = 0L;
        this.readlink = 0L;
        this.read = 0L;
        this.writecache = 0L;
        this.write = 0L;
        this.create = 0L;
        this.remove = 0L;
        this.rename = 0L;
        this.link = 0L;
        this.symlink = 0L;
        this.mkdir = 0L;
        this.rmdir = 0L;
        this.readdir = 0L;
        this.fsstat = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static NfsClientV2 fetch(final Sigar sigar) throws SigarException {
        final NfsClientV2 nfsClientV2 = new NfsClientV2();
        nfsClientV2.gather(sigar);
        return nfsClientV2;
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
    
    public long getRoot() {
        return this.root;
    }
    
    public long getLookup() {
        return this.lookup;
    }
    
    public long getReadlink() {
        return this.readlink;
    }
    
    public long getRead() {
        return this.read;
    }
    
    public long getWritecache() {
        return this.writecache;
    }
    
    public long getWrite() {
        return this.write;
    }
    
    public long getCreate() {
        return this.create;
    }
    
    public long getRemove() {
        return this.remove;
    }
    
    public long getRename() {
        return this.rename;
    }
    
    public long getLink() {
        return this.link;
    }
    
    public long getSymlink() {
        return this.symlink;
    }
    
    public long getMkdir() {
        return this.mkdir;
    }
    
    public long getRmdir() {
        return this.rmdir;
    }
    
    public long getReaddir() {
        return this.readdir;
    }
    
    public long getFsstat() {
        return this.fsstat;
    }
    
    void copyTo(final NfsClientV2 copy) {
        copy._null = this._null;
        copy.getattr = this.getattr;
        copy.setattr = this.setattr;
        copy.root = this.root;
        copy.lookup = this.lookup;
        copy.readlink = this.readlink;
        copy.read = this.read;
        copy.writecache = this.writecache;
        copy.write = this.write;
        copy.create = this.create;
        copy.remove = this.remove;
        copy.rename = this.rename;
        copy.link = this.link;
        copy.symlink = this.symlink;
        copy.mkdir = this.mkdir;
        copy.rmdir = this.rmdir;
        copy.readdir = this.readdir;
        copy.fsstat = this.fsstat;
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
        final String strroot = String.valueOf(this.root);
        if (!"-1".equals(strroot)) {
            map.put("Root", strroot);
        }
        final String strlookup = String.valueOf(this.lookup);
        if (!"-1".equals(strlookup)) {
            map.put("Lookup", strlookup);
        }
        final String strreadlink = String.valueOf(this.readlink);
        if (!"-1".equals(strreadlink)) {
            map.put("Readlink", strreadlink);
        }
        final String strread = String.valueOf(this.read);
        if (!"-1".equals(strread)) {
            map.put("Read", strread);
        }
        final String strwritecache = String.valueOf(this.writecache);
        if (!"-1".equals(strwritecache)) {
            map.put("Writecache", strwritecache);
        }
        final String strwrite = String.valueOf(this.write);
        if (!"-1".equals(strwrite)) {
            map.put("Write", strwrite);
        }
        final String strcreate = String.valueOf(this.create);
        if (!"-1".equals(strcreate)) {
            map.put("Create", strcreate);
        }
        final String strremove = String.valueOf(this.remove);
        if (!"-1".equals(strremove)) {
            map.put("Remove", strremove);
        }
        final String strrename = String.valueOf(this.rename);
        if (!"-1".equals(strrename)) {
            map.put("Rename", strrename);
        }
        final String strlink = String.valueOf(this.link);
        if (!"-1".equals(strlink)) {
            map.put("Link", strlink);
        }
        final String strsymlink = String.valueOf(this.symlink);
        if (!"-1".equals(strsymlink)) {
            map.put("Symlink", strsymlink);
        }
        final String strmkdir = String.valueOf(this.mkdir);
        if (!"-1".equals(strmkdir)) {
            map.put("Mkdir", strmkdir);
        }
        final String strrmdir = String.valueOf(this.rmdir);
        if (!"-1".equals(strrmdir)) {
            map.put("Rmdir", strrmdir);
        }
        final String strreaddir = String.valueOf(this.readdir);
        if (!"-1".equals(strreaddir)) {
            map.put("Readdir", strreaddir);
        }
        final String strfsstat = String.valueOf(this.fsstat);
        if (!"-1".equals(strfsstat)) {
            map.put("Fsstat", strfsstat);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
