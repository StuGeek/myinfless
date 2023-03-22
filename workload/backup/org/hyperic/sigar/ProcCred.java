// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcCred implements Serializable
{
    private static final long serialVersionUID = 3062L;
    long uid;
    long gid;
    long euid;
    long egid;
    
    public ProcCred() {
        this.uid = 0L;
        this.gid = 0L;
        this.euid = 0L;
        this.egid = 0L;
    }
    
    public native void gather(final Sigar p0, final long p1) throws SigarException;
    
    static ProcCred fetch(final Sigar sigar, final long pid) throws SigarException {
        final ProcCred procCred = new ProcCred();
        procCred.gather(sigar, pid);
        return procCred;
    }
    
    public long getUid() {
        return this.uid;
    }
    
    public long getGid() {
        return this.gid;
    }
    
    public long getEuid() {
        return this.euid;
    }
    
    public long getEgid() {
        return this.egid;
    }
    
    void copyTo(final ProcCred copy) {
        copy.uid = this.uid;
        copy.gid = this.gid;
        copy.euid = this.euid;
        copy.egid = this.egid;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String struid = String.valueOf(this.uid);
        if (!"-1".equals(struid)) {
            map.put("Uid", struid);
        }
        final String strgid = String.valueOf(this.gid);
        if (!"-1".equals(strgid)) {
            map.put("Gid", strgid);
        }
        final String streuid = String.valueOf(this.euid);
        if (!"-1".equals(streuid)) {
            map.put("Euid", streuid);
        }
        final String stregid = String.valueOf(this.egid);
        if (!"-1".equals(stregid)) {
            map.put("Egid", stregid);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
