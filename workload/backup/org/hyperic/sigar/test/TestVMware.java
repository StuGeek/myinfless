// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarException;
import java.io.IOException;
import org.hyperic.sigar.vmware.VMControlLibrary;
import java.io.File;

public class TestVMware extends SigarTestCase
{
    public TestVMware(final String name) {
        super(name);
    }
    
    public void testVMware() throws SigarException {
        final File build = new File("build");
        if (!build.exists()) {
            return;
        }
        try {
            VMControlLibrary.link(build.getPath());
        }
        catch (IOException e) {
            this.traceln(e.getMessage());
        }
        this.traceln("vmware support=" + VMControlLibrary.isLoaded());
    }
}
