// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import junit.textui.TestRunner;
import org.apache.log4j.PropertyConfigurator;
import java.util.Properties;
import junit.framework.TestSuite;
import org.hyperic.sigar.cmd.Version;
import junit.framework.TestFailure;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.Test;
import java.io.PrintStream;
import java.util.HashMap;
import junit.textui.ResultPrinter;

public class SigarTestPrinter extends ResultPrinter
{
    private HashMap failures;
    private int maxNameLen;
    private static boolean printedVersion;
    private static final String PREFIX = "org.hyperic.sigar.test.";
    private static final String[][] LOG_PROPS;
    
    public SigarTestPrinter(final PrintStream writer) {
        super(writer);
        this.failures = new HashMap();
        this.maxNameLen = 0;
    }
    
    public void startTest(final Test test) {
        final PrintStream writer = this.getWriter();
        String cls = test.getClass().getName();
        cls = cls.substring("org.hyperic.sigar.test.".length());
        final String method = ((TestCase)test).getName();
        final String name = cls + "." + method;
        writer.print(name);
        for (int n = this.maxNameLen + 3 - name.length(), i = 0; i < n; ++i) {
            writer.print('.');
        }
    }
    
    public void addFailure(final Test test, final AssertionFailedError t) {
        this.failures.put(test, Boolean.TRUE);
        this.getWriter().println("FAILED");
    }
    
    public void addError(final Test test, final Throwable t) {
        this.failures.put(test, Boolean.TRUE);
        this.getWriter().println("ERROR");
    }
    
    public void endTest(final Test test) {
        if (this.failures.get(test) != Boolean.TRUE) {
            this.getWriter().println("ok");
        }
    }
    
    protected void printDefectHeader(final TestFailure failure, final int count) {
        this.getWriter().println(count + ") " + failure.failedTest().getClass().getName() + ":");
    }
    
    public void printVersionInfo() {
        if (SigarTestPrinter.printedVersion) {
            return;
        }
        SigarTestPrinter.printedVersion = true;
        final PrintStream writer = this.getWriter();
        Version.printInfo(writer);
        writer.println("");
    }
    
    public static void addTest(final SigarTestPrinter printer, final TestSuite suite, final Class test) {
        final int len = test.getName().length();
        if (len > printer.maxNameLen) {
            printer.maxNameLen = len;
        }
        suite.addTestSuite(test);
    }
    
    private static Class findTest(final Class[] tests, final String name) {
        final String tname = "Test" + name;
        for (int i = 0; i < tests.length; ++i) {
            if (tests[i].getName().endsWith(tname)) {
                return tests[i];
            }
        }
        return null;
    }
    
    public static void runTests(final Class[] tests, final String[] args) {
        final TestSuite suite = new TestSuite("Sigar tests");
        final SigarTestPrinter printer = new SigarTestPrinter(System.out);
        printer.printVersionInfo();
        if (args.length > 0) {
            final Properties props = new Properties();
            for (int i = 0; i < SigarTestPrinter.LOG_PROPS.length; ++i) {
                props.setProperty(SigarTestPrinter.LOG_PROPS[i][0], SigarTestPrinter.LOG_PROPS[i][1]);
            }
            PropertyConfigurator.configure(props);
            SigarTestCase.setVerbose(true);
            SigarTestCase.setWriter(printer.getWriter());
            for (int i = 0; i < args.length; ++i) {
                final Class test = findTest(tests, args[i]);
                if (test == null) {
                    final String msg = "Invalid test: " + args[i];
                    throw new IllegalArgumentException(msg);
                }
                addTest(printer, suite, test);
            }
        }
        else {
            for (int j = 0; j < tests.length; ++j) {
                addTest(printer, suite, tests[j]);
            }
        }
        final TestRunner runner = new TestRunner((ResultPrinter)printer);
        runner.doRun((Test)suite);
    }
    
    static {
        LOG_PROPS = new String[][] { { "log4j.rootLogger", "DEBUG, R" }, { "log4j.appender.R", "org.apache.log4j.ConsoleAppender" }, { "log4j.appender.R.layout", "org.apache.log4j.PatternLayout" }, { "log4j.appender.R.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n" } };
    }
}
