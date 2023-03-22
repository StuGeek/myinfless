// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.ptql;

import org.hyperic.sigar.SigarException;

public class MalformedQueryException extends SigarException
{
    public MalformedQueryException() {
    }
    
    public MalformedQueryException(final String message) {
        super(message);
    }
}
