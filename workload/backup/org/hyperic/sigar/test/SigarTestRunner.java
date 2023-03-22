// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.win32.test.TestFileVersion;
import org.hyperic.sigar.win32.test.TestService;
import org.hyperic.sigar.win32.test.TestRegistryKey;
import org.hyperic.sigar.win32.test.TestMetaBase;
import org.hyperic.sigar.win32.test.TestPdh;
import org.hyperic.sigar.win32.test.TestLocaleInfo;
import org.hyperic.sigar.win32.test.TestEventLog;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.SigarException;
import java.util.ArrayList;
import org.hyperic.sigar.cmd.Shell;
import java.util.Collection;
import org.hyperic.sigar.cmd.SigarCommandBase;

public class SigarTestRunner extends SigarCommandBase
{
    private Collection completions;
    private static final Class[] TESTS;
    private static final Class[] ALL_TESTS;
    private static final Class[] WIN32_TESTS;
    
    public SigarTestRunner(final Shell shell) {
        super(shell);
        this.completions = new ArrayList();
        for (int i = 0; i < SigarTestRunner.TESTS.length; ++i) {
            final String name = SigarTestRunner.TESTS[i].getName();
            final int ix = name.lastIndexOf(".Test");
            this.completions.add(name.substring(ix + 5));
        }
    }
    
    public SigarTestRunner() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getSyntaxArgs() {
        return "[testclass]";
    }
    
    public String getUsageShort() {
        return "Run sigar tests";
    }
    
    public Collection getCompletions() {
        return this.completions;
    }
    
    public void output(final String[] args) throws SigarException {
        SigarTestPrinter.runTests(SigarTestRunner.TESTS, args);
    }
    
    public static void main(final String[] args) throws Exception {
        new SigarTestRunner().processCommand(args);
    }
    
    static {
        ALL_TESTS = new Class[] { TestLog.class, TestInvoker.class, TestPTQL.class, TestCpu.class, TestCpuInfo.class, TestFileInfo.class, TestFileSystem.class, TestFQDN.class, TestLoadAverage.class, TestMem.class, TestNetIf.class, TestNetInfo.class, TestNetRoute.class, TestNetStat.class, TestNetStatPort.class, TestTcpStat.class, TestNfsClientV2.class, TestNfsServerV2.class, TestNfsClientV3.class, TestNfsServerV3.class, TestProcArgs.class, TestProcEnv.class, TestProcExe.class, TestProcModules.class, TestProcFd.class, TestProcList.class, TestProcMem.class, TestProcState.class, TestProcStat.class, TestProcTime.class, TestResourceLimit.class, TestSignal.class, TestSwap.class, TestThreadCpu.class, TestUptime.class, TestVMware.class, TestWho.class, TestHumidor.class };
        WIN32_TESTS = new Class[] { TestEventLog.class, TestLocaleInfo.class, TestPdh.class, TestMetaBase.class, TestRegistryKey.class, TestService.class, TestFileVersion.class };
        if (SigarLoader.IS_WIN32) {
            TESTS = new Class[SigarTestRunner.ALL_TESTS.length + SigarTestRunner.WIN32_TESTS.length];
            System.arraycopy(SigarTestRunner.ALL_TESTS, 0, SigarTestRunner.TESTS, 0, SigarTestRunner.ALL_TESTS.length);
            System.arraycopy(SigarTestRunner.WIN32_TESTS, 0, SigarTestRunner.TESTS, SigarTestRunner.ALL_TESTS.length, SigarTestRunner.WIN32_TESTS.length);
        }
        else {
            TESTS = SigarTestRunner.ALL_TESTS;
        }
    }
}
