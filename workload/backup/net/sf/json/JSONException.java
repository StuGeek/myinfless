// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class JSONException extends NestableRuntimeException
{
    private static final long serialVersionUID = 6995087065217051815L;
    
    public JSONException() {
    }
    
    public JSONException(final String msg) {
        super(msg, null);
    }
    
    public JSONException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
    public JSONException(final Throwable cause) {
        super((cause == null) ? null : cause.toString(), cause);
    }
}
