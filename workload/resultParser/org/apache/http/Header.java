// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

public interface Header extends NameValuePair
{
    HeaderElement[] getElements() throws ParseException;
}
