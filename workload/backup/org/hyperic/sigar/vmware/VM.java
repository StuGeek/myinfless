// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.vmware;

public class VM extends VMwareObject
{
    public static final int EXECUTION_STATE_ON = 1;
    public static final int EXECUTION_STATE_OFF = 2;
    public static final int EXECUTION_STATE_SUSPENDED = 3;
    public static final int EXECUTION_STATE_STUCK = 4;
    public static final int EXECUTION_STATE_UNKNOWN = 5;
    public static final String[] EXECUTION_STATES;
    public static final int POWEROP_MODE_HARD = 1;
    public static final int POWEROP_MODE_SOFT = 2;
    public static final int POWEROP_MODE_TRYSOFT = 3;
    private static final int POWEROP_MODE_DEFAULT = 3;
    public static final int PRODUCT_WS = 1;
    public static final int PRODUCT_GSX = 2;
    public static final int PRODUCT_ESX = 3;
    public static final int PRODUCT_SERVER = 4;
    public static final int PRODUCT_UNKNOWN = 5;
    public static final String GSX = "GSX";
    public static final String ESX = "ESX";
    public static final String SERVER = "Server";
    public static final String[] PRODUCTS;
    public static final int PLATFORM_WINDOWS = 1;
    public static final int PLATFORM_LINUX = 2;
    public static final int PLATFORM_VMNIX = 3;
    public static final int PLATFORM_UNKNOWN = 4;
    public static final String[] PLATFORMS;
    public static final int PRODINFO_PRODUCT = 1;
    public static final int PRODINFO_PLATFORM = 2;
    public static final int PRODINFO_BUILD = 3;
    public static final int PRODINFO_VERSION_MAJOR = 4;
    public static final int PRODINFO_VERSION_MINOR = 5;
    public static final int PRODINFO_VERSION_REVISION = 6;
    public static final int PERM_READ = 4;
    public static final int PERM_WRITE = 2;
    public static final int PERM_EXECUTE = 1;
    
    native void destroy();
    
    private native void create();
    
    private native void connect(final ConnectParams p0, final String p1, final int p2) throws VMwareException;
    
    public void connect(final ConnectParams params, final String config, final boolean mks) throws VMwareException {
        this.connect(params, config, mks ? 1 : 0);
    }
    
    public void connect(final ConnectParams params, final String config) throws VMwareException {
        this.connect(params, config, 0);
    }
    
    public native void disconnect();
    
    public native boolean isConnected();
    
    public native int getExecutionState() throws VMwareException;
    
    public native int getRemoteConnections() throws VMwareException;
    
    public native int getUptime() throws VMwareException;
    
    public native int getHeartbeat() throws VMwareException;
    
    public native int getToolsLastActive() throws VMwareException;
    
    public native String getRunAsUser() throws VMwareException;
    
    public native int getPermissions() throws VMwareException;
    
    public String getPermissionsString() {
        final char[] perms = { '-', '-', '-' };
        try {
            final int bits = this.getPermissions();
            if ((bits & 0x4) != 0x0) {
                perms[0] = 'r';
            }
            if ((bits & 0x2) != 0x0) {
                perms[1] = 'w';
            }
            if ((bits & 0x1) != 0x0) {
                perms[2] = 'x';
            }
        }
        catch (VMwareException ex) {}
        return new String(perms);
    }
    
    private boolean checkPermission(final int perm) {
        try {
            return (this.getPermissions() & perm) != 0x0;
        }
        catch (VMwareException e) {
            return false;
        }
    }
    
    public boolean canRead() {
        return this.checkPermission(4);
    }
    
    public boolean canWrite() {
        return this.checkPermission(2);
    }
    
    public boolean canExecute() {
        return this.checkPermission(1);
    }
    
    public native String getConfig(final String p0) throws VMwareException;
    
    public native String getResource(final String p0) throws VMwareException;
    
    public native String getGuestInfo(final String p0) throws VMwareException;
    
    public native void setGuestInfo(final String p0, final String p1) throws VMwareException;
    
    public native int getProductInfo(final int p0) throws VMwareException;
    
    public native long getPid() throws VMwareException;
    
