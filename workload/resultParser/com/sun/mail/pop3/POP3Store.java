// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.mail.pop3;

import java.util.Collections;
import javax.mail.Folder;
import java.io.IOException;
import javax.mail.MessagingException;
import java.io.EOFException;
import javax.mail.AuthenticationFailedException;
import com.sun.mail.util.PropUtil;
import javax.mail.URLName;
import javax.mail.Session;
import java.lang.reflect.Constructor;
import java.util.Map;
import javax.mail.Store;

public class POP3Store extends Store
{
    private String name;
    private int defaultPort;
    private boolean isSSL;
    private Protocol port;
    private POP3Folder portOwner;
    private String host;
    private int portNum;
    private String user;
    private String passwd;
    private boolean useStartTLS;
    private boolean requireStartTLS;
    private Map capabilities;
    volatile Constructor messageConstructor;
    volatile boolean rsetBeforeQuit;
    volatile boolean disableTop;
    volatile boolean forgetTopHeaders;
    volatile boolean supportsUidl;
    
    public POP3Store(final Session session, final URLName url) {
        this(session, url, "pop3", false);
    }
    
    public POP3Store(final Session session, final URLName url, String name, boolean isSSL) {
        super(session, url);
        this.name = "pop3";
        this.defaultPort = 110;
        this.isSSL = false;
        this.port = null;
        this.portOwner = null;
        this.host = null;
        this.portNum = -1;
        this.user = null;
        this.passwd = null;
        this.useStartTLS = false;
        this.requireStartTLS = false;
        this.messageConstructor = null;
        this.rsetBeforeQuit = false;
        this.disableTop = false;
        this.forgetTopHeaders = false;
        this.supportsUidl = true;
        if (url != null) {
            name = url.getProtocol();
        }
        this.name = name;
        if (!isSSL) {
            isSSL = PropUtil.getBooleanSessionProperty(session, "mail." + name + ".ssl.enable", false);
        }
        if (isSSL) {
            this.defaultPort = 995;
        }
        else {
            this.defaultPort = 110;
        }
        this.isSSL = isSSL;
        this.rsetBeforeQuit = PropUtil.getBooleanSessionProperty(session, "mail." + name + ".rsetbeforequit", false);
        this.disableTop = PropUtil.getBooleanSessionProperty(session, "mail." + name + ".disabletop", false);
        this.forgetTopHeaders = PropUtil.getBooleanSessionProperty(session, "mail." + name + ".forgettopheaders", false);
        this.useStartTLS = PropUtil.getBooleanSessionProperty(session, "mail." + name + ".starttls.enable", false);
        this.requireStartTLS = PropUtil.getBooleanSessionProperty(session, "mail." + name + ".starttls.required", false);
        final String s = session.getProperty("mail." + name + ".message.class");
        if (s != null) {
            if (session.getDebug()) {
                session.getDebugOut().println("DEBUG POP3: message class: " + s);
            }
            try {
                final ClassLoader cl = this.getClass().getClassLoader();
                Class messageClass = null;
                try {
                    messageClass = Class.forName(s, false, cl);
                }
                catch (ClassNotFoundException ex2) {
                    messageClass = Class.forName(s);
                }
                final Class[] c = { Folder.class, Integer.TYPE };
                this.messageConstructor = messageClass.getConstructor((Class[])c);
            }
            catch (Exception ex) {
                if (session.getDebug()) {
                    session.getDebugOut().println("DEBUG POP3: failed to load message class: " + ex);
                }
            }
        }
    }
    
    protected synchronized boolean protocolConnect(final String host, int portNum, final String user, final String passwd) throws MessagingException {
        if (host == null || passwd == null || user == null) {
            return false;
        }
        if (portNum == -1) {
            portNum = PropUtil.getIntSessionProperty(this.session, "mail." + this.name + ".port", -1);
        }
        if (portNum == -1) {
            portNum = this.defaultPort;
        }
        this.host = host;
        this.portNum = portNum;
        this.user = user;
        this.passwd = passwd;
        try {
            this.port = this.getPort(null);
        }
        catch (EOFException eex) {
            throw new AuthenticationFailedException(eex.getMessage());
        }
        catch (IOException ioex) {
            throw new MessagingException("Connect failed", ioex);
        }
        return true;
    }
    
    public synchronized boolean isConnected() {
        if (!super.isConnected()) {
            return false;
        }
        try {
            if (this.port == null) {
                this.port = this.getPort(null);
            }
            else if (!this.port.noop()) {
                throw new IOException("NOOP failed");
            }
            return true;
        }
        catch (IOException ioex) {
            try {
                super.close();
            }
            catch (MessagingException mex) {}
            finally {
                return false;
            }
        }
    }
    
    synchronized Protocol getPort(final POP3Folder owner) throws IOException {
        if (this.port != null && this.portOwner == null) {
            this.portOwner = owner;
            return this.port;
        }
        final Protocol p = new Protocol(this.host, this.portNum, this.session.getDebug(), this.session.getDebugOut(), this.session.getProperties(), "mail." + this.name, this.isSSL);
        if (this.useStartTLS || this.requireStartTLS) {
            if (p.hasCapability("STLS")) {
                p.stls();
                p.setCapabilities(p.capa());
            }
            else if (this.requireStartTLS) {
                if (this.debug) {
                    this.session.getDebugOut().println("DEBUG POP3: STLS required but not supported");
                }
                try {
                    p.quit();
                }
                catch (IOException ioex) {}
                finally {
                    throw new EOFException("STLS required but not supported");
                }
            }
        }
        this.capabilities = p.getCapabilities();
        if (!this.disableTop && this.capabilities != null && this.capabilities.containsKey("TOP")) {
            this.disableTop = true;
            this.session.getDebugOut().println("DEBUG POP3: server doesn't support TOP, disabling it");
        }
        this.supportsUidl = (this.capabilities == null || this.capabilities.containsKey("UIDL"));
        String msg = null;
        if ((msg = p.login(this.user, this.passwd)) != null) {
            try {
                p.quit();
            }
            catch (IOException ioex2) {}
            finally {
                throw new EOFException(msg);
            }
        }
        if (this.port == null && owner != null) {
            this.port = p;
            this.portOwner = owner;
        }
        if (this.portOwner == null) {
            this.portOwner = owner;
        }
        return p;
    }
    
    synchronized void closePort(final POP3Folder owner) {
        if (this.portOwner == owner) {
            this.port = null;
            this.portOwner = null;
        }
    }
    
    public synchronized void close() throws MessagingException {
        try {
            if (this.port != null) {
                this.port.quit();
            }
        }
        catch (IOException ioex) {}
        finally {
            this.port = null;
            super.close();
        }
    }
    
    public Folder getDefaultFolder() throws MessagingException {
        this.checkConnected();
        return new DefaultFolder(this);
    }
    
    public Folder getFolder(final String name) throws MessagingException {
        this.checkConnected();
        return new POP3Folder(this, name);
    }
    
    public Folder getFolder(final URLName url) throws MessagingException {
        this.checkConnected();
        return new POP3Folder(this, url.getFile());
    }
    
    public Map capabilities() throws MessagingException {
        final Map c = this.capabilities;
        if (c != null) {
            return Collections.unmodifiableMap((Map<?, ?>)c);
        }
        return Collections.emptyMap();
    }
    
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.port != null) {
            this.close();
        }
    }
    
    private void checkConnected() throws MessagingException {
        if (!super.isConnected()) {
            throw new MessagingException("Not connected");
        }
    }
}
