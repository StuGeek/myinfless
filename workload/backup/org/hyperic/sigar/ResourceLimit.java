// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ResourceLimit implements Serializable
{
    private static final long serialVersionUID = 32184L;
    long cpuCur;
    long cpuMax;
    long fileSizeCur;
    long fileSizeMax;
    long pipeSizeMax;
    long pipeSizeCur;
    long dataCur;
    long dataMax;
    long stackCur;
    long stackMax;
    long coreCur;
    long coreMax;
    long memoryCur;
    long memoryMax;
    long processesCur;
    long processesMax;
    long openFilesCur;
    long openFilesMax;
    long virtualMemoryCur;
    long virtualMemoryMax;
    
    public ResourceLimit() {
        this.cpuCur = 0L;
        this.cpuMax = 0L;
        this.fileSizeCur = 0L;
        this.fileSizeMax = 0L;
        this.pipeSizeMax = 0L;
        this.pipeSizeCur = 0L;
        this.dataCur = 0L;
        this.dataMax = 0L;
        this.stackCur = 0L;
        this.stackMax = 0L;
        this.coreCur = 0L;
        this.coreMax = 0L;
        this.memoryCur = 0L;
        this.memoryMax = 0L;
        this.processesCur = 0L;
        this.processesMax = 0L;
        this.openFilesCur = 0L;
        this.openFilesMax = 0L;
        this.virtualMemoryCur = 0L;
        this.virtualMemoryMax = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static ResourceLimit fetch(final Sigar sigar) throws SigarException {
        final ResourceLimit resourceLimit = new ResourceLimit();
        resourceLimit.gather(sigar);
        return resourceLimit;
    }
    
    public long getCpuCur() {
        return this.cpuCur;
    }
    
    public long getCpuMax() {
        return this.cpuMax;
    }
    
    public long getFileSizeCur() {
        return this.fileSizeCur;
    }
    
    public long getFileSizeMax() {
        return this.fileSizeMax;
    }
    
    public long getPipeSizeMax() {
        return this.pipeSizeMax;
    }
    
    public long getPipeSizeCur() {
        return this.pipeSizeCur;
    }
    
    public long getDataCur() {
        return this.dataCur;
    }
    
    public long getDataMax() {
        return this.dataMax;
    }
    
    public long getStackCur() {
        return this.stackCur;
    }
    
    public long getStackMax() {
        return this.stackMax;
    }
    
    public long getCoreCur() {
        return this.coreCur;
    }
    
    public long getCoreMax() {
        return this.coreMax;
    }
    
    public long getMemoryCur() {
        return this.memoryCur;
    }
    
    public long getMemoryMax() {
        return this.memoryMax;
    }
    
    public long getProcessesCur() {
        return this.processesCur;
    }
    
    public long getProcessesMax() {
        return this.processesMax;
    }
    
    public long getOpenFilesCur() {
        return this.openFilesCur;
    }
    
    public long getOpenFilesMax() {
        return this.openFilesMax;
    }
    
    public long getVirtualMemoryCur() {
        return this.virtualMemoryCur;
    }
    
    public long getVirtualMemoryMax() {
        return this.virtualMemoryMax;
    }
    
    void copyTo(final ResourceLimit copy) {
        copy.cpuCur = this.cpuCur;
        copy.cpuMax = this.cpuMax;
        copy.fileSizeCur = this.fileSizeCur;
        copy.fileSizeMax = this.fileSizeMax;
        copy.pipeSizeMax = this.pipeSizeMax;
        copy.pipeSizeCur = this.pipeSizeCur;
        copy.dataCur = this.dataCur;
        copy.dataMax = this.dataMax;
        copy.stackCur = this.stackCur;
        copy.stackMax = this.stackMax;
        copy.coreCur = this.coreCur;
        copy.coreMax = this.coreMax;
        copy.memoryCur = this.memoryCur;
        copy.memoryMax = this.memoryMax;
        copy.processesCur = this.processesCur;
        copy.processesMax = this.processesMax;
        copy.openFilesCur = this.openFilesCur;
        copy.openFilesMax = this.openFilesMax;
        copy.virtualMemoryCur = this.virtualMemoryCur;
        copy.virtualMemoryMax = this.virtualMemoryMax;
    }
    
    public static native long INFINITY();
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strcpuCur = String.valueOf(this.cpuCur);
        if (!"-1".equals(strcpuCur)) {
            map.put("CpuCur", strcpuCur);
        }
        final String strcpuMax = String.valueOf(this.cpuMax);
        if (!"-1".equals(strcpuMax)) {
            map.put("CpuMax", strcpuMax);
        }
        final String strfileSizeCur = String.valueOf(this.fileSizeCur);
        if (!"-1".equals(strfileSizeCur)) {
            map.put("FileSizeCur", strfileSizeCur);
        }
        final String strfileSizeMax = String.valueOf(this.fileSizeMax);
        if (!"-1".equals(strfileSizeMax)) {
            map.put("FileSizeMax", strfileSizeMax);
        }
        final String strpipeSizeMax = String.valueOf(this.pipeSizeMax);
        if (!"-1".equals(strpipeSizeMax)) {
            map.put("PipeSizeMax", strpipeSizeMax);
        }
        final String strpipeSizeCur = String.valueOf(this.pipeSizeCur);
        if (!"-1".equals(strpipeSizeCur)) {
            map.put("PipeSizeCur", strpipeSizeCur);
        }
        final String strdataCur = String.valueOf(this.dataCur);
        if (!"-1".equals(strdataCur)) {
            map.put("DataCur", strdataCur);
        }
        final String strdataMax = String.valueOf(this.dataMax);
        if (!"-1".equals(strdataMax)) {
            map.put("DataMax", strdataMax);
        }
        final String strstackCur = String.valueOf(this.stackCur);
        if (!"-1".equals(strstackCur)) {
            map.put("StackCur", strstackCur);
        }
        final String strstackMax = String.valueOf(this.stackMax);
        if (!"-1".equals(strstackMax)) {
            map.put("StackMax", strstackMax);
        }
        final String strcoreCur = String.valueOf(this.coreCur);
        if (!"-1".equals(strcoreCur)) {
            map.put("CoreCur", strcoreCur);
        }
        final String strcoreMax = String.valueOf(this.coreMax);
        if (!"-1".equals(strcoreMax)) {
            map.put("CoreMax", strcoreMax);
        }
        final String strmemoryCur = String.valueOf(this.memoryCur);
        if (!"-1".equals(strmemoryCur)) {
            map.put("MemoryCur", strmemoryCur);
        }
        final String strmemoryMax = String.valueOf(this.memoryMax);
        if (!"-1".equals(strmemoryMax)) {
            map.put("MemoryMax", strmemoryMax);
        }
        final String strprocessesCur = String.valueOf(this.processesCur);
        if (!"-1".equals(strprocessesCur)) {
            map.put("ProcessesCur", strprocessesCur);
        }
        final String strprocessesMax = String.valueOf(this.processesMax);
        if (!"-1".equals(strprocessesMax)) {
            map.put("ProcessesMax", strprocessesMax);
        }
        final String stropenFilesCur = String.valueOf(this.openFilesCur);
        if (!"-1".equals(stropenFilesCur)) {
            map.put("OpenFilesCur", stropenFilesCur);
        }
        final String stropenFilesMax = String.valueOf(this.openFilesMax);
        if (!"-1".equals(stropenFilesMax)) {
            map.put("OpenFilesMax", stropenFilesMax);
        }
        final String strvirtualMemoryCur = String.valueOf(this.virtualMemoryCur);
        if (!"-1".equals(strvirtualMemoryCur)) {
            map.put("VirtualMemoryCur", strvirtualMemoryCur);
        }
        final String strvirtualMemoryMax = String.valueOf(this.virtualMemoryMax);
        if (!"-1".equals(strvirtualMemoryMax)) {
            map.put("VirtualMemoryMax", strvirtualMemoryMax);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
