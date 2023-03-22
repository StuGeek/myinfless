// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32.test;

import java.util.Iterator;
import java.util.Map;
import org.hyperic.sigar.win32.Pdh;
import org.hyperic.sigar.test.SigarTestCase;

public class TestPdh extends SigarTestCase
{
    public TestPdh(final String name) {
        super(name);
    }
    
    private static boolean isCounter(final long type) {
        return (type & 0x400L) == 0x400L;
    }
    
    private void getValue(final String key) throws Exception {
        final Pdh pdh = new Pdh();
        this.traceln(key + ": " + pdh.getDescription(key));
        this.traceln("counter=" + isCounter(pdh.getCounterType(key)));
        this.assertGtEqZeroTrace("raw", (long)pdh.getRawValue(key));
        this.assertGtEqZeroTrace("fmt", (long)pdh.getFormattedValue(key));
    }
    
    public void testGetValue() throws Exception {
        Pdh.enableTranslation();
        final String DL = "\\";
        final String[][] keys = { { "System", "System Up Time" }, { "Memory", "Available Bytes" }, { "Memory", "Pages/sec" }, { "Processor(_Total)", "% User Time" } };
        for (int i = 0; i < keys.length; ++i) {
            final String path = "\\" + keys[i][0] + "\\" + keys[i][1];
            final String trans = Pdh.translate(path);
            if (!trans.equals(path)) {
                this.traceln(path + "-->" + trans);
            }
            this.traceln(path + " validate: " + Pdh.validate(path));
            this.getValue(path);
        }
    }
    
    public void testCounterMap() throws Exception {
        final Map counters = Pdh.getEnglishPerflibCounterMap();
        this.assertGtZeroTrace("counters", counters.size());
        int dups = 0;
        for (final Map.Entry entry : counters.entrySet()) {
            final String name = entry.getKey();
            final int[] ix = entry.getValue();
            if (ix.length > 1) {
                ++dups;
            }
        }
        this.traceln(dups + " names have dups");
        final String[] keys = { "System", "System Up Time" };
        int last = -1;
        for (int i = 0; i < keys.length; ++i) {
            final String name2 = keys[i];
            final int[] ix2 = counters.get(name2.toLowerCase());
            assertFalse(ix2[0] == last);
            this.traceln(name2 + "=" + ix2[0]);
            last = ix2[0];
            final String lookupName = Pdh.getCounterName(ix2[0]);
            this.traceln(name2 + "=" + lookupName);
        }
    }
    
    public void testValidate() {
        final Object[][] tests = { { "\\Does Not\\Exist", new Integer(-1073738824), new Integer(-1073738816) }, { "Does Not Exist", new Integer(-1073738816) }, { "\\System\\DoesNotExist", new Integer(-1073738823) }, { "\\Processor(666)\\% User Time", new Integer(-2147481647) }, { "\\System\\Threads", new Integer(0), new Integer(-1073738816) } };
        for (int i = 0; i < tests.length; ++i) {
            final String path = (String)tests[i][0];
            int expect = (int)tests[i][1];
            final int status = Pdh.validate(path);
            boolean expectedResult = status == expect;
            if (!expectedResult && tests[i].length == 3) {
                expect = (int)tests[i][2];
                expectedResult = (status == expect);
            }
            if (!expectedResult) {
                this.traceln("[validate] " + path + "-->" + Integer.toHexString(status).toUpperCase() + " != " + Integer.toHexString(expect).toUpperCase());
            }
            assertTrue(expectedResult);
        }
    }
    
    public void testPdh() throws Exception {
        final String[] iface = Pdh.getKeys("Thread");
        assertTrue(iface.length > 0);
    }
}
