// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class MorphException extends NestableRuntimeException
{
    private static final long serialVersionUID = -540093801787033824L;
    protected Throwable cause;
    
    public MorphException(final String message) {
        super(message);
        this.cause = null;
    }
    
    public MorphException(final String message, final Throwable cause) {
        super(message);
        this.cause = null;
        this.cause = cause;
    }
    
    public MorphException(final Throwable cause) {
        super(cause.getMessage());
        this.cause = null;
        this.cause = cause;
    }
    
    public Throwable getCause() {
        return this.cause;
    }
}
