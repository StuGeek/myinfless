// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class TestFileSystem extends SigarTestCase
{
    public TestFileSystem(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = this.getSigar();
        final FileSystem[] fslist = sigar.getFileSystemList();
        final FileSystemMap mounts = sigar.getFileSystemMap();
        final String dir = System.getProperty("user.home");
        this.assertTrueTrace("\nMountPoint for " + dir, mounts.getMountPoint(dir).getDirName());
        for (int i = 0; i < fslist.length; ++i) {
            final FileSystem fs = fslist[i];
            assertTrue(mounts.getFileSystem(fs.getDirName()) != null);
            this.assertLengthTrace("DevName", fs.getDevName());
            this.assertLengthTrace("DirName", fs.getDirName());
            this.assertLengthTrace("TypeName", fs.getTypeName());
            this.assertLengthTrace("SysTypeName", fs.getSysTypeName());
            this.traceln("Options=" + fs.getOptions());
            FileSystemUsage usage;
            try {
                usage = sigar.getFileSystemUsage(fs.getDirName());
            }
            catch (SigarException e) {
                if (fs.getType() == 2) {
                    throw e;
                }
                continue;
            }
            switch (fs.getType()) {
                case 2: {
                    this.assertGtZeroTrace("  Total", usage.getTotal());
                    this.assertGtEqZeroTrace("  Free", usage.getFree());
                    this.assertGtEqZeroTrace("  Avail", usage.getAvail());
                    this.assertGtEqZeroTrace("   Used", usage.getUsed());
                    final double usePercent = usage.getUsePercent() * 100.0;
                    this.traceln("  Usage=" + usePercent + "%");
                    assertTrue(usePercent <= 100.0);
                    break;
                }
            }
            this.traceln("  DiskReads=" + usage.getDiskReads());
            this.traceln("  DiskWrites=" + usage.getDiskWrites());
        }
        try {
            sigar.getFileSystemUsage("T O T A L L Y B O G U S");
            assertTrue(false);
        }
        catch (SigarException e2) {
            assertTrue(true);
        }
    }
}
