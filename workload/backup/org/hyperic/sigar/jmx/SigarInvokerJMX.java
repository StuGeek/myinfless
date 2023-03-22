// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.jmx;

import org.hyperic.sigar.util.ReferenceMap;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarException;
import java.util.StringTokenizer;
import org.hyperic.sigar.SigarProxy;
import java.util.Map;
import org.hyperic.sigar.SigarInvoker;

public class SigarInvokerJMX extends SigarInvoker
{
    public static final String DOMAIN_NAME = "sigar";
    public static final String PROP_TYPE = "Type";
    public static final String PROP_ARG = "Arg";
    private String arg;
    private static Map cache;
    
    public SigarInvokerJMX() {
        this.arg = null;
    }
    
    public static SigarInvokerJMX getInstance(final SigarProxy proxy, String name) {
        int ix = name.indexOf(":");
        if (ix > 0) {
            name = name.substring(ix + 1);
        }
        SigarInvokerJMX invoker;
        if ((invoker = SigarInvokerJMX.cache.get(name)) != null) {
            invoker.setProxy(proxy);
            return invoker;
        }
        invoker = new SigarInvokerJMX();
        invoker.setProxy(proxy);
        final StringTokenizer st = new StringTokenizer(name, ",");
        while (st.hasMoreTokens()) {
            final String attr = st.nextToken();
            ix = attr.indexOf(61);
            final String key = attr.substring(0, ix);
            final String val = attr.substring(key.length() + 1);
            if (key.equals("Type")) {
                invoker.setType(val);
            }
            else {
                if (!key.equals("Arg")) {
                    continue;
                }
                invoker.setArg(decode(val));
            }
        }
        SigarInvokerJMX.cache.put(name, invoker);
        return invoker;
    }
    
    public static String decode(final String val) {
        final StringBuffer buf = new StringBuffer(val.length());
        boolean changed = false;
        final int len = val.length();
        int i = 0;
        while (i < len) {
            final char c = val.charAt(i);
            if (c == '%') {
                if (i + 2 > len) {
                    break;
                }
                final String s = val.substring(i + 1, i + 3);
                char d;
                if (s.equals("3A")) {
                    d = ':';
                }
                else if (s.equals("3D")) {
                    d = '=';
                }
                else {
                    if (!s.equals("2C")) {
                        buf.append(c);
                        ++i;
                        continue;
                    }
                    d = ',';
                }
                changed = true;
                buf.append(d);
                i += 3;
            }
            else {
                buf.append(c);
                ++i;
            }
        }
        return changed ? buf.toString() : val;
    }
    
    private void setArg(final String val) {
        this.arg = val;
    }
    
    public String getArg() {
        return this.arg;
    }
    
    public static String getObjectName(final String type, final String arg) {
        String s = "sigar:";
        s = s + "Type=" + type;
        if (arg != null) {
            s = s + ",Arg=" + arg;
        }
        return s;
    }
    
    public String getObjectName() {
        return getObjectName(this.getType(), this.getArg());
    }
    
    public String toString() {
        return this.getObjectName();
    }
    
    public Object invoke(final String attr) throws SigarException, SigarNotImplementedException {
        return super.invoke(this.getArg(), attr);
    }
    
    static {
        SigarInvokerJMX.cache = ReferenceMap.synchronizedMap();
    }
}
