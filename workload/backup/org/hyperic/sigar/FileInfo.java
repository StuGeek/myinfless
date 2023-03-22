// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.io.Serializable;

public class FileInfo extends FileAttrs implements Serializable
{
    private static final long serialVersionUID = 607239L;
    private static final SimpleDateFormat DATE_FORMAT;
    String name;
    private transient Sigar sigar;
    private boolean dirStatEnabled;
    private DirStat stat;
    private boolean lstat;
    private FileInfo oldInfo;
    public static final int TYPE_NOFILE = 0;
    public static final int TYPE_REG = 1;
    public static final int TYPE_DIR = 2;
    public static final int TYPE_CHR = 3;
    public static final int TYPE_BLK = 4;
    public static final int TYPE_PIPE = 5;
    public static final int TYPE_LNK = 6;
    public static final int TYPE_SOCK = 7;
    public static final int TYPE_UNKFILE = 8;
    public static final int MODE_UREAD = 1024;
    public static final int MODE_UWRITE = 512;
    public static final int MODE_UEXECUTE = 256;
    public static final int MODE_GREAD = 64;
    public static final int MODE_GWRITE = 32;
    public static final int MODE_GEXECUTE = 16;
    public static final int MODE_WREAD = 4;
    public static final int MODE_WWRITE = 2;
    public static final int MODE_WEXECUTE = 1;
    
    public FileInfo() {
        this.dirStatEnabled = false;
        this.stat = null;
        this.oldInfo = null;
    }
    
    private static native String getTypeString(final int p0);
    
    native void gatherLink(final Sigar p0, final String p1) throws SigarException;
    
    public String getTypeString() {
        return getTypeString(this.type);
    }
    
