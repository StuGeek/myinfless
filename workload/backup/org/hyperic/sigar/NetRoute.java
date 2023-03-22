// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class NetRoute implements Serializable
{
    private static final long serialVersionUID = 13039L;
    String destination;
    String gateway;
    long flags;
    long refcnt;
    long use;
    long metric;
    String mask;
    long mtu;
    long window;
    long irtt;
    String ifname;
    
    public NetRoute() {
        this.destination = null;
        this.gateway = null;
        this.flags = 0L;
        this.refcnt = 0L;
        this.use = 0L;
        this.metric = 0L;
        this.mask = null;
        this.mtu = 0L;
        this.window = 0L;
        this.irtt = 0L;
        this.ifname = null;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static NetRoute fetch(final Sigar sigar) throws SigarException {
        final NetRoute netRoute = new NetRoute();
        netRoute.gather(sigar);
        return netRoute;
    }
    
    public String getDestination() {
        return this.destination;
    }
    
    public String getGateway() {
        return this.gateway;
    }
    
    public long getFlags() {
        return this.flags;
    }
    
    public long getRefcnt() {
        return this.refcnt;
    }
    
    public long getUse() {
        return this.use;
    }
    
    public long getMetric() {
        return this.metric;
    }
    
    public String getMask() {
        return this.mask;
    }
    
    public long getMtu() {
        return this.mtu;
    }
    
    public long getWindow() {
        return this.window;
    }
    
    public long getIrtt() {
        return this.irtt;
    }
    
    public String getIfname() {
        return this.ifname;
    }
    
    void copyTo(final NetRoute copy) {
        copy.destination = this.destination;
        copy.gateway = this.gateway;
        copy.flags = this.flags;
        copy.refcnt = this.refcnt;
        copy.use = this.use;
        copy.metric = this.metric;
        copy.mask = this.mask;
        copy.mtu = this.mtu;
        copy.window = this.window;
        copy.irtt = this.irtt;
        copy.ifname = this.ifname;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strdestination = String.valueOf(this.destination);
        if (!"-1".equals(strdestination)) {
            map.put("Destination", strdestination);
        }
        final String strgateway = String.valueOf(this.gateway);
        if (!"-1".equals(strgateway)) {
            map.put("Gateway", strgateway);
        }
        final String strflags = String.valueOf(this.flags);
        if (!"-1".equals(strflags)) {
            map.put("Flags", strflags);
        }
        final String strrefcnt = String.valueOf(this.refcnt);
        if (!"-1".equals(strrefcnt)) {
            map.put("Refcnt", strrefcnt);
        }
        final String struse = String.valueOf(this.use);
        if (!"-1".equals(struse)) {
            map.put("Use", struse);
        }
        final String strmetric = String.valueOf(this.metric);
        if (!"-1".equals(strmetric)) {
            map.put("Metric", strmetric);
        }
        final String strmask = String.valueOf(this.mask);
        if (!"-1".equals(strmask)) {
            map.put("Mask", strmask);
        }
        final String strmtu = String.valueOf(this.mtu);
        if (!"-1".equals(strmtu)) {
            map.put("Mtu", strmtu);
        }
        final String strwindow = String.valueOf(this.window);
        if (!"-1".equals(strwindow)) {
            map.put("Window", strwindow);
        }
        final String strirtt = String.valueOf(this.irtt);
        if (!"-1".equals(strirtt)) {
            map.put("Irtt", strirtt);
        }
        final String strifname = String.valueOf(this.ifname);
        if (!"-1".equals(strifname)) {
            map.put("Ifname", strifname);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
