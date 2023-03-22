// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.jmx;

import java.util.ArrayList;
import javax.management.ObjectName;
import javax.management.InvalidAttributeValueException;
import javax.management.ReflectionException;
import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import javax.management.Attribute;
import javax.management.AttributeList;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxyCache;
import javax.management.MBeanServer;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Sigar;
import javax.management.MBeanRegistration;
import javax.management.DynamicMBean;

public abstract class AbstractMBean implements DynamicMBean, MBeanRegistration
{
    protected static final String MBEAN_ATTR_TYPE = "type";
    protected static final short CACHED_30SEC = 0;
    protected static final short CACHED_5SEC = 1;
    protected static final short CACHED_500MS = 2;
    protected static final short CACHELESS = 3;
    protected static final short DEFAULT = 0;
    protected final Sigar sigarImpl;
    protected final SigarProxy sigar;
    protected MBeanServer mbeanServer;
    
    protected AbstractMBean(final Sigar sigar, final short cacheMode) {
        this.sigarImpl = sigar;
        if (cacheMode == 3) {
            this.sigar = this.sigarImpl;
        }
        else if (cacheMode == 2) {
            this.sigar = SigarProxyCache.newInstance(this.sigarImpl, 500);
        }
        else if (cacheMode == 1) {
            this.sigar = SigarProxyCache.newInstance(this.sigarImpl, 5000);
        }
        else {
            this.sigar = SigarProxyCache.newInstance(this.sigarImpl, 30000);
        }
    }
    
    public abstract String getObjectName();
    
    protected RuntimeException unexpectedError(final String type, final SigarException e) {
        final String msg = "Unexected error in Sigar.get" + type + ": " + e.getMessage();
        return new IllegalArgumentException(msg);
    }
    
    public AttributeList getAttributes(final String[] attrs) {
        final AttributeList result = new AttributeList();
        for (int i = 0; i < attrs.length; ++i) {
            try {
                result.add(new Attribute(attrs[i], this.getAttribute(attrs[i])));
            }
            catch (AttributeNotFoundException e) {}
            catch (MBeanException e2) {}
            catch (ReflectionException ex) {}
        }
        return result;
    }
    
    public AttributeList setAttributes(final AttributeList attrs) {
        final AttributeList result = new AttributeList();
        for (int i = 0; i < attrs.size(); ++i) {
            try {
                final Attribute next = ((ArrayList<Attribute>)attrs).get(i);
                this.setAttribute(next);
                result.add(next);
            }
            catch (AttributeNotFoundException e) {}
            catch (InvalidAttributeValueException e2) {}
            catch (MBeanException e3) {}
            catch (ReflectionException ex) {}
        }
        return result;
    }
    
    public ObjectName preRegister(final MBeanServer server, final ObjectName name) throws Exception {
        this.mbeanServer = server;
        return new ObjectName(this.getObjectName());
    }
    
    public void postRegister(final Boolean success) {
    }
    
    public void preDeregister() throws Exception {
    }
    
    public void postDeregister() {
        this.mbeanServer = null;
    }
}
