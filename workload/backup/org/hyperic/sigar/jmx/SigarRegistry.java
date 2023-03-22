// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.jmx;

import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectInstance;
import org.hyperic.sigar.SigarException;
import javax.management.ObjectName;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import org.hyperic.sigar.Sigar;
import java.util.ArrayList;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;

public class SigarRegistry extends AbstractMBean
{
    private static final String MBEAN_TYPE = "SigarRegistry";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanConstructorInfo MBEAN_CONSTR_DEFAULT;
    private final String objectName;
    private final ArrayList managedBeans;
    
    public SigarRegistry() {
        super(new Sigar(), (short)3);
        this.objectName = "sigar:type=SigarRegistry";
        this.managedBeans = new ArrayList();
    }
    
    public String getObjectName() {
        return this.objectName;
    }
    
    public Object getAttribute(final String attr) throws AttributeNotFoundException {
        throw new AttributeNotFoundException(attr);
    }
    
    public void setAttribute(final Attribute attr) throws AttributeNotFoundException {
        throw new AttributeNotFoundException(attr.getName());
    }
    
    public Object invoke(final String action, final Object[] params, final String[] signatures) throws MBeanException, ReflectionException {
        throw new ReflectionException(new NoSuchMethodException(action), action);
    }
    
    public MBeanInfo getMBeanInfo() {
        return SigarRegistry.MBEAN_INFO;
    }
    
    public void postRegister(final Boolean success) {
        super.postRegister(success);
        if (!success) {
            return;
        }
        this.registerCpuBeans();
        this.registerMemoryBeans();
        this.registerSystemBeans();
    }
    
    private void registerCpuBeans() {
        ObjectInstance nextRegistered = null;
        try {
            for (int cpuCount = this.sigar.getCpuInfoList().length, i = 0; i < cpuCount; ++i) {
                final SigarCpu nextCpu = new SigarCpu(this.sigarImpl, i);
                try {
                    if (!this.mbeanServer.isRegistered(new ObjectName(nextCpu.getObjectName()))) {
                        nextRegistered = this.mbeanServer.registerMBean(nextCpu, null);
                    }
                }
                catch (Exception ex) {}
                if (nextRegistered != null) {
                    this.managedBeans.add(nextRegistered.getObjectName());
                }
                nextRegistered = null;
                final SigarCpuPerc nextCpuPerc = new SigarCpuPerc(this.sigarImpl, i);
                try {
                    if (!this.mbeanServer.isRegistered(new ObjectName(nextCpuPerc.getObjectName()))) {
                        nextRegistered = this.mbeanServer.registerMBean(nextCpuPerc, null);
                    }
                }
                catch (Exception ex2) {}
                if (nextRegistered != null) {
                    this.managedBeans.add(nextRegistered.getObjectName());
                }
                nextRegistered = null;
                final SigarCpuInfo nextCpuInfo = new SigarCpuInfo(this.sigarImpl, i);
                try {
                    if (!this.mbeanServer.isRegistered(new ObjectName(nextCpuInfo.getObjectName()))) {
                        nextRegistered = this.mbeanServer.registerMBean(nextCpuInfo, null);
                    }
                }
                catch (Exception ex3) {}
                if (nextRegistered != null) {
                    this.managedBeans.add(nextRegistered.getObjectName());
                }
                nextRegistered = null;
            }
        }
        catch (SigarException e) {
            throw this.unexpectedError("CpuInfoList", e);
        }
    }
    
    private void registerMemoryBeans() {
        ObjectInstance nextRegistered = null;
        final SigarMem mem = new SigarMem(this.sigarImpl);
        try {
            if (!this.mbeanServer.isRegistered(new ObjectName(mem.getObjectName()))) {
                nextRegistered = this.mbeanServer.registerMBean(mem, null);
            }
        }
        catch (Exception ex) {}
        if (nextRegistered != null) {
            this.managedBeans.add(nextRegistered.getObjectName());
        }
        nextRegistered = null;
        final SigarSwap swap = new SigarSwap(this.sigarImpl);
        try {
            if (!this.mbeanServer.isRegistered(new ObjectName(swap.getObjectName()))) {
                nextRegistered = this.mbeanServer.registerMBean(swap, null);
            }
        }
        catch (Exception e) {
            nextRegistered = null;
        }
        if (nextRegistered != null) {
            this.managedBeans.add(nextRegistered.getObjectName());
        }
        nextRegistered = null;
    }
    
    private void registerSystemBeans() {
        ObjectInstance nextRegistered = null;
        final SigarLoadAverage loadAvg = new SigarLoadAverage(this.sigarImpl);
        try {
            if (!this.mbeanServer.isRegistered(new ObjectName(loadAvg.getObjectName()))) {
                nextRegistered = this.mbeanServer.registerMBean(loadAvg, null);
            }
        }
        catch (Exception ex) {}
        if (nextRegistered != null) {
            this.managedBeans.add(nextRegistered.getObjectName());
        }
        nextRegistered = null;
    }
    
    public void preDeregister() throws Exception {
        for (int i = this.managedBeans.size() - 1; i >= 0; --i) {
            final ObjectName next = this.managedBeans.remove(i);
            if (this.mbeanServer.isRegistered(next)) {
                try {
                    this.mbeanServer.unregisterMBean(next);
                }
                catch (Exception ex) {}
            }
        }
        super.preDeregister();
    }
    
    static {
        MBEAN_CONSTR_DEFAULT = new MBeanConstructorInfo(SigarRegistry.class.getName(), "Creates a new instance of this class. Will create the Sigar instance this class uses when constructing other MBeans", new MBeanParameterInfo[0]);
        MBEAN_INFO = new MBeanInfo(SigarRegistry.class.getName(), "Sigar MBean registry. Provides a central point for creation and destruction of Sigar MBeans. Any Sigar MBean created via this instance will automatically be cleaned up when this instance is deregistered from the MBean server.", null, new MBeanConstructorInfo[] { SigarRegistry.MBEAN_CONSTR_DEFAULT }, null, null);
    }
}
