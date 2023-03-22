// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

public interface EventLogNotification
{
    boolean matches(final EventLogRecord p0);
    
    void handleNotification(final EventLogRecord p0);
}
