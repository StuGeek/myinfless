// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

public class SigarException extends Exception
{
    private String message;
    
    public SigarException() {
    }
    
    public SigarException(final String s) {
        super(s);
    }
    
    public String getMessage() {
        if (this.message != null) {
            return this.message;
        }
        return super.getMessage();
    }
    
    void setMessage(final String message) {
        this.message = message;
    }
}
