// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class NetInterfaceConfig implements Serializable
{
    private static final long serialVersionUID = 15948L;
    String name;
    String hwaddr;
    String type;
    String description;
    String address;
    String destination;
    String broadcast;
    String netmask;
    long flags;
    long mtu;
    long metric;
    
    public NetInterfaceConfig() {
        this.name = null;
        this.hwaddr = null;
        this.type = null;
        this.description = null;
        this.address = null;
        this.destination = null;
        this.broadcast = null;
        this.netmask = null;
        this.flags = 0L;
        this.mtu = 0L;
        this.metric = 0L;
    }
    
    public native void gather(final Sigar p0, final String p1) throws SigarException;
    
    static NetInterfaceConfig fetch(final Sigar sigar, final String name) throws SigarException {
        final NetInterfaceConfig netInterfaceConfig = new NetInterfaceConfig();
        netInterfaceConfig.gather(sigar, name);
        return netInterfaceConfig;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getHwaddr() {
        return this.hwaddr;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public String getDestination() {
        return this.destination;
    }
    
    public String getBroadcast() {
        return this.broadcast;
    }
    
    public String getNetmask() {
        return this.netmask;
    }
    
    public long getFlags() {
        return this.flags;
    }
    
    public long getMtu() {
        return this.mtu;
    }
    
    public long getMetric() {
        return this.metric;
    }
    
    void copyTo(final NetInterfaceConfig copy) {
        copy.name = this.name;
        copy.hwaddr = this.hwaddr;
        copy.type = this.type;
        copy.description = this.description;
        copy.address = this.address;
        copy.destination = this.destination;
        copy.broadcast = this.broadcast;
        copy.netmask = this.netmask;
        copy.flags = this.flags;
        copy.mtu = this.mtu;
        copy.metric = this.metric;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strname = String.valueOf(this.name);
        if (!"-1".equals(strname)) {
            map.put("Name", strname);
        }
        final String strhwaddr = String.valueOf(this.hwaddr);
        if (!"-1".equals(strhwaddr)) {
            map.put("Hwaddr", strhwaddr);
        }
        final String strtype = String.valueOf(this.type);
        if (!"-1".equals(strtype)) {
            map.put("Type", strtype);
        }
        final String strdescription = String.valueOf(this.description);
        if (!"-1".equals(strdescription)) {
            map.put("Description", strdescription);
        }
        final String straddress = String.valueOf(this.address);
        if (!"-1".equals(straddress)) {
            map.put("Address", straddress);
        }
        final String strdestination = String.valueOf(this.destination);
        if (!"-1".equals(strdestination)) {
            map.put("Destination", strdestination);
        }
        final String strbroadcast = String.valueOf(this.broadcast);
        if (!"-1".equals(strbroadcast)) {
            map.put("Broadcast", strbroadcast);
        }
        final String strnetmask = String.valueOf(this.netmask);
        if (!"-1".equals(strnetmask)) {
            map.put("Netmask", strnetmask);
        }
        final String strflags = String.valueOf(this.flags);
        if (!"-1".equals(strflags)) {
            map.put("Flags", strflags);
        }
        final String strmtu = String.valueOf(this.mtu);
        if (!"-1".equals(strmtu)) {
            map.put("Mtu", strmtu);
        }
        final String strmetric = String.valueOf(this.metric);
        if (!"-1".equals(strmetric)) {
            map.put("Metric", strmetric);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
