// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import java.lang.reflect.Method;
import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.ptql.ProcessQueryFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Properties;
import org.hyperic.sigar.Sigar;
import junit.framework.TestCase;

public abstract class SigarTestCase extends TestCase
{
    private static Sigar sigar;
    private Properties props;
    private static boolean verbose;
    protected static final boolean JDK_14_COMPAT;
    private static PrintStream out;
    
    public SigarTestCase(final String name) {
        super(name);
        this.props = new Properties();
        final File f = new File(System.getProperty("user.home"), ".sigar.properties");
        if (f.exists()) {
            FileInputStream is = null;
            try {
                is = new FileInputStream(f);
                this.props.load(is);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (IOException ex) {}
                }
            }
        }
    }
    
    public Sigar getSigar() {
        if (SigarTestCase.sigar == null) {
            SigarTestCase.sigar = new Sigar();
            if (getVerbose()) {
                SigarTestCase.sigar.enableLogging(true);
            }
        }
        return SigarTestCase.sigar;
    }
    
    public static void closeSigar() {
        if (SigarTestCase.sigar != null) {
            SigarTestCase.sigar.close();
            SigarTestCase.sigar = null;
        }
        ProcessQueryFactory.getInstance().clear();
        Humidor.getInstance().close();
    }
    
    public Properties getProperties() {
        return this.props;
    }
    
    public String getProperty(final String key, final String val) {
        return this.getProperties().getProperty(key, val);
    }
    
    public String getProperty(final String key) {
        return this.getProperty(key, null);
    }
    
    public static void setVerbose(final boolean value) {
        SigarTestCase.verbose = value;
    }
    
    public static boolean getVerbose() {
        return SigarTestCase.verbose;
    }
    
    public static void setWriter(final PrintStream value) {
        SigarTestCase.out = value;
    }
    
    public static PrintStream getWriter() {
        return SigarTestCase.out;
    }
    
    public long getInvalidPid() {
        return 666666L;
    }
    
    public void traceln(final String msg) {
        if (getVerbose()) {
            getWriter().println(msg);
        }
    }
    
    public void trace(final String msg) {
        if (getVerbose()) {
            getWriter().print(msg);
        }
    }
    
    public void assertTrueTrace(final String msg, final String value) {
        this.traceln(msg + "=" + value);
        assertTrue(msg, value != null);
    }
    
    public void assertLengthTrace(final String msg, final String value) {
        this.assertTrueTrace(msg, value);
        assertTrue(msg, value.length() > 0);
    }
    
    public void assertIndexOfTrace(final String msg, final String value, final String substr) {
        this.assertTrueTrace(msg, value);
        assertTrue(msg, value.indexOf(substr) != -1);
    }
    
    public void assertGtZeroTrace(final String msg, final long value) {
        this.traceln(msg + "=" + value);
        assertTrue(msg, value > 0L);
    }
    
    public void assertGtEqZeroTrace(final String msg, final long value) {
        this.traceln(msg + "=" + value);
        assertTrue(msg, value >= 0L);
    }
    
    public void assertValidFieldTrace(final String msg, final long value) {
        if (value != -1L) {
            this.assertGtEqZeroTrace(msg, value);
        }
    }
    
    public void assertEqualsTrace(final String msg, final long expected, final long actual) {
        this.traceln(msg + "=" + actual + "/" + expected);
        assertEquals(msg, expected, actual);
    }
    
    public void traceMethods(final Object obj) throws Exception {
        final Class cls = obj.getClass();
        final Method[] methods = cls.getDeclaredMethods();
        this.traceln("");
        for (int i = 0; i < methods.length; ++i) {
            final String name = methods[i].getName();
            if (name.startsWith("get")) {
                Object val = methods[i].invoke(obj, new Object[0]);
                if (val instanceof Long && (long)val == -1L) {
                    val = "NOTIMPL";
                }
                this.traceln(name + "=" + val);
            }
        }
    }
    
    static {
        SigarTestCase.sigar = null;
        SigarTestCase.verbose = "true".equals(System.getProperty("sigar.testVerbose"));
        JDK_14_COMPAT = (System.getProperty("java.specification.version").compareTo("1.4") >= 0);
        SigarTestCase.out = System.out;
    }
}
