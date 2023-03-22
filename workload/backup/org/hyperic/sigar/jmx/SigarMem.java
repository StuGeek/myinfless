// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.jmx;

import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.Attribute;
import javax.management.ReflectionException;
import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Sigar;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;

public class SigarMem extends AbstractMBean
{
    private static final String MBEAN_TYPE = "Mem";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_ACTUAL_FREE;
    private static final MBeanAttributeInfo MBEAN_ATTR_ACTUAL_USED;
    private static final MBeanAttributeInfo MBEAN_ATTR_FREE;
    private static final MBeanAttributeInfo MBEAN_ATTR_RAM;
    private static final MBeanAttributeInfo MBEAN_ATTR_TOTAL;
    private static final MBeanAttributeInfo MBEAN_ATTR_USED;
    private static final MBeanConstructorInfo MBEAN_CONSTR_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private final String objectName;
    
    public SigarMem(final Sigar sigar) throws IllegalArgumentException {
        super(sigar, (short)2);
        this.objectName = "sigar:type=Memory";
    }
    
    public String getObjectName() {
        return this.objectName;
    }
    
    public long getActualFree() {
        try {
            return this.sigar.getMem().getActualFree();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Mem", e);
        }
    }
    
    public long getActualUsed() {
        try {
            return this.sigar.getMem().getActualUsed();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Mem", e);
        }
    }
    
    public long getFree() {
        try {
            return this.sigar.getMem().getFree();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Mem", e);
        }
    }
    
    public long getRam() {
        try {
            return this.sigar.getMem().getRam();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Mem", e);
        }
    }
    
    public long getTotal() {
        try {
            return this.sigar.getMem().getTotal();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Mem", e);
        }
    }
    
    public long getUsed() {
        try {
            return this.sigar.getMem().getUsed();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Mem", e);
        }
    }
    
    public Object getAttribute(final String attr) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (SigarMem.MBEAN_ATTR_ACTUAL_FREE.getName().equals(attr)) {
            return new Long(this.getActualFree());
        }
        if (SigarMem.MBEAN_ATTR_ACTUAL_USED.getName().equals(attr)) {
            return new Long(this.getActualUsed());
        }
        if (SigarMem.MBEAN_ATTR_FREE.getName().equals(attr)) {
            return new Long(this.getFree());
        }
        if (SigarMem.MBEAN_ATTR_RAM.getName().equals(attr)) {
            return new Long(this.getRam());
        }
        if (SigarMem.MBEAN_ATTR_TOTAL.getName().equals(attr)) {
            return new Long(this.getTotal());
        }
        if (SigarMem.MBEAN_ATTR_USED.getName().equals(attr)) {
            return new Long(this.getUsed());
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
        return SigarMem.MBEAN_INFO;
    }
    
    static {
        MBEAN_ATTR_ACTUAL_FREE = new MBeanAttributeInfo("ActualFree", "long", "TODO add proper description here", true, false, false);
        MBEAN_ATTR_ACTUAL_USED = new MBeanAttributeInfo("ActualUsed", "long", "TODO add proper description here", true, false, false);
        MBEAN_ATTR_FREE = new MBeanAttributeInfo("Free", "long", "TODO add proper description here", true, false, false);
        MBEAN_ATTR_RAM = new MBeanAttributeInfo("Ram", "long", "TODO add proper description here", true, false, false);
        MBEAN_ATTR_TOTAL = new MBeanAttributeInfo("Total", "long", "TODO add proper description here", true, false, false);
        MBEAN_ATTR_USED = new MBeanAttributeInfo("Used", "long", "TODO add proper description here", true, false, false);
        SigarMem.MBEAN_PARAM_SIGAR = new MBeanParameterInfo("sigar", Sigar.class.getName(), "The Sigar instance to use to fetch data from");
        MBEAN_CONSTR_SIGAR = new MBeanConstructorInfo(SigarMem.class.getName(), "Creates a new instance, using the Sigar instance specified to fetch the data.", new MBeanParameterInfo[] { SigarMem.MBEAN_PARAM_SIGAR });
        MBEAN_INFO = new MBeanInfo(SigarMem.class.getName(), "Sigar Memory MBean, provides raw data for the physical memory installed on the system. Uses an internal cache that invalidates within 500ms, allowing for bulk request being satisfied with a single dataset fetch.", new MBeanAttributeInfo[] { SigarMem.MBEAN_ATTR_ACTUAL_FREE, SigarMem.MBEAN_ATTR_ACTUAL_USED, SigarMem.MBEAN_ATTR_FREE, SigarMem.MBEAN_ATTR_RAM, SigarMem.MBEAN_ATTR_TOTAL, SigarMem.MBEAN_ATTR_USED }, new MBeanConstructorInfo[] { SigarMem.MBEAN_CONSTR_SIGAR }, null, null);
    }
}
