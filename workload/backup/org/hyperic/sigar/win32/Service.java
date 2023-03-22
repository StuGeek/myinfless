// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import java.util.Arrays;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.hyperic.sigar.Sigar;

public class Service extends Win32
{
    public static final int SERVICE_STOPPED = 1;
    public static final int SERVICE_START_PENDING = 2;
    public static final int SERVICE_STOP_PENDING = 3;
    public static final int SERVICE_RUNNING = 4;
    public static final int SERVICE_CONTINUE_PENDING = 5;
    public static final int SERVICE_PAUSE_PENDING = 6;
    public static final int SERVICE_PAUSED = 7;
    private static final String[] STATUS;
    private static final int CONTROL_START = 0;
    private static final int CONTROL_STOP = 1;
    private static final int CONTROL_PAUSE = 2;
    private static final int CONTROL_RESUME = 3;
    private static final int STANDARD_RIGHTS_REQUIRED = 983040;
    private static final int SC_MANAGER_CONNECT = 1;
    private static final int SC_MANAGER_CREATE_SERVICE = 2;
    private static final int SC_MANAGER_ENUMERATE_SERVICE = 4;
    private static final int SC_MANAGER_LOCK = 8;
    private static final int SC_MANAGER_QUERY_LOCK_STATUS = 16;
    private static final int SC_MANAGER_MODIFY_BOOT_CONFIG = 32;
    private static final int SC_MANAGER_ALL_ACCESS = 983103;
    private static final int SERVICE_QUERY_CONFIG = 1;
    private static final int SERVICE_CHANGE_CONFIG = 2;
    private static final int SERVICE_QUERY_STATUS = 4;
    private static final int SERVICE_ENUMERATE_DEPENDENTS = 8;
    private static final int SERVICE_START = 16;
    private static final int SERVICE_STOP = 32;
    private static final int SERVICE_PAUSE_CONTINUE = 64;
    private static final int SERVICE_INTERROGATE = 128;
    private static final int SERVICE_USER_DEFINED_CONTROL = 256;
    private static final int SERVICE_ALL_ACCESS = 983551;
    private long manager;
    private long service;
    private String name;
    
    private Service() throws Win32Exception {
        this.manager = OpenSCManager("", 983103);
    }
    
    public static native List getServiceNames(final Sigar p0, final String p1) throws Win32Exception;
    
    public static List getServiceNames() throws Win32Exception {
        return getServiceNames(null, null);
    }
    
    public static List getServiceConfigs(final Sigar sigar, final String exe) throws Win32Exception {
        final List services = new ArrayList();
        final List names = getServiceNames(sigar, "Service.Exe.Ieq=" + exe);
        for (int i = 0; i < names.size(); ++i) {
            Service service = null;
            try {
                service = new Service(names.get(i));
                final ServiceConfig config = service.getConfig();
                services.add(config);
            }
            catch (Win32Exception e) {}
            finally {
                if (service != null) {
                    service.close();
                }
            }
        }
        return services;
    }
    
    public static List getServiceConfigs(final String exe) throws Win32Exception {
        final Sigar sigar = new Sigar();
        try {
            return getServiceConfigs(sigar, exe);
        }
        finally {
            sigar.close();
        }
    }
    
    public Service(final String serviceName) throws Win32Exception {
        this();
        this.service = OpenService(this.manager, serviceName, 983551);
        this.name = serviceName;
    }
    
    protected void finalize() {
        this.close();
    }
    
    public synchronized void close() {
        if (this.service != 0L) {
            CloseServiceHandle(this.service);
            this.service = 0L;
        }
        if (this.manager != 0L) {
            CloseServiceHandle(this.manager);
            this.manager = 0L;
        }
    }
    
    public static Service create(final ServiceConfig config) throws Win32Exception {
        if (config.getName() == null) {
            throw new IllegalArgumentException("name=null");
        }
        if (config.getPath() == null) {
            throw new IllegalArgumentException("path=null");
        }
        final Service service = new Service();
        service.service = CreateService(service.manager, config.getName(), config.getDisplayName(), config.getType(), config.getStartType(), config.getErrorControl(), config.getPath(), config.getDependencies(), config.getStartName(), config.getPassword());
        if (config.getDescription() != null) {
            service.setDescription(config.getDescription());
        }
        return service;
    }
    
    public void delete() throws Win32Exception {
        DeleteService(this.service);
    }
    
    public void setDescription(final String description) {
        ChangeServiceDescription(this.service, description);
    }
    
    public int status() {
        return this.getStatus();
    }
    
    public int getStatus() {
        return QueryServiceStatus(this.service);
    }
    
    public String getStatusString() {
        return Service.STATUS[this.getStatus()];
    }
    
    private void control(final int ctl) throws Win32Exception {
        ControlService(this.service, ctl);
    }
    
