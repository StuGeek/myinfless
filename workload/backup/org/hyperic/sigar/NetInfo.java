// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class NetInfo implements Serializable
{
    private static final long serialVersionUID = 9427L;
    String defaultGateway;
    String hostName;
    String domainName;
    String primaryDns;
    String secondaryDns;
    
    public NetInfo() {
        this.defaultGateway = null;
        this.hostName = null;
        this.domainName = null;
        this.primaryDns = null;
        this.secondaryDns = null;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static NetInfo fetch(final Sigar sigar) throws SigarException {
        final NetInfo netInfo = new NetInfo();
        netInfo.gather(sigar);
        return netInfo;
    }
    
    public String getDefaultGateway() {
        return this.defaultGateway;
    }
    
    public String getHostName() {
        return this.hostName;
    }
    
    public String getDomainName() {
        return this.domainName;
    }
    
    public String getPrimaryDns() {
        return this.primaryDns;
    }
    
    public String getSecondaryDns() {
        return this.secondaryDns;
    }
    
    void copyTo(final NetInfo copy) {
        copy.defaultGateway = this.defaultGateway;
        copy.hostName = this.hostName;
        copy.domainName = this.domainName;
        copy.primaryDns = this.primaryDns;
        copy.secondaryDns = this.secondaryDns;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strdefaultGateway = String.valueOf(this.defaultGateway);
        if (!"-1".equals(strdefaultGateway)) {
            map.put("DefaultGateway", strdefaultGateway);
        }
        final String strhostName = String.valueOf(this.hostName);
        if (!"-1".equals(strhostName)) {
            map.put("HostName", strhostName);
        }
        final String strdomainName = String.valueOf(this.domainName);
        if (!"-1".equals(strdomainName)) {
            map.put("DomainName", strdomainName);
        }
        final String strprimaryDns = String.valueOf(this.primaryDns);
        if (!"-1".equals(strprimaryDns)) {
            map.put("PrimaryDns", strprimaryDns);
        }
        final String strsecondaryDns = String.valueOf(this.secondaryDns);
        if (!"-1".equals(strsecondaryDns)) {
            map.put("SecondaryDns", strsecondaryDns);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
