// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;

public class FileSystemMap extends HashMap
{
    public Object put(final Object key, final Object value) {
        throw new UnsupportedOperationException();
    }
    
    public void init(final FileSystem[] fslist) {
        super.clear();
        for (int i = 0; i < fslist.length; ++i) {
            super.put(fslist[i].getDirName(), fslist[i]);
        }
    }
    
    public FileSystem getFileSystem(final String name) {
        return this.get(name);
    }
    
    public boolean isMounted(final String name) {
        return this.get(name) != null;
    }
    
    public FileSystem getMountPoint(final String name) {
        FileSystem fs = this.getFileSystem(name);
        if (fs != null) {
            return fs;
        }
        File dir = new File(name);
        if (!dir.exists()) {
            return null;
        }
        try {
            dir = dir.getCanonicalFile();
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (!dir.isDirectory()) {
            dir = dir.getParentFile();
        }
        do {
            fs = this.getFileSystem(dir.toString());
            if (fs != null) {
                return fs;
            }
            dir = dir.getParentFile();
        } while (dir != null);
        return null;
    }
}
