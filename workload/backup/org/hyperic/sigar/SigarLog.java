// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SigarLog
{
    private static final int LOG_FATAL = 0;
    private static final int LOG_ERROR = 1;
    private static final int LOG_WARN = 2;
    private static final int LOG_INFO = 3;
    private static final int LOG_DEBUG = 4;
    
    private static native void setLogger(final Sigar p0, final Logger p1);
    
    public static native void setLevel(final Sigar p0, final int p1);
    
    private static boolean isLogConfigured() {
        return Logger.getRootLogger().getAllAppenders().hasMoreElements();
    }
    
    private static Logger getLogger() {
        return getLogger("Sigar");
    }
    
    public static Logger getLogger(final String name) {
        final Logger log = Logger.getLogger(name);
        if (!isLogConfigured()) {
            BasicConfigurator.configure();
        }
        return log;
    }
    
    static void error(final String msg, final Throwable exc) {
        getLogger().error((Object)msg, exc);
    }
    
    static void debug(final String msg, final Throwable exc) {
        getLogger().debug((Object)msg, exc);
    }
    
    public static void enable(final Sigar sigar) {
        final Logger log = getLogger();
        Level level = log.getLevel();
        if (level == null) {
            level = Logger.getRootLogger().getLevel();
            if (level == null) {
                return;
            }
        }
        switch (level.toInt()) {
            case 50000: {
                setLevel(sigar, 0);
                break;
            }
            case 40000: {
                setLevel(sigar, 1);
                break;
            }
            case 30000: {
                setLevel(sigar, 2);
                break;
            }
            case 20000: {
                setLevel(sigar, 3);
                break;
            }
            case 10000: {
                setLevel(sigar, 4);
                break;
            }
        }
        setLogger(sigar, log);
    }
    
    public static void disable(final Sigar sigar) {
        setLogger(sigar, null);
    }
}
