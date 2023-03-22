// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Uptime implements Serializable
{
    private static final long serialVersionUID = 1263L;
    double uptime;
    
    public Uptime() {
        this.uptime = 0.0;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static Uptime fetch(final Sigar sigar) throws SigarException {
        final Uptime uptime = new Uptime();
        uptime.gather(sigar);
        return uptime;
    }
    
    public double getUptime() {
        return this.uptime;
    }
    
    void copyTo(final Uptime copy) {
        copy.uptime = this.uptime;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String struptime = String.valueOf(this.uptime);
        if (!"-1".equals(struptime)) {
            map.put("Uptime", struptime);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
