// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcExe implements Serializable
{
    private static final long serialVersionUID = 1997L;
    String name;
    String cwd;
    
    public ProcExe() {
        this.name = null;
        this.cwd = null;
    }
    
    public native void gather(final Sigar p0, final long p1) throws SigarException;
    
    static ProcExe fetch(final Sigar sigar, final long pid) throws SigarException {
        final ProcExe procExe = new ProcExe();
        procExe.gather(sigar, pid);
        return procExe;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getCwd() {
        return this.cwd;
    }
    
    void copyTo(final ProcExe copy) {
        copy.name = this.name;
        copy.cwd = this.cwd;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strname = String.valueOf(this.name);
        if (!"-1".equals(strname)) {
            map.put("Name", strname);
        }
        final String strcwd = String.valueOf(this.cwd);
        if (!"-1".equals(strcwd)) {
            map.put("Cwd", strcwd);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
