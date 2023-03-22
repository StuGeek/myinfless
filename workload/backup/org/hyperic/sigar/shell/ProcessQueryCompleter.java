// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import org.hyperic.sigar.SigarProxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import org.hyperic.sigar.util.GetlineCompleter;

public class ProcessQueryCompleter implements GetlineCompleter
{
    private static final String SIGAR_PACKAGE = "org.hyperic.sigar.";
    private static final Map METHODS;
    private static final Collection NOPS;
    private static final Collection SOPS;
    private static final Class[] NOPARAM;
    private static final String PROC_PREFIX = "getProc";
    private ShellBase shell;
    private GetlineCompleter m_completer;
    private Map methods;
    
    public ProcessQueryCompleter(final ShellBase shell) {
        this.shell = shell;
        this.methods = getMethods();
        this.m_completer = new CollectionCompleter(shell, this.methods.keySet());
    }
    
    public static Map getMethods() {
        return ProcessQueryCompleter.METHODS;
    }
    
    public static Collection getMethodOpNames(final Method method) {
        if (method == null) {
            return ProcessQueryCompleter.SOPS;
        }
        final Class rtype = method.getReturnType();
        if (rtype == Character.TYPE || rtype == Double.TYPE || rtype == Integer.TYPE || rtype == Long.TYPE) {
            return ProcessQueryCompleter.NOPS;
        }
        return ProcessQueryCompleter.SOPS;
    }
    
    public static boolean isSigarClass(final Class type) {
        return type.getName().startsWith("org.hyperic.sigar.");
    }
    
    public String complete(String line) {
        final int ix = line.indexOf(".");
        if (ix == -1) {
            line = this.m_completer.complete(line);
            if (!line.endsWith(".") && this.methods.get(line) != null) {
                return line + ".";
            }
            return line;
        }
        else {
            final String attrClass = line.substring(0, ix);
            String attr = line.substring(ix + 1, line.length());
            Method method = this.methods.get(attrClass);
            if (method == null) {
                return line;
            }
            final Class subtype = method.getReturnType();
            final boolean isSigarClass = isSigarClass(subtype);
            final int ix2 = attr.indexOf(".");
            if (ix2 != -1) {
                method = null;
                final String op = attr.substring(ix2 + 1, attr.length());
                attr = attr.substring(0, ix2);
                if (isSigarClass) {
                    try {
                        method = subtype.getMethod("get" + attr, (Class[])ProcessQueryCompleter.NOPARAM);
                    }
                    catch (NoSuchMethodException ex) {}
                }
                final Method m = method;
                final GetlineCompleter completer = new CollectionCompleter(this.shell, getMethodOpNames(m));
                final String partial = completer.complete(op);
                String result = attrClass + "." + attr + "." + partial;
                if (partial.length() == 2) {
                    result += "=";
                }
                return result;
            }
            if (isSigarClass) {
                final ArrayList possible = new ArrayList();
                final Method[] submethods = subtype.getDeclaredMethods();
                for (int i = 0; i < submethods.length; ++i) {
                    final Method j = submethods[i];
                    if (j.getName().startsWith("get")) {
                        possible.add(j.getName().substring(3));
                    }
                }
                final GetlineCompleter completer = new CollectionCompleter(this.shell, possible);
                final String partial = completer.complete(attr);
                String result = attrClass + "." + partial;
                if (possible.contains(partial)) {
                    result += ".";
                }
                return result;
            }
            return line;
        }
    }
    
    static {
        METHODS = new HashMap();
        NOPS = Arrays.asList("eq", "ne", "gt", "ge", "lt", "le");
        SOPS = Arrays.asList("eq", "ne", "re", "ct", "ew", "sw");
        NOPARAM = new Class[0];
        final Method[] methods = SigarProxy.class.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            final String name = methods[i].getName();
            if (name.startsWith("getProc")) {
                final Class[] params = methods[i].getParameterTypes();
                if (params.length == 1) {
                    if (params[0] == Long.TYPE) {
                        ProcessQueryCompleter.METHODS.put(name.substring("getProc".length()), methods[i]);
                    }
                }
            }
        }
    }
}
