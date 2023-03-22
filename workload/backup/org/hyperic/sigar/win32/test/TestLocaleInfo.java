// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32.test;

import org.hyperic.sigar.win32.LocaleInfo;
import org.hyperic.sigar.test.SigarTestCase;

public class TestLocaleInfo extends SigarTestCase
{
    public TestLocaleInfo(final String name) {
        super(name);
    }
    
    private void checkInfo(final LocaleInfo info, final String match) throws Exception {
        this.assertGtZeroTrace("id", info.getId());
        this.assertGtZeroTrace("primary lang", info.getPrimaryLangId());
        this.assertGtEqZeroTrace("sub lang", info.getSubLangId());
        this.assertLengthTrace("perflib id", info.getPerflibLangId());
        this.assertIndexOfTrace("lang", info.toString(), match);
    }
    
    public void testInfo() throws Exception {
        final Object[][] tests = { { new Integer(22), "Portuguese" }, { new Integer(LocaleInfo.makeLangId(9, 5)), "New Zealand" }, { new Integer(7), "German" }, { new Integer(LocaleInfo.makeLangId(10, 20)), "Puerto Rico" } };
        for (int i = 0; i < tests.length; ++i) {
            final Integer id = (Integer)tests[i][0];
            final String lang = (String)tests[i][1];
            final LocaleInfo info = new LocaleInfo(id);
            this.checkInfo(info, lang);
        }
        this.checkInfo(new LocaleInfo(), "");
    }
}
