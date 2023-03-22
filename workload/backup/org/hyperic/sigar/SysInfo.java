// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class SysInfo implements Serializable
{
    private static final long serialVersionUID = 16002L;
    String name;
    String version;
    String arch;
    String machine;
    String description;
    String patchLevel;
    String vendor;
    String vendorVersion;
    String vendorName;
    String vendorCodeName;
    
    public SysInfo() {
        this.name = null;
        this.version = null;
        this.arch = null;
        this.machine = null;
        this.description = null;
        this.patchLevel = null;
        this.vendor = null;
        this.vendorVersion = null;
        this.vendorName = null;
        this.vendorCodeName = null;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static SysInfo fetch(final Sigar sigar) throws SigarException {
        final SysInfo sysInfo = new SysInfo();
        sysInfo.gather(sigar);
        return sysInfo;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public String getArch() {
        return this.arch;
    }
    
    public String getMachine() {
        return this.machine;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getPatchLevel() {
        return this.patchLevel;
    }
    
    public String getVendor() {
        return this.vendor;
    }
    
    public String getVendorVersion() {
        return this.vendorVersion;
    }
    
    public String getVendorName() {
        return this.vendorName;
    }
    
    public String getVendorCodeName() {
        return this.vendorCodeName;
    }
    
    void copyTo(final SysInfo copy) {
        copy.name = this.name;
        copy.version = this.version;
        copy.arch = this.arch;
        copy.machine = this.machine;
        copy.description = this.description;
        copy.patchLevel = this.patchLevel;
        copy.vendor = this.vendor;
        copy.vendorVersion = this.vendorVersion;
        copy.vendorName = this.vendorName;
        copy.vendorCodeName = this.vendorCodeName;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strname = String.valueOf(this.name);
        if (!"-1".equals(strname)) {
            map.put("Name", strname);
        }
        final String strversion = String.valueOf(this.version);
        if (!"-1".equals(strversion)) {
            map.put("Version", strversion);
        }
        final String strarch = String.valueOf(this.arch);
        if (!"-1".equals(strarch)) {
            map.put("Arch", strarch);
        }
        final String strmachine = String.valueOf(this.machine);
        if (!"-1".equals(strmachine)) {
            map.put("Machine", strmachine);
        }
        final String strdescription = String.valueOf(this.description);
        if (!"-1".equals(strdescription)) {
            map.put("Description", strdescription);
        }
        final String strpatchLevel = String.valueOf(this.patchLevel);
        if (!"-1".equals(strpatchLevel)) {
            map.put("PatchLevel", strpatchLevel);
        }
        final String strvendor = String.valueOf(this.vendor);
        if (!"-1".equals(strvendor)) {
            map.put("Vendor", strvendor);
        }
        final String strvendorVersion = String.valueOf(this.vendorVersion);
        if (!"-1".equals(strvendorVersion)) {
            map.put("VendorVersion", strvendorVersion);
        }
        final String strvendorName = String.valueOf(this.vendorName);
        if (!"-1".equals(strvendorName)) {
            map.put("VendorName", strvendorName);
        }
        final String strvendorCodeName = String.valueOf(this.vendorCodeName);
        if (!"-1".equals(strvendorCodeName)) {
            map.put("VendorCodeName", strvendorCodeName);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
