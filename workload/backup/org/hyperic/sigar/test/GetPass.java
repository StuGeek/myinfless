// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import java.io.IOException;
import org.hyperic.sigar.Sigar;

public class GetPass
{
    public static void main(final String[] args) throws Exception {
        try {
            final String password = Sigar.getPassword("Enter password: ");
            System.out.println("You entered: ->" + password + "<-");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
