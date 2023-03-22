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

public class SigarCpu extends AbstractMBean
{
    private static final String MBEAN_TYPE = "CpuList";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_CPUINDEX;
    private static final MBeanAttributeInfo MBEAN_ATTR_IDLE;
    private static final MBeanAttributeInfo MBEAN_ATTR_NICE;
    private static final MBeanAttributeInfo MBEAN_ATTR_SYS;
    private static final MBeanAttributeInfo MBEAN_ATTR_TOTAL;
    private static final MBeanAttributeInfo MBEAN_ATTR_USER;
    private static final MBeanAttributeInfo MBEAN_ATTR_WAIT;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_CPUINDEX;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private final int cpuIndex;
    private final String objectName;
    
    public SigarCpu(final int cpuIndex) throws IllegalArgumentException {
        this(new Sigar(), cpuIndex);
    }
    
    public SigarCpu(final Sigar sigar, final int cpuIndex) throws IllegalArgumentException {
        super(sigar, (short)2);
        if (cpuIndex < 0) {
            throw new IllegalArgumentException("CPU index has to be non-negative: " + cpuIndex);
        }
        try {
            final int cpuCount;
            if ((cpuCount = sigar.getCpuList().length) < cpuIndex) {
                throw new IllegalArgumentException("CPU index out of range (found " + cpuCount + " CPU(s)): " + cpuIndex);
            }
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuList", e);
        }
        this.cpuIndex = cpuIndex;
        this.objectName = "sigar:type=Cpu," + SigarCpu.MBEAN_ATTR_CPUINDEX.getName().substring(0, 1).toLowerCase() + SigarCpu.MBEAN_ATTR_CPUINDEX.getName().substring(1) + "=" + cpuIndex;
    }
    
    public String getObjectName() {
        return this.objectName;
    }
    
    public int getCpuIndex() {
        return this.cpuIndex;
    }
    
    public long getIdle() {
        try {
            return this.sigar.getCpuList()[this.cpuIndex].getIdle();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuList", e);
        }
    }
    
    public long getNice() {
        try {
            return this.sigar.getCpuList()[this.cpuIndex].getNice();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuList", e);
        }
    }
    
    public long getSys() {
        try {
            return this.sigar.getCpuList()[this.cpuIndex].getSys();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuList", e);
        }
    }
    
    public long getTotal() {
        try {
            return this.sigar.getCpuList()[this.cpuIndex].getTotal();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuList", e);
        }
    }
    
    public long getUser() {
        try {
            return this.sigar.getCpuList()[this.cpuIndex].getUser();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuList", e);
        }
    }
    
    public long getWait() {
        try {
            return this.sigar.getCpuList()[this.cpuIndex].getWait();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuList", e);
        }
    }
    
    public Object getAttribute(final String attr) throws AttributeNotFoundException {
        if (SigarCpu.MBEAN_ATTR_CPUINDEX.getName().equals(attr)) {
            return new Integer(this.getCpuIndex());
        }
        if (SigarCpu.MBEAN_ATTR_IDLE.getName().equals(attr)) {
            return new Long(this.getIdle());
        }
        if (SigarCpu.MBEAN_ATTR_NICE.getName().equals(attr)) {
            return new Long(this.getNice());
        }
        if (SigarCpu.MBEAN_ATTR_SYS.getName().equals(attr)) {
            return new Long(this.getSys());
        }
        if (SigarCpu.MBEAN_ATTR_TOTAL.getName().equals(attr)) {
            return new Long(this.getTotal());
        }
        if (SigarCpu.MBEAN_ATTR_USER.getName().equals(attr)) {
            return new Long(this.getUser());
        }
        if (SigarCpu.MBEAN_ATTR_WAIT.getName().equals(attr)) {
            return new Long(this.getWait());
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
        return SigarCpu.MBEAN_INFO;
    }
    
    static {
        MBEAN_ATTR_CPUINDEX = new MBeanAttributeInfo("CpuIndex", "int", "The index of the CPU, typically starting at 0", true, false, false);
        MBEAN_ATTR_IDLE = new MBeanAttributeInfo("Idle", "long", "The idle time of the CPU, in [ms]", true, false, false);
        MBEAN_ATTR_NICE = new MBeanAttributeInfo("Nice", "long", "The time of the CPU spent on nice priority, in [ms]", true, false, false);
        MBEAN_ATTR_SYS = new MBeanAttributeInfo("Sys", "long", "The time of the CPU used by the system, in [ms]", true, false, false);
        MBEAN_ATTR_TOTAL = new MBeanAttributeInfo("Total", "long", "The total time of the CPU, in [ms]", true, false, false);
        MBEAN_ATTR_USER = new MBeanAttributeInfo("User", "long", "The time of the CPU used by user processes, in [ms]", true, false, false);
        MBEAN_ATTR_WAIT = new MBeanAttributeInfo("Wait", "long", "The time the CPU had to wait for data to be loaded, in [ms]", true, false, false);
        SigarCpu.MBEAN_PARAM_CPUINDEX = new MBeanParameterInfo("cpuIndex", "int", "The index of the CPU to read data for. Must be >= 0 and not exceed the CPU count of the system");
        SigarCpu.MBEAN_PARAM_SIGAR = new MBeanParameterInfo("sigar", Sigar.class.getName(), "The Sigar instance to use to fetch data from");
        MBEAN_CONSTR_CPUINDEX = new MBeanConstructorInfo(SigarCpu.class.getName(), "Creates a new instance for the CPU index specified, using a new Sigar instance to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[] { SigarCpu.MBEAN_PARAM_CPUINDEX });
        MBEAN_CONSTR_CPUINDEX_SIGAR = new MBeanConstructorInfo(SigarCpu.class.getName(), "Creates a new instance for the CPU index specified, using the Sigar instance specified to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[] { SigarCpu.MBEAN_PARAM_SIGAR, SigarCpu.MBEAN_PARAM_CPUINDEX });
        MBEAN_INFO = new MBeanInfo(SigarCpu.class.getName(), "Sigar CPU MBean. Provides raw timing data for a single CPU. The data is cached for 500ms, meaning each request (and as a result each block request to all parameters) within half a second is satisfied from the same dataset.", new MBeanAttributeInfo[] { SigarCpu.MBEAN_ATTR_CPUINDEX, SigarCpu.MBEAN_ATTR_IDLE, SigarCpu.MBEAN_ATTR_NICE, SigarCpu.MBEAN_ATTR_SYS, SigarCpu.MBEAN_ATTR_TOTAL, SigarCpu.MBEAN_ATTR_USER, SigarCpu.MBEAN_ATTR_WAIT }, new MBeanConstructorInfo[] { SigarCpu.MBEAN_CONSTR_CPUINDEX, SigarCpu.MBEAN_CONSTR_CPUINDEX_SIGAR }, null, null);
    }
}
