// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.primitive;

import java.util.Locale;

public abstract class AbstractIntegerMorpher extends AbstractPrimitiveMorpher
{
    public AbstractIntegerMorpher() {
    }
    
    public AbstractIntegerMorpher(final boolean useDefault) {
        super(useDefault);
    }
    
    protected String getIntegerValue(final Object obj) {
        final Locale defaultLocale = Locale.getDefault();
        String str = null;
        try {
            Locale.setDefault(Locale.US);
            str = String.valueOf(obj);
        }
        finally {
            Locale.setDefault(defaultLocale);
        }
        Locale.setDefault(defaultLocale);
        final int index = str.indexOf(".");
        if (index != -1) {
            str = str.substring(0, index);
        }
        return str;
    }
}
