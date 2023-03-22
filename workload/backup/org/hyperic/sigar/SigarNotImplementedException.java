// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

public class SigarNotImplementedException extends SigarException
{
    private static final String msg = "This method has not been implemented on this platform";
    public static final SigarNotImplementedException INSTANCE;
    
    public SigarNotImplementedException() {
    }
    
    public SigarNotImplementedException(final String s) {
        super(s);
    }
    
    static {
        INSTANCE = new SigarNotImplementedException("This method has not been implemented on this platform");
    }
}
