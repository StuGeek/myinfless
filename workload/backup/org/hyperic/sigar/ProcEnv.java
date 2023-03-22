// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.Map;

class ProcEnv
{
    private ProcEnv() {
    }
    
    public static native Map getAll(final Sigar p0, final long p1) throws SigarException;
    
    public static native String getValue(final Sigar p0, final long p1, final String p2) throws SigarException;
}