    public void stop() throws Win32Exception {
        this.control(1);
    }
    
    public void stopAndWait(final long timeout) throws Win32Exception {
        this.stop(timeout);
    }
    
    public void stop(final long timeout) throws Win32Exception {
        this.stop();
        final Waiter waiter = new Waiter(this, timeout, 1, 3);
        if (!waiter.sleep()) {
            throw new Win32Exception("Failed to stop service");
        }
    }
    
    public void start() throws Win32Exception {
        this.control(0);
    }
    
    public void start(final long timeout) throws Win32Exception {
        this.start();
        final Waiter waiter = new Waiter(this, timeout, 4, 2);
        if (!waiter.sleep()) {
            throw new Win32Exception("Failed to start service");
        }
    }
    
    public void pause() throws Win32Exception {
        this.control(2);
    }
    
    public void pause(final long timeout) throws Win32Exception {
        this.pause();
        final Waiter waiter = new Waiter(this, timeout, 7, 6);
        if (!waiter.sleep()) {
            throw new Win32Exception("Failed to pause service");
        }
    }
    
    public void resume() throws Win32Exception {
        this.control(3);
    }
    
    public ServiceConfig getConfig() throws Win32Exception {
        final ServiceConfig config = new ServiceConfig();
        QueryServiceConfig(this.service, config);
        config.setName(this.name);
        return config;
    }
    
    private static native boolean ChangeServiceDescription(final long p0, final String p1);
    
    private static native boolean CloseServiceHandle(final long p0);
    
    private static native long CreateService(final long p0, final String p1, final String p2, final int p3, final int p4, final int p5, final String p6, final String[] p7, final String p8, final String p9) throws Win32Exception;
    
    private static native void ControlService(final long p0, final int p1) throws Win32Exception;
    
    private static native void DeleteService(final long p0) throws Win32Exception;
    
    private static native long OpenSCManager(final String p0, final int p1) throws Win32Exception;
    
    private static native long OpenService(final long p0, final String p1, final int p2) throws Win32Exception;
    
    private static native int QueryServiceStatus(final long p0);
    
    private static native boolean QueryServiceConfig(final long p0, final ServiceConfig p1) throws Win32Exception;
    
    public void list(final PrintStream out) throws Win32Exception {
        this.getConfig().list(out);
        out.println("status........[" + this.getStatusString() + "]");
    }
    
    public static void main(final String[] args) throws Exception {
        List services;
        if (args.length == 0) {
            services = getServiceNames();
        }
        else {
            if (args.length == 2 && args[0].equals("-toggle")) {
                final long timeout = 5000L;
                final Service service = new Service(args[1]);
                if (service.getStatus() == 4) {
                    System.out.println("Stopping service...");
                    service.stop(timeout);
                }
                else {
                    System.out.println("Starting service...");
                    service.start(timeout);
                }
                System.out.println(service.getStatusString());
                return;
            }
            if (args.length == 1 && args[0].startsWith("Service.")) {
                final Sigar sigar = new Sigar();
                try {
                    services = getServiceNames(sigar, args[0]);
                }
                finally {
                    sigar.close();
                }
            }
            else {
                if (args.length == 1 && args[0].endsWith(".exe")) {
                    final Sigar sigar = new Sigar();
                    try {
                        services = getServiceConfigs(args[0]);
                    }
                    finally {
                        sigar.close();
                    }
                    for (int i = 0; i < services.size(); ++i) {
                        final ServiceConfig config = services.get(i);
                        config.list(System.out);
                        System.out.println("");
                    }
                    return;
                }
                services = Arrays.asList(args);
            }
        }
        for (int j = 0; j < services.size(); ++j) {
            final String name = services.get(j);
            final Service service = new Service(name);
            service.list(System.out);
            System.out.println("");
        }
    }
    
    static {
        STATUS = new String[] { "Unknown", "Stopped", "Start Pending", "Stop Pending", "Running", "Continue Pending", "Pause Pending", "Paused" };
    }
    
    private static class Waiter
    {
        long start;
        Service service;
        long timeout;
        int wantedState;
        int pendingState;
        
        Waiter(final Service service, final long timeout, final int wantedState, final int pendingState) {
            this.start = System.currentTimeMillis();
            this.service = service;
            this.timeout = timeout;
            this.wantedState = wantedState;
            this.pendingState = pendingState;
        }
        
        boolean sleep() {
            int status;
            while ((status = this.service.getStatus()) != this.wantedState) {
                if (status != this.pendingState) {
                    return false;
                }
                if (this.timeout > 0L) {
                    if (System.currentTimeMillis() - this.start >= this.timeout) {
                        break;
                    }
                }
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {}
            }
            return status == this.wantedState;
        }
    }
}
