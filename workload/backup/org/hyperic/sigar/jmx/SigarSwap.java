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

public class SigarSwap extends AbstractMBean
{
    private static final String MBEAN_TYPE = "Swap";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_FREE;
    private static final MBeanAttributeInfo MBEAN_ATTR_TOTAL;
    private static final MBeanAttributeInfo MBEAN_ATTR_USED;
    private static final MBeanConstructorInfo MBEAN_CONSTR_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private final String objectName;
    
    public SigarSwap(final Sigar sigar) throws IllegalArgumentException {
        super(sigar, (short)1);
        this.objectName = "sigar:type=Swap";
    }
    
    public String getObjectName() {
        return this.objectName;
    }
    
    public long getFree() {
        try {
            return this.sigar.getSwap().getFree();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Swap", e);
        }
    }
    
    public long getTotal() {
        try {
            return this.sigar.getSwap().getTotal();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Swap", e);
        }
    }
    
    public long getUsed() {
        try {
            return this.sigar.getSwap().getUsed();
        }
        catch (SigarException e) {
            throw this.unexpectedError("Swap", e);
        }
    }
    
    public Object getAttribute(final String attr) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (SigarSwap.MBEAN_ATTR_FREE.getName().equals(attr)) {
            return new Long(this.getFree());
        }
        if (SigarSwap.MBEAN_ATTR_TOTAL.getName().equals(attr)) {
            return new Long(this.getTotal());
        }
        if (SigarSwap.MBEAN_ATTR_USED.getName().equals(attr)) {
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
        return SigarSwap.MBEAN_INFO;
    }
    
    static {
        MBEAN_ATTR_FREE = new MBeanAttributeInfo("Free", "long", "The amount of free swap memory, in [bytes]", true, false, false);
        MBEAN_ATTR_TOTAL = new MBeanAttributeInfo("Total", "long", "The total amount of swap memory, in [bytes]", true, false, false);
        MBEAN_ATTR_USED = new MBeanAttributeInfo("Used", "long", "The amount of swap memory in use, in [bytes]", true, false, false);
        SigarSwap.MBEAN_PARAM_SIGAR = new MBeanParameterInfo("sigar", Sigar.class.getName(), "The Sigar instance to use to fetch data from");
        MBEAN_CONSTR_SIGAR = new MBeanConstructorInfo(SigarSwap.class.getName(), "Creates a new instance, using the Sigar instance specified to fetch the data.", new MBeanParameterInfo[] { SigarSwap.MBEAN_PARAM_SIGAR });
        MBEAN_INFO = new MBeanInfo(SigarSwap.class.getName(), "Sigar Swap MBean, provides raw data for the swap memory configured on the system. Uses an internal cache that invalidates within 5 seconds.", new MBeanAttributeInfo[] { SigarSwap.MBEAN_ATTR_FREE, SigarSwap.MBEAN_ATTR_TOTAL, SigarSwap.MBEAN_ATTR_USED }, new MBeanConstructorInfo[] { SigarSwap.MBEAN_CONSTR_SIGAR }, null, null);
    }
}
