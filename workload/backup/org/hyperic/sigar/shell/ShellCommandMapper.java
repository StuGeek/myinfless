// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.util.Iterator;

public interface ShellCommandMapper
{
    ShellCommandHandler getHandler(final String p0);
    
    Iterator getCommandNameIterator();
}
