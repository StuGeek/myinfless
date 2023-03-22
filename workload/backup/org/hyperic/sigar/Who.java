// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Who implements Serializable
{
    private static final long serialVersionUID = 4241L;
    String user;
    String device;
    String host;
    long time;
    
    public Who() {
        this.user = null;
        this.device = null;
        this.host = null;
        this.time = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static Who fetch(final Sigar sigar) throws SigarException {
        final Who who = new Who();
        who.gather(sigar);
        return who;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public String getDevice() {
        return this.device;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public long getTime() {
        return this.time;
    }
    
    void copyTo(final Who copy) {
        copy.user = this.user;
        copy.device = this.device;
        copy.host = this.host;
        copy.time = this.time;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String struser = String.valueOf(this.user);
        if (!"-1".equals(struser)) {
            map.put("User", struser);
        }
        final String strdevice = String.valueOf(this.device);
        if (!"-1".equals(strdevice)) {
            map.put("Device", strdevice);
        }
        final String strhost = String.valueOf(this.host);
        if (!"-1".equals(strhost)) {
            map.put("Host", strhost);
        }
        final String strtime = String.valueOf(this.time);
        if (!"-1".equals(strtime)) {
            map.put("Time", strtime);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