    public char getTypeChar() {
        switch (this.type) {
            case 2: {
                return 'd';
            }
            case 3: {
                return 'c';
            }
            case 4: {
                return 'b';
            }
            case 5: {
                return 'p';
            }
            case 6: {
                return 'l';
            }
            case 7: {
                return 's';
            }
            default: {
                return '-';
            }
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public boolean equals(final Object o) {
        return o.equals(this.name);
    }
    
    private static native String getPermissionsString(final long p0);
    
    public String getPermissionsString() {
        return getPermissionsString(this.permissions);
    }
    
    private static native int getMode(final long p0);
    
    public int getMode() {
        return getMode(this.permissions);
    }
    
    public void enableDirStat(final boolean value) {
        this.dirStatEnabled = value;
        if (value) {
            if (this.type != 2) {
                throw new IllegalArgumentException(this.name + " is not a directory");
            }
            try {
                if (this.stat == null) {
                    this.stat = this.sigar.getDirStat(this.name);
                }
                else {
                    this.stat.gather(this.sigar, this.name);
                }
            }
            catch (SigarException ex) {}
        }
    }
    
    private StringBuffer format(final ArrayList changes) {
        final StringBuffer sb = new StringBuffer();
        if (changes.size() == 0) {
            return sb;
        }
        for (int size = changes.size(), i = 0; i < size; ++i) {
            sb.append('{').append(changes.get(i)).append('}');
        }
        return sb;
    }
    
    private static String formatDate(final long time) {
        return FileInfo.DATE_FORMAT.format(new Date(time));
    }
    
    public String diff() {
        if (this.oldInfo == null) {
            return "";
        }
        return this.diff(this.oldInfo);
    }
    
    public String diff(final DirStat stat) {
        final DirStat thisStat = this.stat;
        final ArrayList changes = new ArrayList();
        if (thisStat.files != stat.files) {
            changes.add(new Diff("Files", stat.getFiles(), thisStat.getFiles()));
        }
        if (thisStat.subdirs != stat.subdirs) {
            changes.add(new Diff("Subdirs", stat.getSubdirs(), thisStat.getSubdirs()));
        }
        if (thisStat.symlinks != stat.symlinks) {
            changes.add(new Diff("Symlinks", stat.getSymlinks(), thisStat.getSymlinks()));
        }
        if (thisStat.chrdevs != stat.chrdevs) {
            changes.add(new Diff("Chrdevs", stat.getChrdevs(), thisStat.getChrdevs()));
        }
        if (thisStat.blkdevs != stat.blkdevs) {
            changes.add(new Diff("Blkdevs", stat.getBlkdevs(), thisStat.getBlkdevs()));
        }
        if (thisStat.sockets != stat.sockets) {
            changes.add(new Diff("Sockets", stat.getSockets(), thisStat.getSockets()));
        }
        if (thisStat.total != stat.total) {
            changes.add(new Diff("Total", stat.getTotal(), thisStat.getTotal()));
        }
        return this.format(changes).toString();
    }
    
    public String diff(final FileInfo info) {
        final ArrayList changes = new ArrayList();
        if (this.getMtime() != info.getMtime()) {
            changes.add(new Diff("Mtime", formatDate(info.getMtime()), formatDate(this.getMtime())));
        }
        else {
            if (this.getCtime() == info.getCtime()) {
                return "";
            }
            changes.add(new Diff("Ctime", formatDate(info.getCtime()), formatDate(this.getCtime())));
        }
        if (this.getPermissions() != info.getPermissions()) {
            changes.add(new Diff("Perms", info.getPermissionsString(), this.getPermissionsString()));
        }
        if (this.getType() != info.getType()) {
            changes.add(new Diff("Type", info.getTypeString(), this.getTypeString()));
        }
        if (this.getUid() != info.getUid()) {
            changes.add(new Diff("Uid", info.getUid(), this.getUid()));
        }
        if (this.getGid() != info.getGid()) {
            changes.add(new Diff("Gid", info.getGid(), this.getGid()));
        }
        if (this.getSize() != info.getSize()) {
            changes.add(new Diff("Size", info.getSize(), this.getSize()));
        }
        if (!OperatingSystem.IS_WIN32) {
            if (this.getInode() != info.getInode()) {
                changes.add(new Diff("Inode", info.getInode(), this.getInode()));
            }
            if (this.getDevice() != info.getDevice()) {
                changes.add(new Diff("Device", info.getDevice(), this.getDevice()));
            }
            if (this.getNlink() != info.getNlink()) {
                changes.add(new Diff("Nlink", info.getNlink(), this.getNlink()));
            }
        }
        final StringBuffer sb = this.format(changes);
        if (this.dirStatEnabled) {
            sb.append(this.diff(info.stat));
        }
        return sb.toString();
    }
    
    public FileInfo getPreviousInfo() {
        return this.oldInfo;
    }
    
    public boolean modified() throws SigarException, SigarFileNotFoundException {
        if (this.oldInfo == null) {
            this.oldInfo = new FileInfo();
            if (this.dirStatEnabled) {
                this.oldInfo.stat = new DirStat();
            }
        }
        this.copyTo(this.oldInfo);
        if (this.dirStatEnabled) {
            this.stat.copyTo(this.oldInfo.stat);
        }
        this.stat();
        return this.mtime != this.oldInfo.mtime;
    }
    
    public boolean changed() throws SigarException, SigarFileNotFoundException {
        return this.modified() || this.ctime != this.oldInfo.ctime;
    }
    
    public void stat() throws SigarException, SigarFileNotFoundException {
        final long mtime = this.mtime;
        if (this.lstat) {
            this.gatherLink(this.sigar, this.name);
        }
        else {
            this.gather(this.sigar, this.name);
        }
        if (this.dirStatEnabled && mtime != this.mtime) {
            this.stat.gather(this.sigar, this.name);
        }
    }
    
    private static FileInfo fetchInfo(final Sigar sigar, final String name, final boolean followSymlinks) throws SigarException {
        final FileInfo info = new FileInfo();
        try {
            if (followSymlinks) {
                info.gather(sigar, name);
                info.lstat = false;
            }
            else {
                info.gatherLink(sigar, name);
                info.lstat = true;
            }
        }
        catch (SigarException e) {
            e.setMessage(name + ": " + e.getMessage());
            throw e;
        }
        info.sigar = sigar;
        info.name = name;
        return info;
    }
    
    static FileInfo fetchFileInfo(final Sigar sigar, final String name) throws SigarException {
        return fetchInfo(sigar, name, true);
    }
    
    static FileInfo fetchLinkInfo(final Sigar sigar, final String name) throws SigarException {
        return fetchInfo(sigar, name, false);
    }
    
    static {
        DATE_FORMAT = new SimpleDateFormat("MMM dd HH:mm");
    }
    
    private class Diff
    {
        private String attr;
        private String old;
        private String cur;
        
        Diff(final String attr, final String old, final String cur) {
            this.attr = attr;
            this.old = old;
            this.cur = cur;
        }
        
        Diff(final FileInfo fileInfo, final String attr, final int old, final int cur) {
            this(fileInfo, attr, String.valueOf(old), String.valueOf(cur));
        }
        
        Diff(final FileInfo fileInfo, final String attr, final long old, final long cur) {
            this(fileInfo, attr, String.valueOf(old), String.valueOf(cur));
        }
        
        public String toString() {
            return this.attr + ": " + this.old + "|" + this.cur;
        }
    }
}
