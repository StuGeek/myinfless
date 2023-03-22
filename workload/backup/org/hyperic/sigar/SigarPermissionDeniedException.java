// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

public class SigarPermissionDeniedException extends SigarException
{
    public static String getUserDeniedMessage(final SigarProxy sigar, final long pid) {
        String user = "unknown";
        String owner = "unknown";
        try {
            user = sigar.getProcCredName(sigar.getPid()).getUser();
        }
        catch (SigarException ex) {}
        try {
            owner = sigar.getProcCredName(pid).getUser();
        }
        catch (SigarException ex2) {}
        return "User " + user + " denied access to process " + pid + " owned by " + owner;
    }
    
    public SigarPermissionDeniedException(final String s) {
        super(s);
    }
    
    public SigarPermissionDeniedException(final SigarProxy sigar, final long pid) {
        super(getUserDeniedMessage(sigar, pid));
    }
}
