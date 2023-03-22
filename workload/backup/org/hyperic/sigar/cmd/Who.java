// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Who extends SigarCommandBase
{
    public Who(final Shell shell) {
        super(shell);
    }
    
    public Who() {
    }
    
    public String getUsageShort() {
        return "Show who is logged on";
    }
    
    private String getTime(final long time) {
        if (time == 0L) {
            return "unknown";
        }
        final String fmt = "MMM dd HH:mm";
        return new SimpleDateFormat(fmt).format(new Date(time));
    }
    
    public void output(final String[] args) throws SigarException {
        final org.hyperic.sigar.Who[] who = this.sigar.getWhoList();
        for (int i = 0; i < who.length; ++i) {
            String host = who[i].getHost();
            if (host.length() != 0) {
                host = "(" + host + ")";
            }
            this.printf(new String[] { who[i].getUser(), who[i].getDevice(), this.getTime(who[i].getTime() * 1000L), host });
        }
    }
    
    public static void main(final String[] args) throws Exception {
        new Who().processCommand(args);
    }
}
