// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import java.util.Date;
import java.text.SimpleDateFormat;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.util.PrintfFormat;

public class Uptime extends SigarCommandBase
{
    private static Object[] loadAvg;
    private static PrintfFormat formatter;
    
    public Uptime(final Shell shell) {
        super(shell);
    }
    
    public Uptime() {
    }
    
    public String getUsageShort() {
        return "Display how long the system has been running";
    }
    
    public void output(final String[] args) throws SigarException {
        System.out.println(getInfo(this.sigar));
    }
    
    public static String getInfo(final SigarProxy sigar) throws SigarException {
        final double uptime = sigar.getUptime().getUptime();
        String loadAverage;
        try {
            final double[] avg = sigar.getLoadAverage();
            Uptime.loadAvg[0] = new Double(avg[0]);
            Uptime.loadAvg[1] = new Double(avg[1]);
            Uptime.loadAvg[2] = new Double(avg[2]);
            loadAverage = "load average: " + Uptime.formatter.sprintf(Uptime.loadAvg);
        }
        catch (SigarNotImplementedException e) {
            loadAverage = "(load average unknown)";
        }
        return "  " + getCurrentTime() + "  up " + formatUptime(uptime) + ", " + loadAverage;
    }
    
    private static String formatUptime(final double uptime) {
        String retval = "";
        final int days = (int)uptime / 86400;
        if (days != 0) {
            retval = retval + days + " " + ((days > 1) ? "days" : "day") + ", ";
        }
        int minutes = (int)uptime / 60;
        int hours = minutes / 60;
        hours %= 24;
        minutes %= 60;
        if (hours != 0) {
            retval = retval + hours + ":" + minutes;
        }
        else {
            retval = retval + minutes + " min";
        }
        return retval;
    }
    
    private static String getCurrentTime() {
        return new SimpleDateFormat("h:mm a").format(new Date());
    }
    
    public static void main(final String[] args) throws Exception {
        new Uptime().processCommand(args);
    }
    
    static {
        Uptime.loadAvg = new Object[3];
        Uptime.formatter = new PrintfFormat("%.2f, %.2f, %.2f");
    }
}
