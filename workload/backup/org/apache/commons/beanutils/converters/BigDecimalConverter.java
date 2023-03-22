// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import java.math.BigDecimal;

public final class BigDecimalConverter extends NumberConverter
{
    public BigDecimalConverter() {
        super(true);
    }
    
    public BigDecimalConverter(final Object defaultValue) {
        super(true, defaultValue);
    }
    
    protected Class getDefaultType() {
        return BigDecimal.class;
    }
}
