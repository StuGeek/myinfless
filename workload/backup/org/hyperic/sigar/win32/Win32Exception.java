// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import org.hyperic.sigar.SigarException;

public class Win32Exception extends SigarException
{
    private int errorCode;
    
    public Win32Exception(final String s) {
        super(s);
    }
    
    public Win32Exception(final int error, final String s) {
        super(s);
        this.errorCode = error;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
}
