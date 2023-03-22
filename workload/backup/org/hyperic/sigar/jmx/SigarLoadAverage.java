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
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.Sigar;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;

public class SigarLoadAverage extends AbstractMBean
{
    private static final String MBEAN_TYPE = "LoadAverage";
    private static final double NOT_IMPLEMENTED_LOAD_VALUE = -1.0;
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_LAST1MIN;
    private static final MBeanAttributeInfo MBEAN_ATTR_LAST5MIN;
    private static final MBeanAttributeInfo MBEAN_ATTR_LAST15MIN;
    private static final MBeanConstructorInfo MBEAN_CONSTR_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private final String objectName;
    private boolean notImplemented;
    
    public SigarLoadAverage() throws IllegalArgumentException {
        this(new Sigar());
    }
    
    public SigarLoadAverage(final Sigar sigar) throws IllegalArgumentException {
        super(sigar, (short)0);
        this.objectName = "sigar:type=LoadAverage";
    }
    
    public String getObjectName() {
        return this.objectName;
    }
    
    public double getLastMinute() {
        try {
            return this.sigarImpl.getLoadAverage()[0];
        }
        catch (SigarNotImplementedException e2) {
            return -1.0;
        }
        catch (SigarException e) {
            throw this.unexpectedError("LoadAverage", e);
        }
    }
    
    public double getLastFiveMinutes() {
        try {
            return this.sigarImpl.getLoadAverage()[1];
        }
        catch (SigarNotImplementedException e2) {
            return -1.0;
        }
        catch (SigarException e) {
            throw this.unexpectedError("LoadAverage", e);
        }
    }
    
    public double getLast15Minutes() {
        try {
            return this.sigarImpl.getLoadAverage()[2];
        }
        catch (SigarNotImplementedException e2) {
            return -1.0;
        }
        catch (SigarException e) {
            throw this.unexpectedError("LoadAverage", e);
        }
    }
    
    public Object getAttribute(final String attr) throws AttributeNotFoundException {
        if (SigarLoadAverage.MBEAN_ATTR_LAST1MIN.getName().equals(attr)) {
            return new Double(this.getLastMinute());
        }
        if (SigarLoadAverage.MBEAN_ATTR_LAST5MIN.getName().equals(attr)) {
            return new Double(this.getLastFiveMinutes());
        }
        if (SigarLoadAverage.MBEAN_ATTR_LAST15MIN.getName().equals(attr)) {
            return new Double(this.getLast15Minutes());
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
        return SigarLoadAverage.MBEAN_INFO;
    }
    
    static {
        MBEAN_ATTR_LAST1MIN = new MBeanAttributeInfo("LastMinute", "double", "The load average in the last minute, as a fraction of 1, or -1.0 if the load cannot be determined on this platform", true, false, false);
        MBEAN_ATTR_LAST5MIN = new MBeanAttributeInfo("LastFiveMinutes", "double", "The load average over the last five minutes, as a fraction of 1, or -1.0 if the load cannot be determined on this platform", true, false, false);
        MBEAN_ATTR_LAST15MIN = new MBeanAttributeInfo("Last15Minutes", "double", "The load average over the last 15 minutes, as a fraction of 1, or -1.0 if the load cannot be determined on this platform", true, false, false);
        SigarLoadAverage.MBEAN_PARAM_SIGAR = new MBeanParameterInfo("sigar", Sigar.class.getName(), "The Sigar instance to use to fetch data from");
        MBEAN_CONSTR_SIGAR = new MBeanConstructorInfo(SigarLoadAverage.class.getName(), "Creates a new instance, using the Sigar instance specified to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[] { SigarLoadAverage.MBEAN_PARAM_SIGAR });
        MBEAN_INFO = new MBeanInfo(SigarLoadAverage.class.getName(), "Sigar load average MBean. Provides load averages of the system over the last one, five and 15 minutes. Due to the long term character of that information, the fetch is done using a Sigar proxy cache with a timeout of 30 seconds.", new MBeanAttributeInfo[] { SigarLoadAverage.MBEAN_ATTR_LAST1MIN, SigarLoadAverage.MBEAN_ATTR_LAST5MIN, SigarLoadAverage.MBEAN_ATTR_LAST15MIN }, new MBeanConstructorInfo[] { SigarLoadAverage.MBEAN_CONSTR_SIGAR }, null, null);
    }
}
