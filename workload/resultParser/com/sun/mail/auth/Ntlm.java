// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.mail.auth;

import java.lang.reflect.InvocationTargetException;
import java.io.InputStream;
import com.sun.mail.util.BASE64DecoderStream;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.io.IOException;
import java.io.OutputStream;
import com.sun.mail.util.BASE64EncoderStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Ntlm
{
    private PrintStream debugout;
    
    public Ntlm(final PrintStream debugout) {
        this.debugout = debugout;
    }
    
    public String generateType1Msg(final boolean useUnicode, final int flags, final String domain, final String workstation) throws IOException {
        try {
            final Class t1MClass = Class.forName("jcifs.ntlmssp.Type1Message");
            final Constructor t1Mconstructor = t1MClass.getConstructor(Integer.TYPE, String.class, String.class);
            final Object t1m = t1Mconstructor.newInstance(new Integer(flags), domain, workstation);
            if (this.debugout != null) {
                this.debugout.println("DEBUG NTLM: type 1 message: " + t1m);
            }
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            final BASE64EncoderStream out = new BASE64EncoderStream(bout, Integer.MAX_VALUE);
            if (this.debugout != null) {
                this.debugout.println("DEBUG NTLM: type 1 message length: " + ((byte[])t1MClass.getMethod("toByteArray", (Class[])new Class[0]).invoke(t1m, (Object[])null)).length);
            }
            out.write((byte[])t1MClass.getMethod("toByteArray", (Class[])new Class[0]).invoke(t1m, (Object[])null));
            out.flush();
            out.close();
            return new String(bout.toByteArray());
        }
        catch (IOException ioex) {
            if (this.debugout != null) {
                ioex.printStackTrace(this.debugout);
            }
            throw ioex;
        }
        catch (Exception ex) {
            if (this.debugout != null) {
                ex.printStackTrace(this.debugout);
            }
            return null;
        }
    }
    
    public String generateType3Msg(final String username, final String password, final String domain, final String workstation, final String challenge, final int flags, final int lmCompatibility) throws IOException {
        try {
            final Class t2MClass = Class.forName("jcifs.ntlmssp.Type2Message");
            final Constructor t2Mconstructor = t2MClass.getConstructor(Class.forName("[B"));
            Object t2m;
            try {
                final BASE64DecoderStream in = new BASE64DecoderStream(new ByteArrayInputStream(challenge.getBytes()));
                final byte[] bytes = new byte[in.available()];
                in.read(bytes);
                t2m = t2Mconstructor.newInstance(bytes);
            }
            catch (IOException ex) {
                final IOException ioex = new IOException("Invalid Type2 message");
                ioex.initCause(ex);
                throw ioex;
            }
            final Class t3MClass = Class.forName("jcifs.ntlmssp.Type3Message");
            final Constructor t3Mconstructor = t3MClass.getConstructor(t2MClass, String.class, String.class, String.class, String.class, Integer.TYPE);
            final Object t3m = t3Mconstructor.newInstance(t2m, password, (domain == null) ? "" : domain, username, workstation, new Integer(flags));
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            final BASE64EncoderStream out = new BASE64EncoderStream(bout, Integer.MAX_VALUE);
            out.write((byte[])t3MClass.getMethod("toByteArray", (Class[])new Class[0]).invoke(t3m, (Object[])null));
            out.flush();
            out.close();
            return new String(bout.toByteArray());
        }
        catch (InvocationTargetException itex) {
            if (this.debugout != null) {
                itex.printStackTrace(this.debugout);
            }
            final Throwable t = itex.getTargetException();
            if (t instanceof IOException) {
                throw (IOException)t;
            }
            final IOException ioex2 = new IOException("Ntlm.generateType3Msg failed; Exception: " + t);
            ioex2.initCause(itex);
            throw ioex2;
        }
        catch (IOException ioex3) {
            if (this.debugout != null) {
                ioex3.printStackTrace(this.debugout);
            }
            throw ioex3;
        }
        catch (Exception ex2) {
            if (this.debugout != null) {
                ex2.printStackTrace(this.debugout);
            }
            return null;
        }
    }
}