    public native int getId() throws VMwareException;
    
    public String getVersion() throws VMwareException {
        return this.getProductInfo(4) + "." + this.getProductInfo(5);
    }
    
    public String getFullVersion() throws VMwareException {
        return this.getVersion() + "." + this.getProductInfo(6) + "." + this.getProductInfo(3);
    }
    
    private String getConfigEx(final String key) {
        try {
            return this.getConfig(key);
        }
        catch (VMwareException e) {
            return null;
        }
    }
    
    public String getDisplayName() {
        return this.getConfigEx("displayName");
    }
    
    public String getGuestOS() {
        return this.getConfigEx("guestOS");
    }
    
    public String getMemSize() {
        return this.getConfigEx("memsize");
    }
    
    public String getProductName() {
        try {
            final int info = this.getProductInfo(1);
            return VM.PRODUCTS[info] + " " + this.getVersion();
        }
        catch (VMwareException e) {
            return null;
        }
    }
    
    public String getProductPlatform() {
        try {
            final int info = this.getProductInfo(2);
            return VM.PLATFORMS[info];
        }
        catch (VMwareException e) {
            return null;
        }
    }
    
    private boolean isState(final int state) {
        try {
            return this.getExecutionState() == state;
        }
        catch (VMwareException e) {
            return false;
        }
    }
    
    public boolean isOn() {
        return this.isState(1);
    }
    
    public boolean isOff() {
        return this.isState(2);
    }
    
    public boolean isSuspended() {
        return this.isState(3);
    }
    
    public boolean isStuck() {
        return this.isState(4);
    }
    
    public boolean isESX() {
        try {
            return this.getProductInfo(1) == 3;
        }
        catch (VMwareException e) {
            return false;
        }
    }
    
    public boolean isGSX() {
        try {
            return this.getProductInfo(1) == 2;
        }
        catch (VMwareException e) {
            return false;
        }
    }
    
    public native void start(final int p0) throws VMwareException;
    
    public void start() throws VMwareException {
        this.start(3);
    }
    
    public native void stop(final int p0) throws VMwareException;
    
    public void stop() throws VMwareException {
        this.stop(3);
    }
    
    public native void reset(final int p0) throws VMwareException;
    
    public void reset() throws VMwareException {
        this.reset(3);
    }
    
    public native void suspend(final int p0) throws VMwareException;
    
    public void suspend() throws VMwareException {
        this.suspend(3);
    }
    
    public void resume(final int mode) throws VMwareException {
        final int state = this.getExecutionState();
        if (state != 3) {
            throw new VMwareException("VM state is not suspended: " + VM.EXECUTION_STATES[state]);
        }
        this.start(mode);
    }
    
    public void resume() throws VMwareException {
        this.resume(3);
    }
    
    private native void createNamedSnapshot(final String p0, final String p1, final boolean p2, final boolean p3) throws VMwareException;
    
    private native void createDefaultSnapshot() throws VMwareException;
    
    public void createSnapshot(final String name, final String description, final boolean quiesce, final boolean memory) throws VMwareException {
        if (this.isESX()) {
            this.createNamedSnapshot(name, description, quiesce, memory);
        }
        else {
            this.createDefaultSnapshot();
        }
    }
    
    public native void revertToSnapshot() throws VMwareException;
    
    public native void removeAllSnapshots() throws VMwareException;
    
    public native boolean hasSnapshot() throws VMwareException;
    
    public native void saveScreenshot(final String p0) throws VMwareException;
    
    public native void deviceConnect(final String p0) throws VMwareException;
    
    public native void deviceDisconnect(final String p0) throws VMwareException;
    
    public native boolean deviceIsConnected(final String p0) throws VMwareException;
    
    public VM() {
        this.create();
    }
    
    static {
        EXECUTION_STATES = new String[] { "INVALID", "ON", "OFF", "SUSPENDED", "STUCK", "UNKNOWN" };
        PRODUCTS = new String[] { "INVALID", "Workstation", "GSX", "ESX", "Server", "UNKNOWN" };
        PLATFORMS = new String[] { "INVALID", "Windows", "Linux", "VmNix", "UNKNOWN" };
    }
}
