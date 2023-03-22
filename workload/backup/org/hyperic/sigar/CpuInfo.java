// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class CpuInfo implements Serializable
{
    private static final long serialVersionUID = 9710L;
    String vendor;
    String model;
    int mhz;
    long cacheSize;
    int totalCores;
    int totalSockets;
    int coresPerSocket;
    
    public CpuInfo() {
        this.vendor = null;
        this.model = null;
        this.mhz = 0;
        this.cacheSize = 0L;
        this.totalCores = 0;
        this.totalSockets = 0;
        this.coresPerSocket = 0;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static CpuInfo fetch(final Sigar sigar) throws SigarException {
        final CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.gather(sigar);
        return cpuInfo;
    }
    
    public String getVendor() {
        return this.vendor;
    }
    
    public String getModel() {
        return this.model;
    }
    
    public int getMhz() {
        return this.mhz;
    }
    
    public long getCacheSize() {
        return this.cacheSize;
    }
    
    public int getTotalCores() {
        return this.totalCores;
    }
    
    public int getTotalSockets() {
        return this.totalSockets;
    }
    
    public int getCoresPerSocket() {
        return this.coresPerSocket;
    }
    
    void copyTo(final CpuInfo copy) {
        copy.vendor = this.vendor;
        copy.model = this.model;
        copy.mhz = this.mhz;
        copy.cacheSize = this.cacheSize;
        copy.totalCores = this.totalCores;
        copy.totalSockets = this.totalSockets;
        copy.coresPerSocket = this.coresPerSocket;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strvendor = String.valueOf(this.vendor);
        if (!"-1".equals(strvendor)) {
            map.put("Vendor", strvendor);
        }
        final String strmodel = String.valueOf(this.model);
        if (!"-1".equals(strmodel)) {
            map.put("Model", strmodel);
        }
        final String strmhz = String.valueOf(this.mhz);
        if (!"-1".equals(strmhz)) {
            map.put("Mhz", strmhz);
        }
        final String strcacheSize = String.valueOf(this.cacheSize);
        if (!"-1".equals(strcacheSize)) {
            map.put("CacheSize", strcacheSize);
        }
        final String strtotalCores = String.valueOf(this.totalCores);
        if (!"-1".equals(strtotalCores)) {
            map.put("TotalCores", strtotalCores);
        }
        final String strtotalSockets = String.valueOf(this.totalSockets);
        if (!"-1".equals(strtotalSockets)) {
            map.put("TotalSockets", strtotalSockets);
        }
        final String strcoresPerSocket = String.valueOf(this.coresPerSocket);
        if (!"-1".equals(strcoresPerSocket)) {
            map.put("CoresPerSocket", strcoresPerSocket);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
