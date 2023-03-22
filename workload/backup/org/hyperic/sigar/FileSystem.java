// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class FileSystem implements Serializable
{
    private static final long serialVersionUID = 9641L;
    String dirName;
    String devName;
    String typeName;
    String sysTypeName;
    String options;
    int type;
    long flags;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_NONE = 1;
    public static final int TYPE_LOCAL_DISK = 2;
    public static final int TYPE_NETWORK = 3;
    public static final int TYPE_RAM_DISK = 4;
    public static final int TYPE_CDROM = 5;
    public static final int TYPE_SWAP = 6;
    
    public FileSystem() {
        this.dirName = null;
        this.devName = null;
        this.typeName = null;
        this.sysTypeName = null;
        this.options = null;
        this.type = 0;
        this.flags = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static FileSystem fetch(final Sigar sigar) throws SigarException {
        final FileSystem fileSystem = new FileSystem();
        fileSystem.gather(sigar);
        return fileSystem;
    }
    
    public String getDirName() {
        return this.dirName;
    }
    
    public String getDevName() {
        return this.devName;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public String getSysTypeName() {
        return this.sysTypeName;
    }
    
    public String getOptions() {
        return this.options;
    }
    
    public int getType() {
        return this.type;
    }
    
    public long getFlags() {
        return this.flags;
    }
    
    void copyTo(final FileSystem copy) {
        copy.dirName = this.dirName;
        copy.devName = this.devName;
        copy.typeName = this.typeName;
        copy.sysTypeName = this.sysTypeName;
        copy.options = this.options;
        copy.type = this.type;
        copy.flags = this.flags;
    }
    
    public String toString() {
        return this.getDirName();
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strdirName = String.valueOf(this.dirName);
        if (!"-1".equals(strdirName)) {
            map.put("DirName", strdirName);
        }
        final String strdevName = String.valueOf(this.devName);
        if (!"-1".equals(strdevName)) {
            map.put("DevName", strdevName);
        }
        final String strtypeName = String.valueOf(this.typeName);
        if (!"-1".equals(strtypeName)) {
            map.put("TypeName", strtypeName);
        }
        final String strsysTypeName = String.valueOf(this.sysTypeName);
        if (!"-1".equals(strsysTypeName)) {
            map.put("SysTypeName", strsysTypeName);
        }
        final String stroptions = String.valueOf(this.options);
        if (!"-1".equals(stroptions)) {
            map.put("Options", stroptions);
        }
        final String strtype = String.valueOf(this.type);
        if (!"-1".equals(strtype)) {
            map.put("Type", strtype);
        }
        final String strflags = String.valueOf(this.flags);
        if (!"-1".equals(strflags)) {
            map.put("Flags", strflags);
        }
        return map;
    }
}
