// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class NetInterfaceStat implements Serializable
{
    private static final long serialVersionUID = 20008L;
    long rxBytes;
    long rxPackets;
    long rxErrors;
    long rxDropped;
    long rxOverruns;
    long rxFrame;
    long txBytes;
    long txPackets;
    long txErrors;
    long txDropped;
    long txOverruns;
    long txCollisions;
    long txCarrier;
    long speed;
    
    public NetInterfaceStat() {
        this.rxBytes = 0L;
        this.rxPackets = 0L;
        this.rxErrors = 0L;
        this.rxDropped = 0L;
        this.rxOverruns = 0L;
        this.rxFrame = 0L;
        this.txBytes = 0L;
        this.txPackets = 0L;
        this.txErrors = 0L;
        this.txDropped = 0L;
        this.txOverruns = 0L;
        this.txCollisions = 0L;
        this.txCarrier = 0L;
        this.speed = 0L;
    }
    
    public native void gather(final Sigar p0, final String p1) throws SigarException;
    
    static NetInterfaceStat fetch(final Sigar sigar, final String name) throws SigarException {
        final NetInterfaceStat netInterfaceStat = new NetInterfaceStat();
        netInterfaceStat.gather(sigar, name);
        return netInterfaceStat;
    }
    
    public long getRxBytes() {
        return this.rxBytes;
    }
    
    public long getRxPackets() {
        return this.rxPackets;
    }
    
    public long getRxErrors() {
        return this.rxErrors;
    }
    
    public long getRxDropped() {
        return this.rxDropped;
    }
    
    public long getRxOverruns() {
        return this.rxOverruns;
    }
    
    public long getRxFrame() {
        return this.rxFrame;
    }
    
    public long getTxBytes() {
        return this.txBytes;
    }
    
    public long getTxPackets() {
        return this.txPackets;
    }
    
    public long getTxErrors() {
        return this.txErrors;
    }
    
    public long getTxDropped() {
        return this.txDropped;
    }
    
    public long getTxOverruns() {
        return this.txOverruns;
    }
    
    public long getTxCollisions() {
        return this.txCollisions;
    }
    
    public long getTxCarrier() {
        return this.txCarrier;
    }
    
    public long getSpeed() {
        return this.speed;
    }
    
    void copyTo(final NetInterfaceStat copy) {
        copy.rxBytes = this.rxBytes;
        copy.rxPackets = this.rxPackets;
        copy.rxErrors = this.rxErrors;
        copy.rxDropped = this.rxDropped;
        copy.rxOverruns = this.rxOverruns;
        copy.rxFrame = this.rxFrame;
        copy.txBytes = this.txBytes;
        copy.txPackets = this.txPackets;
        copy.txErrors = this.txErrors;
        copy.txDropped = this.txDropped;
        copy.txOverruns = this.txOverruns;
        copy.txCollisions = this.txCollisions;
        copy.txCarrier = this.txCarrier;
        copy.speed = this.speed;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strrxBytes = String.valueOf(this.rxBytes);
        if (!"-1".equals(strrxBytes)) {
            map.put("RxBytes", strrxBytes);
        }
        final String strrxPackets = String.valueOf(this.rxPackets);
        if (!"-1".equals(strrxPackets)) {
            map.put("RxPackets", strrxPackets);
        }
        final String strrxErrors = String.valueOf(this.rxErrors);
        if (!"-1".equals(strrxErrors)) {
            map.put("RxErrors", strrxErrors);
        }
        final String strrxDropped = String.valueOf(this.rxDropped);
        if (!"-1".equals(strrxDropped)) {
            map.put("RxDropped", strrxDropped);
        }
        final String strrxOverruns = String.valueOf(this.rxOverruns);
        if (!"-1".equals(strrxOverruns)) {
            map.put("RxOverruns", strrxOverruns);
        }
        final String strrxFrame = String.valueOf(this.rxFrame);
        if (!"-1".equals(strrxFrame)) {
            map.put("RxFrame", strrxFrame);
        }
        final String strtxBytes = String.valueOf(this.txBytes);
        if (!"-1".equals(strtxBytes)) {
            map.put("TxBytes", strtxBytes);
        }
        final String strtxPackets = String.valueOf(this.txPackets);
        if (!"-1".equals(strtxPackets)) {
            map.put("TxPackets", strtxPackets);
        }
        final String strtxErrors = String.valueOf(this.txErrors);
        if (!"-1".equals(strtxErrors)) {
            map.put("TxErrors", strtxErrors);
        }
        final String strtxDropped = String.valueOf(this.txDropped);
        if (!"-1".equals(strtxDropped)) {
            map.put("TxDropped", strtxDropped);
        }
        final String strtxOverruns = String.valueOf(this.txOverruns);
        if (!"-1".equals(strtxOverruns)) {
            map.put("TxOverruns", strtxOverruns);
        }
        final String strtxCollisions = String.valueOf(this.txCollisions);
        if (!"-1".equals(strtxCollisions)) {
            map.put("TxCollisions", strtxCollisions);
        }
        final String strtxCarrier = String.valueOf(this.txCarrier);
        if (!"-1".equals(strtxCarrier)) {
            map.put("TxCarrier", strtxCarrier);
        }
        final String strspeed = String.valueOf(this.speed);
        if (!"-1".equals(strspeed)) {
            map.put("Speed", strspeed);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
