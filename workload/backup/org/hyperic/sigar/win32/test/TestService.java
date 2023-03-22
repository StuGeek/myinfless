// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32.test;

import org.hyperic.sigar.win32.ServiceConfig;
import java.util.List;
import org.hyperic.sigar.win32.Win32Exception;
import org.hyperic.sigar.win32.Service;
import org.hyperic.sigar.test.SigarTestCase;

public class TestService extends SigarTestCase
{
    private static final String TEST_NAME = "MyTestService";
    private static final String PREFIX = "sigar.test.service.";
    private static final boolean TEST_CREATE;
    private static final boolean TEST_DELETE;
    
    public TestService(final String name) {
        super(name);
    }
    
    public void testServiceOpen() throws Exception {
        final Service service = new Service("Eventlog");
        service.getConfig();
        service.close();
        final String dummyName = "DOESNOTEXIST";
        try {
            new Service(dummyName);
            assertTrue(false);
        }
        catch (Win32Exception e) {
            this.traceln(dummyName + ": " + e.getMessage());
            assertTrue(true);
        }
    }
    
    public void testServiceNames() throws Exception {
        List services = Service.getServiceNames();
        this.assertGtZeroTrace("getServiceNames", services.size());
        final String[] ptql = { "Service.Name.ct=Ev", "Service.Path.ew=.exe" };
        for (int i = 0; i < ptql.length; ++i) {
            services = Service.getServiceNames(this.getSigar(), ptql[i]);
            this.assertGtZeroTrace(ptql[i], services.size());
        }
        final String[] invalid = { "State.Name.ct=Ev", "Service.Invalid.ew=.exe", "-" };
        for (int j = 0; j < invalid.length; ++j) {
            try {
                services = Service.getServiceNames(this.getSigar(), invalid[j]);
                fail("'" + invalid[j] + "' did not throw Exception");
            }
            catch (Exception ex) {}
        }
    }
    
    public void testServiceConfig() throws Exception {
        final List configs = Service.getServiceConfigs(this.getSigar(), "svchost.exe");
        this.assertGtZeroTrace("getServiceConfigs", configs.size());
    }
    
    public void testServiceCreateDelete() throws Exception {
        if (!TestService.TEST_CREATE) {
            return;
        }
        final ServiceConfig config = new ServiceConfig("MyTestService");
        config.setStartType(3);
        config.setDisplayName("My Test Service");
        config.setDescription("A Description of " + config.getDisplayName());
        config.setPath("C:\\Program Files\\My Test 1.0\\mytest.exe");
        Service.create(config);
    }
    
    public void testDeleteService() throws Exception {
        if (!TestService.TEST_DELETE) {
            return;
        }
        final Service service = new Service("MyTestService");
        service.delete();
    }
    
    static {
        TEST_CREATE = "true".equals(System.getProperty("sigar.test.service.create"));
        TEST_DELETE = "true".equals(System.getProperty("sigar.test.service.delete"));
    }
}
