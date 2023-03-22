// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import java.io.IOException;
import java.io.FileOutputStream;
import org.hyperic.sigar.DirStat;
import org.hyperic.sigar.FileInfo;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import java.io.File;
import java.util.Date;
import org.hyperic.sigar.Sigar;

public class TestFileInfo extends SigarTestCase
{
    public TestFileInfo(final String name) {
        super(name);
    }
    
    private void getFileInfo(final Sigar sigar, final String file) throws SigarException {
        this.traceln("Entry=" + file);
        final FileInfo info = sigar.getFileInfo(file);
        this.assertGtEqZeroTrace("Permisions", info.getPermissions());
        this.assertTrueTrace("Permissions", info.getPermissionsString());
        this.assertGtEqZeroTrace("Mode", info.getMode());
        this.assertTrueTrace("Type", info.getTypeString());
        this.assertGtEqZeroTrace("Size", info.getSize());
        this.assertGtEqZeroTrace("Uid", info.getUid());
        this.assertGtEqZeroTrace("Gid", info.getUid());
        this.assertGtEqZeroTrace("Inode", info.getInode());
        this.traceln("Device=" + info.getDevice());
        this.assertGtEqZeroTrace("Nlink", info.getNlink());
        this.assertGtEqZeroTrace("Atime", info.getAtime());
        this.traceln(new Date(info.getAtime()).toString());
        this.assertGtZeroTrace("Mtime", info.getMtime());
        this.traceln(new Date(info.getMtime()).toString());
        this.assertGtZeroTrace("Ctime", info.getCtime());
        this.traceln(new Date(info.getCtime()).toString());
        if (info.getType() == 2) {
            try {
                final DirStat stats = sigar.getDirStat(file);
                this.assertEqualsTrace("Total", new File(file).list().length, stats.getTotal());
                this.assertGtEqZeroTrace("Files", stats.getFiles());
                this.assertGtEqZeroTrace("Subdirs", stats.getSubdirs());
            }
            catch (SigarNotImplementedException e) {}
        }
        else {
            try {
                sigar.getDirStat(file);
                assertTrue(false);
            }
            catch (SigarException e2) {
                assertTrue(true);
            }
        }
        sigar.getLinkInfo(file);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final File dir = new File(System.getProperty("user.dir"));
        final String[] entries = dir.list();
        for (int i = 0; i < entries.length; ++i) {
            final String file = entries[i];
            final File testFile = new File(dir, file);
            if (testFile.exists()) {
                if (testFile.canRead()) {
                    if (!testFile.isHidden()) {
                        this.traceln(file + ":");
                        this.getFileInfo(sigar, testFile.getAbsolutePath());
                    }
                }
            }
        }
        String file = "NO SUCH FILE";
        try {
            this.getFileInfo(sigar, file);
            assertTrue(false);
        }
        catch (SigarNotImplementedException e3) {}
        catch (SigarException e) {
            this.traceln(file + ": " + e.getMessage());
            assertTrue(true);
        }
        final File tmp = File.createTempFile("sigar-", "");
        file = tmp.getAbsolutePath();
        tmp.deleteOnExit();
        this.traceln("TMP=" + file);
        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException ex) {}
        try {
            final FileInfo info = sigar.getFileInfo(file);
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                os.write(1);
            }
            catch (IOException ioe) {
                throw ioe;
            }
            finally {
                if (os != null) {
                    try {
                        os.close();
                    }
                    catch (IOException ex2) {}
                }
            }
            tmp.setReadOnly();
            final boolean changed = info.changed();
            this.traceln(info.diff());
            assertTrue(info.getPreviousInfo().getSize() != info.getSize());
            assertTrue(changed);
        }
        catch (SigarNotImplementedException e4) {}
        catch (SigarException e2) {
            throw e2;
        }
    }
}
