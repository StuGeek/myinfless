// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import org.hyperic.sigar.SigarLoader;
import java.util.Iterator;
import java.io.File;
import java.io.FilenameFilter;

public class FileCompleter extends CollectionCompleter implements FilenameFilter
{
    private static final String HOME;
    private String name;
    
    public FileCompleter() {
    }
    
    public FileCompleter(final ShellBase shell) {
        super(shell);
    }
    
    public static String expand(final String name) {
        if (name.startsWith("~")) {
            return FileCompleter.HOME + name.substring(1, name.length());
        }
        return name;
    }
    
    public boolean accept(final File dir, final String name) {
        return !name.equals(".") && !name.equals("..") && name.startsWith(this.name);
    }
    
    public Iterator getIterator() {
        return null;
    }
    
    private String appendSep(final String name) {
        if (name.endsWith(File.separator)) {
            return name;
        }
        return name + File.separator;
    }
    
    private boolean isDotFile(final File file) {
        return file.getName().equals(".") && file.getParentFile() != null;
    }
    
    public String complete(final String line) {
        String fileName = line;
        boolean isHome = false;
        if (line.length() == 0) {
            return this.appendSep(".");
        }
        if (fileName.startsWith("~")) {
            isHome = true;
            fileName = expand(fileName);
        }
        final File file = new File(fileName);
        File dir;
        if (file.exists() && !this.isDotFile(file)) {
            if (!file.isDirectory()) {
                return line;
            }
            this.name = null;
            dir = file;
            if (!fileName.endsWith(File.separator)) {
                return line + File.separator;
            }
        }
        else {
            this.name = file.getName();
            dir = file.getParentFile();
            if (dir == null) {
                if (SigarLoader.IS_WIN32 && line.length() == 1 && Character.isLetter(line.charAt(0))) {
                    return line + ":\\";
                }
                return line;
            }
            else if (!dir.exists() || !dir.isDirectory()) {
                return line;
            }
        }
        String[] list;
        if (this.name == null) {
            list = dir.list();
        }
        else {
            list = dir.list(this);
        }
        if (list.length == 1) {
            fileName = this.appendSep(dir.toString()) + list[0];
            if (new File(fileName).isDirectory()) {
                fileName = this.appendSep(fileName);
            }
            if (isHome) {
                return "~" + fileName.substring(FileCompleter.HOME.length(), fileName.length());
            }
            return fileName;
        }
        else {
            final String partial = this.displayPossible(list);
            if (partial != null) {
                return this.appendSep(dir.toString()) + partial;
            }
            return line;
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final String line = new FileCompleter().complete(args[0]);
        System.out.println("\nsigar> '" + line + "'");
    }
    
    static {
        HOME = System.getProperty("user.home");
    }
}
