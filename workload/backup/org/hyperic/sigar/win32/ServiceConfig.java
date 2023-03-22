// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import java.util.Arrays;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ServiceConfig
{
    public static final int START_BOOT = 0;
    public static final int START_SYSTEM = 1;
    public static final int START_AUTO = 2;
    public static final int START_MANUAL = 3;
    public static final int START_DISABLED = 4;
    public static final int TYPE_KERNEL_DRIVER = 1;
    public static final int TYPE_FILE_SYSTEM_DRIVER = 2;
    public static final int TYPE_ADAPTER = 4;
    public static final int TYPE_RECOGNIZER_DRIVER = 8;
    public static final int TYPE_WIN32_OWN_PROCESS = 16;
    public static final int TYPE_WIN32_SHARE_PROCESS = 32;
    public static final int TYPE_INTERACTIVE_PROCESS = 256;
    public static final int ERROR_IGNORE = 0;
    public static final int ERROR_NORMAL = 1;
    public static final int ERROR_SEVERE = 2;
    public static final int ERROR_CRITICAL = 3;
    private static final String[] START_TYPES;
    private static final String[] ERROR_TYPES;
    int type;
    int startType;
    int errorControl;
    String path;
    String exe;
    String[] argv;
    String loadOrderGroup;
    int tagId;
    String[] dependencies;
    String startName;
    String displayName;
    String description;
    String password;
    String name;
    
    ServiceConfig() {
    }
    
    public ServiceConfig(final String name) {
        this.name = name;
        this.type = 16;
        this.startType = 2;
        this.errorControl = 1;
        this.password = "";
    }
    
    public String getPath() {
        return this.path;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public String[] getArgv() {
        return this.argv;
    }
    
    public String getExe() {
        if (this.exe == null) {
            final String[] argv = this.getArgv();
            if (argv != null && argv.length != 0) {
                this.exe = argv[0];
            }
        }
        return this.exe;
    }
    
    public String[] getDependencies() {
        if (this.dependencies == null) {
            return new String[0];
        }
        return this.dependencies;
    }
    
    public void setDependencies(final String[] dependencies) {
        this.dependencies = dependencies;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public int getErrorControl() {
        return this.errorControl;
    }
    
    public void setErrorControl(final int errorControl) {
        this.errorControl = errorControl;
    }
    
    public String getErrorControlString() {
        return ServiceConfig.ERROR_TYPES[this.getErrorControl()];
    }
    
    public String getLoadOrderGroup() {
        return this.loadOrderGroup;
    }
    
    public void setLoadOrderGroup(final String loadOrderGroup) {
        this.loadOrderGroup = loadOrderGroup;
    }
    
    public String getStartName() {
        return this.startName;
    }
    
    public void setStartName(final String startName) {
        this.startName = startName;
    }
    
    public int getStartType() {
        return this.startType;
    }
    
    public void setStartType(final int startType) {
        this.startType = startType;
    }
    
    public String getStartTypeString() {
        return ServiceConfig.START_TYPES[this.getStartType()];
    }
    
    public int getTagId() {
        return this.tagId;
    }
    
    public void setTagId(final int tagId) {
        this.tagId = tagId;
    }
    
    public int getType() {
        return this.type;
    }
    
    public List getTypeList() {
        final ArrayList types = new ArrayList();
        if ((this.type & 0x1) != 0x0) {
            types.add("Kernel Driver");
        }
        if ((this.type & 0x2) != 0x0) {
            types.add("File System Driver");
        }
        if ((this.type & 0x4) != 0x0) {
            types.add("Adapter");
        }
        if ((this.type & 0x8) != 0x0) {
            types.add("Recognizer Driver");
        }
        if ((this.type & 0x10) != 0x0) {
            types.add("Own Process");
        }
        if ((this.type & 0x20) != 0x0) {
            types.add("Share Process");
        }
        if ((this.type & 0x100) != 0x0) {
            types.add("Interactive Process");
        }
        return types;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void list(final PrintStream out) throws Win32Exception {
        out.println("name..........[" + this.getName() + "]");
        out.println("display.......[" + this.getDisplayName() + "]");
        out.println("description...[" + this.getDescription() + "]");
        out.println("start type....[" + this.getStartTypeString() + "]");
        out.println("start name....[" + this.getStartName() + "]");
        out.println("type.........." + this.getTypeList());
        out.println("path..........[" + this.getPath() + "]");
        out.println("exe...........[" + this.getExe() + "]");
        out.println("deps.........." + Arrays.asList(this.getDependencies()));
        out.println("error ctl.....[" + this.getErrorControlString() + "]");
    }
    
    static {
        START_TYPES = new String[] { "Boot", "System", "Auto", "Manual", "Disabled" };
        ERROR_TYPES = new String[] { "Ignore", "Normal", "Severe", "Critical" };
    }
}
