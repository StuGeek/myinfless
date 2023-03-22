// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32.test;

import java.util.List;
import java.util.ArrayList;
import org.hyperic.sigar.win32.RegistryKey;
import org.hyperic.sigar.test.SigarTestCase;

public class TestRegistryKey extends SigarTestCase
{
    private static final boolean TEST_WRITE = false;
    
    public TestRegistryKey(final String name) {
        super(name);
    }
    
    public void testRegistryRead() throws Exception {
        final RegistryKey software = RegistryKey.LocalMachine.openSubKey("SOFTWARE");
        final String[] keys = software.getSubKeyNames();
        assertTrue(keys.length > 0);
        software.close();
    }
    
    public void testHardwareValues() throws Exception {
        final RegistryKey hw = RegistryKey.LocalMachine.openSubKey("HARDWARE\\DESCRIPTION\\System");
        try {
            final ArrayList values = new ArrayList();
            hw.getMultiStringValue("SystemBiosVersion", values);
            this.assertGtZeroTrace("SystemBiosVersion.size()", values.size());
            this.traceln("SystemBiosVersion=" + values);
        }
        catch (Exception ex) {}
        final RegistryKey cpu0 = hw.openSubKey("CentralProcessor\\0");
        final String cpu2 = cpu0.getStringValue("ProcessorNameString");
        this.assertLengthTrace("cpu0", cpu2);
        cpu0.close();
        hw.close();
    }
    
    public void testSoftwareValues() throws Exception {
        final RegistryKey ms = RegistryKey.LocalMachine.openSubKey("SOFTWARE\\Microsoft");
        RegistryKey msmq = null;
        try {
            msmq = ms.openSubKey("MSMQ\\Parameters");
        }
        catch (Exception ex) {}
        if (msmq != null) {
            this.traceln("MSMQ...");
            if (msmq.getSubKeyNames().length > 0) {
                try {
                    final String build = msmq.getStringValue("CurrentBuild");
                    this.assertLengthTrace("CurrentBuild", build);
                    final int id = msmq.getIntValue("SeqID");
                    this.assertGtZeroTrace("SeqID", id);
                }
                catch (Exception ex2) {}
            }
            msmq.close();
        }
        RegistryKey sql = null;
        try {
            sql = ms.openSubKey("Microsoft SQL Server\\MSSQL.1\\Setup");
        }
        catch (Exception ex3) {}
        if (sql != null) {
            this.traceln("MsSQL...");
            try {
                final String edition = sql.getStringValue("Edition");
                this.assertLengthTrace("Edition", edition);
            }
            catch (Exception ex4) {}
            sql.close();
        }
        ms.close();
        final String TC = "SOFTWARE\\Apache Software Foundation\\Procrun 2.0\\Tomcat6\\Parameters\\Java";
        try {
            final RegistryKey tc = RegistryKey.LocalMachine.openSubKey("SOFTWARE\\Apache Software Foundation\\Procrun 2.0\\Tomcat6\\Parameters\\Java");
            this.traceln("Tomcat6...");
            final ArrayList values = new ArrayList();
            tc.getMultiStringValue("Options", values);
            this.assertGtZeroTrace("Options.size()", values.size());
            this.traceln("Options=" + values);
            tc.close();
        }
        catch (Exception ex5) {}
    }
    
    public void testRegistryWrite() throws Exception {
    }
}
