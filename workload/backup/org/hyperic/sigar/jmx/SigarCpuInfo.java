// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.jmx;

import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Sigar;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;

public class SigarCpuInfo extends AbstractMBean
{
    private static final String MBEAN_TYPE = "CpuInfoList";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_CPUINDEX;
    private static final MBeanAttributeInfo MBEAN_ATTR_CACHESIZE;
    private static final MBeanAttributeInfo MBEAN_ATTR_MHZ;
    private static final MBeanAttributeInfo MBEAN_ATTR_MODEL;
    private static final MBeanAttributeInfo MBEAN_ATTR_VENDOR;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX_SIGAR;
    private static final MBeanParameterInfo MBEAN_PARAM_CPUINDEX;
    private static final MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private int cpuIndex;
    private String objectName;
    
    public SigarCpuInfo(final int index) throws IllegalArgumentException {
        this(new Sigar(), index);
    }
    
    public SigarCpuInfo(final Sigar sigar, final int index) {
        super(sigar, (short)0);
        if (index < 0) {
            throw new IllegalArgumentException("CPU index has to be non-negative: " + index);
        }
        try {
            final int cpuCount;
            if ((cpuCount = sigar.getCpuInfoList().length) < index) {
                throw new IllegalArgumentException("CPU index out of range (found " + cpuCount + " CPU(s)): " + index);
            }
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuInfoList", e);
        }
        this.cpuIndex = index;
        this.objectName = "sigar:type=CpuInfo," + SigarCpuInfo.MBEAN_ATTR_CPUINDEX.getName().substring(0, 1).toLowerCase() + SigarCpuInfo.MBEAN_ATTR_CPUINDEX.getName().substring(1) + "=" + this.cpuIndex;
    }
    
    public String getObjectName() {
        return this.objectName;
    }
    
    public int getCpuIndex() {
        return this.cpuIndex;
    }
    
    public long getCacheSize() {
        try {
            return this.sigar.getCpuInfoList()[this.cpuIndex].getCacheSize();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuInfoList", e);
        }
    }
    
    public int getMhz() {
        try {
            return this.sigar.getCpuInfoList()[this.cpuIndex].getMhz();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuInfoList", e);
        }
    }
    
    public String getModel() {
        try {
            return this.sigar.getCpuInfoList()[this.cpuIndex].getModel();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuInfoList", e);
        }
    }
    
    public String getVendor() {
        try {
            return this.sigar.getCpuInfoList()[this.cpuIndex].getVendor();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuInfoList", e);
        }
    }
    
    public Object getAttribute(final String attr) throws AttributeNotFoundException {
        if (SigarCpuInfo.MBEAN_ATTR_CACHESIZE.getName().equals(attr)) {
            return new Long(this.getCacheSize());
        }
        if (SigarCpuInfo.MBEAN_ATTR_CPUINDEX.getName().equals(attr)) {
            return new Integer(this.getCpuIndex());
        }
        if (SigarCpuInfo.MBEAN_ATTR_MHZ.getName().equals(attr)) {
            return new Integer(this.getMhz());
        }
        if (SigarCpuInfo.MBEAN_ATTR_MODEL.getName().equals(attr)) {
            return this.getModel();
        }
        if (SigarCpuInfo.MBEAN_ATTR_VENDOR.getName().equals(attr)) {
            return this.getVendor();
        }
        throw new AttributeNotFoundException(attr);
    }
    
    public void setAttribute(final Attribute attr) throws AttributeNotFoundException {
        throw new AttributeNotFoundException(attr.getName());
    }
    
    public Object invoke(final String actionName, final Object[] params, final String[] signature) throws ReflectionException {
        throw new ReflectionException(new NoSuchMethodException(actionName), actionName);
    }
    
    public MBeanInfo getMBeanInfo() {
        return SigarCpuInfo.MBEAN_INFO;
    }
    
    static {
        MBEAN_ATTR_CPUINDEX = new MBeanAttributeInfo("CpuIndex", "int", "The index of the CPU, typically starting at 0", true, false, false);
        MBEAN_ATTR_CACHESIZE = new MBeanAttributeInfo("CacheSize", "long", "The cache size of the CPU, in [byte]", true, false, false);
        MBEAN_ATTR_MHZ = new MBeanAttributeInfo("Mhz", "int", "The clock speed of the CPU, in [MHz]", true, false, false);
        MBEAN_ATTR_MODEL = new MBeanAttributeInfo("Model", "java.lang.String", "The CPU model reported", true, false, false);
        MBEAN_ATTR_VENDOR = new MBeanAttributeInfo("Vendor", "java.lang.String", "The CPU vendor reported", true, false, false);
        MBEAN_PARAM_CPUINDEX = new MBeanParameterInfo("cpuIndex", "int", "The index of the CPU to read data for. Must be >= 0 and not exceed the CPU count of the system");
        MBEAN_PARAM_SIGAR = new MBeanParameterInfo("sigar", Sigar.class.getName(), "The Sigar instance to use to fetch data from");
        MBEAN_CONSTR_CPUINDEX = new MBeanConstructorInfo(SigarCpuInfo.class.getName(), "Creates a new instance for the CPU index specified, using a new Sigar instance to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[] { SigarCpuInfo.MBEAN_PARAM_CPUINDEX });
        MBEAN_CONSTR_CPUINDEX_SIGAR = new MBeanConstructorInfo(SigarCpuInfo.class.getName(), "Creates a new instance for the CPU index specified, using the Sigar instance specified to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[] { SigarCpuInfo.MBEAN_PARAM_SIGAR, SigarCpuInfo.MBEAN_PARAM_CPUINDEX });
        MBEAN_INFO = new MBeanInfo(SigarCpuInfo.class.getName(), "Sigar CPU Info MBean, provides overall information for a single CPU. This information only changes if, for example, a CPU is reducing its clock frequency or shutting down part of its cache. Subsequent requests are satisfied from within a cache that invalidates after 30 seconds.", new MBeanAttributeInfo[] { SigarCpuInfo.MBEAN_ATTR_CPUINDEX, SigarCpuInfo.MBEAN_ATTR_CACHESIZE, SigarCpuInfo.MBEAN_ATTR_MHZ, SigarCpuInfo.MBEAN_ATTR_MODEL, SigarCpuInfo.MBEAN_ATTR_VENDOR }, new MBeanConstructorInfo[] { SigarCpuInfo.MBEAN_CONSTR_CPUINDEX, SigarCpuInfo.MBEAN_CONSTR_CPUINDEX_SIGAR }, null, null);
    }
}
