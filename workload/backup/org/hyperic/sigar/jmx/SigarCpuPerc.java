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

public class SigarCpuPerc extends AbstractMBean
{
    private static final String MBEAN_TYPE = "CpuPercList";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_CPUINDEX;
    private static final MBeanAttributeInfo MBEAN_ATTR_COMBINED;
    private static final MBeanAttributeInfo MBEAN_ATTR_IDLE;
    private static final MBeanAttributeInfo MBEAN_ATTR_NICE;
    private static final MBeanAttributeInfo MBEAN_ATTR_SYS;
    private static final MBeanAttributeInfo MBEAN_ATTR_USER;
    private static final MBeanAttributeInfo MBEAN_ATTR_WAIT;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_CPUINDEX;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private int cpuIndex;
    private String objectName;
    
    public SigarCpuPerc(final int index) {
        this(new Sigar(), index);
    }
    
    public SigarCpuPerc(final Sigar sigar, final int index) {
        super(sigar, (short)1);
        if (index < 0) {
            throw new IllegalArgumentException("CPU index has to be non-negative: " + index);
        }
        try {
            final int cpuCount;
            if ((cpuCount = sigar.getCpuPercList().length) < index) {
                throw new IllegalArgumentException("CPU index out of range (found " + cpuCount + " CPU(s)): " + index);
            }
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuPercList", e);
        }
        this.cpuIndex = index;
        this.objectName = "sigar:type=CpuPerc," + SigarCpuPerc.MBEAN_ATTR_CPUINDEX.getName().substring(0, 1).toLowerCase() + SigarCpuPerc.MBEAN_ATTR_CPUINDEX.getName().substring(1) + "=" + this.cpuIndex;
    }
    
    public String getObjectName() {
        return this.objectName;
    }
    
    public int getCpuIndex() {
        return this.cpuIndex;
    }
    
    public double getCombined() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getCombined();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuPercList", e);
        }
    }
    
    public double getIdle() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getIdle();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuPercList", e);
        }
    }
    
    public double getNice() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getNice();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuPercList", e);
        }
    }
    
    public double getSys() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getSys();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuPercList", e);
        }
    }
    
    public double getUser() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getUser();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuPercList", e);
        }
    }
    
    public double getWait() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getWait();
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuPercList", e);
        }
    }
    
    public Object getAttribute(final String attr) throws AttributeNotFoundException {
        if (SigarCpuPerc.MBEAN_ATTR_COMBINED.getName().equals(attr)) {
            return new Double(this.getCombined());
        }
        if (SigarCpuPerc.MBEAN_ATTR_CPUINDEX.getName().equals(attr)) {
            return new Integer(this.getCpuIndex());
        }
        if (SigarCpuPerc.MBEAN_ATTR_IDLE.getName().equals(attr)) {
            return new Double(this.getIdle());
        }
        if (SigarCpuPerc.MBEAN_ATTR_NICE.getName().equals(attr)) {
            return new Double(this.getNice());
        }
        if (SigarCpuPerc.MBEAN_ATTR_SYS.getName().equals(attr)) {
            return new Double(this.getSys());
        }
        if (SigarCpuPerc.MBEAN_ATTR_USER.getName().equals(attr)) {
            return new Double(this.getUser());
        }
        if (SigarCpuPerc.MBEAN_ATTR_WAIT.getName().equals(attr)) {
            return new Double(this.getWait());
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
        return SigarCpuPerc.MBEAN_INFO;
    }
    
    static {
        MBEAN_ATTR_CPUINDEX = new MBeanAttributeInfo("CpuIndex", "int", "The index of the CPU, typically starting at 0", true, false, false);
        MBEAN_ATTR_COMBINED = new MBeanAttributeInfo("Combined", "double", "The total time of the CPU, as a fraction of 1", true, false, false);
        MBEAN_ATTR_IDLE = new MBeanAttributeInfo("Idle", "double", "The idle time of the CPU, as a fraction of 1", true, false, false);
        MBEAN_ATTR_NICE = new MBeanAttributeInfo("Nice", "double", "The time of the CPU spent on nice priority, as a fraction of 1", true, false, false);
        MBEAN_ATTR_SYS = new MBeanAttributeInfo("Sys", "double", "The time of the CPU used by the system, as a fraction of 1", true, false, false);
        MBEAN_ATTR_USER = new MBeanAttributeInfo("User", "double", "The time of the CPU used by user processes, as a fraction of 1", true, false, false);
        MBEAN_ATTR_WAIT = new MBeanAttributeInfo("Wait", "double", "The time the CPU had to wait for data to be loaded, as a fraction of 1", true, false, false);
        SigarCpuPerc.MBEAN_PARAM_CPUINDEX = new MBeanParameterInfo("cpuIndex", "int", "The index of the CPU to read data for. Must be >= 0 and not exceed the CPU count of the system");
        SigarCpuPerc.MBEAN_PARAM_SIGAR = new MBeanParameterInfo("sigar", Sigar.class.getName(), "The Sigar instance to use to fetch data from");
        MBEAN_CONSTR_CPUINDEX = new MBeanConstructorInfo(SigarCpuPerc.class.getName(), "Creates a new instance for the CPU index specified, using a new Sigar instance to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[] { SigarCpuPerc.MBEAN_PARAM_CPUINDEX });
        MBEAN_CONSTR_CPUINDEX_SIGAR = new MBeanConstructorInfo(SigarCpuPerc.class.getName(), "Creates a new instance for the CPU index specified, using the Sigar instance specified to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[] { SigarCpuPerc.MBEAN_PARAM_SIGAR, SigarCpuPerc.MBEAN_PARAM_CPUINDEX });
        MBEAN_INFO = new MBeanInfo(SigarCpuPerc.class.getName(), "Sigar CPU MBean. Provides percentage data for a single CPU, averaged over the timeframe between the last and the current measurement point. Two measurement points can be as close as 5 seconds, meaning subsequent requests for data within 5 seconds after the last executed call will be satisfied from cached data.", new MBeanAttributeInfo[] { SigarCpuPerc.MBEAN_ATTR_CPUINDEX, SigarCpuPerc.MBEAN_ATTR_COMBINED, SigarCpuPerc.MBEAN_ATTR_IDLE, SigarCpuPerc.MBEAN_ATTR_NICE, SigarCpuPerc.MBEAN_ATTR_SYS, SigarCpuPerc.MBEAN_ATTR_USER, SigarCpuPerc.MBEAN_ATTR_WAIT }, new MBeanConstructorInfo[] { SigarCpuPerc.MBEAN_CONSTR_CPUINDEX, SigarCpuPerc.MBEAN_CONSTR_CPUINDEX_SIGAR }, null, null);
    }
}
