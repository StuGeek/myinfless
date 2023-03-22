// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class NetConnection implements Serializable
{
    private static final long serialVersionUID = 12776L;
    long localPort;
    String localAddress;
    long remotePort;
    String remoteAddress;
    int type;
    int state;
    long sendQueue;
    long receiveQueue;
    
    public NetConnection() {
        this.localPort = 0L;
        this.localAddress = null;
        this.remotePort = 0L;
        this.remoteAddress = null;
        this.type = 0;
        this.state = 0;
        this.sendQueue = 0L;
        this.receiveQueue = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static NetConnection fetch(final Sigar sigar) throws SigarException {
        final NetConnection netConnection = new NetConnection();
        netConnection.gather(sigar);
        return netConnection;
    }
    
    public long getLocalPort() {
        return this.localPort;
    }
    
    public String getLocalAddress() {
        return this.localAddress;
    }
    
    public long getRemotePort() {
        return this.remotePort;
    }
    
    public String getRemoteAddress() {
        return this.remoteAddress;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getState() {
        return this.state;
    }
    
    public long getSendQueue() {
        return this.sendQueue;
    }
    
    public long getReceiveQueue() {
        return this.receiveQueue;
    }
    
    void copyTo(final NetConnection copy) {
        copy.localPort = this.localPort;
        copy.localAddress = this.localAddress;
        copy.remotePort = this.remotePort;
        copy.remoteAddress = this.remoteAddress;
        copy.type = this.type;
        copy.state = this.state;
        copy.sendQueue = this.sendQueue;
        copy.receiveQueue = this.receiveQueue;
    }
    
    public native String getTypeString();
    
    public static native String getStateString(final int p0);
    
    public String getStateString() {
        return getStateString(this.state);
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strlocalPort = String.valueOf(this.localPort);
        if (!"-1".equals(strlocalPort)) {
            map.put("LocalPort", strlocalPort);
        }
        final String strlocalAddress = String.valueOf(this.localAddress);
        if (!"-1".equals(strlocalAddress)) {
            map.put("LocalAddress", strlocalAddress);
        }
        final String strremotePort = String.valueOf(this.remotePort);
        if (!"-1".equals(strremotePort)) {
            map.put("RemotePort", strremotePort);
        }
        final String strremoteAddress = String.valueOf(this.remoteAddress);
        if (!"-1".equals(strremoteAddress)) {
            map.put("RemoteAddress", strremoteAddress);
        }
        final String strtype = String.valueOf(this.type);
        if (!"-1".equals(strtype)) {
            map.put("Type", strtype);
        }
        final String strstate = String.valueOf(this.state);
        if (!"-1".equals(strstate)) {
            map.put("State", strstate);
        }
        final String strsendQueue = String.valueOf(this.sendQueue);
        if (!"-1".equals(strsendQueue)) {
            map.put("SendQueue", strsendQueue);
        }
        final String strreceiveQueue = String.valueOf(this.receiveQueue);
        if (!"-1".equals(strreceiveQueue)) {
            map.put("ReceiveQueue", strreceiveQueue);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
