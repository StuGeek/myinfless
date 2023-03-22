// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcCredName implements Serializable
{
    private static final long serialVersionUID = 2266L;
    String user;
    String group;
    
    public ProcCredName() {
        this.user = null;
        this.group = null;
    }
    
    public native void gather(final Sigar p0, final long p1) throws SigarException;
    
    static ProcCredName fetch(final Sigar sigar, final long pid) throws SigarException {
        final ProcCredName procCredName = new ProcCredName();
        procCredName.gather(sigar, pid);
        return procCredName;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public String getGroup() {
        return this.group;
    }
    
    void copyTo(final ProcCredName copy) {
        copy.user = this.user;
        copy.group = this.group;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String struser = String.valueOf(this.user);
        if (!"-1".equals(struser)) {
            map.put("User", struser);
        }
        final String strgroup = String.valueOf(this.group);
        if (!"-1".equals(strgroup)) {
            map.put("Group", strgroup);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
