// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.mail.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Collection;
import java.util.regex.Pattern;
import java.security.cert.CertificateParsingException;
import java.util.List;
import java.security.cert.Certificate;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.lang.reflect.Method;
import javax.net.ssl.SSLSocket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import javax.net.ssl.SSLSocketFactory;
import java.security.GeneralSecurityException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import javax.net.SocketFactory;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

public class SocketFetcher
{
    private static boolean debug;
    
    private SocketFetcher() {
    }
    
    public static Socket getSocket(final String host, final int port, Properties props, String prefix, final boolean useSSL) throws IOException {
        if (SocketFetcher.debug) {
            System.out.println("DEBUG SocketFetcher: getSocket, host " + host + ", port " + port + ", prefix " + prefix + ", useSSL " + useSSL);
        }
        if (prefix == null) {
            prefix = "socket";
        }
        if (props == null) {
            props = new Properties();
        }
        final int cto = PropUtil.getIntProperty(props, prefix + ".connectiontimeout", -1);
        Socket socket = null;
        final String localaddrstr = props.getProperty(prefix + ".localaddress", null);
        InetAddress localaddr = null;
        if (localaddrstr != null) {
            localaddr = InetAddress.getByName(localaddrstr);
        }
        final int localport = PropUtil.getIntProperty(props, prefix + ".localport", 0);
        final boolean fb = PropUtil.getBooleanProperty(props, prefix + ".socketFactory.fallback", true);
        final boolean idCheck = PropUtil.getBooleanProperty(props, prefix + ".ssl.checkserveridentity", false);
        int sfPort = -1;
        String sfErr = "unknown socket factory";
        try {
            SocketFactory sf = null;
            String sfPortName = null;
            if (useSSL) {
                final Object sfo = props.get(prefix + ".ssl.socketFactory");
                if (sfo instanceof SocketFactory) {
                    sf = (SocketFactory)sfo;
                    sfErr = "SSL socket factory instance " + sf;
                }
                if (sf == null) {
                    final String sfClass = props.getProperty(prefix + ".ssl.socketFactory.class");
                    sf = getSocketFactory(sfClass);
                    sfErr = "SSL socket factory class " + sfClass;
                }
                sfPortName = ".ssl.socketFactory.port";
            }
            if (sf == null) {
                final Object sfo = props.get(prefix + ".socketFactory");
                if (sfo instanceof SocketFactory) {
                    sf = (SocketFactory)sfo;
                    sfErr = "socket factory instance " + sf;
                }
                if (sf == null) {
                    final String sfClass = props.getProperty(prefix + ".socketFactory.class");
                    sf = getSocketFactory(sfClass);
                    sfErr = "socket factory class " + sfClass;
                }
                sfPortName = ".socketFactory.port";
            }
            if (sf != null) {
                sfPort = PropUtil.getIntProperty(props, prefix + sfPortName, -1);
                if (sfPort == -1) {
                    sfPort = port;
                }
                socket = createSocket(localaddr, localport, host, sfPort, cto, props, prefix, sf, useSSL, idCheck);
            }
        }
        catch (SocketTimeoutException sex) {
            throw sex;
        }
        catch (Exception ex) {
            if (!fb) {
                if (ex instanceof InvocationTargetException) {
                    final Throwable t = ((InvocationTargetException)ex).getTargetException();
                    if (t instanceof Exception) {
                        ex = (Exception)t;
                    }
                }
                if (ex instanceof IOException) {
                    throw (IOException)ex;
                }
                final IOException ioex = new IOException("Couldn't connect using " + sfErr + " to host, port: " + host + ", " + sfPort + "; Exception: " + ex);
                ioex.initCause(ex);
                throw ioex;
            }
        }
        if (socket == null) {
            socket = createSocket(localaddr, localport, host, port, cto, props, prefix, null, useSSL, idCheck);
        }
        final int to = PropUtil.getIntProperty(props, prefix + ".timeout", -1);
        if (to >= 0) {
            socket.setSoTimeout(to);
        }
        configureSSLSocket(socket, props, prefix);
        return socket;
    }
    
    public static Socket getSocket(final String host, final int port, final Properties props, final String prefix) throws IOException {
        return getSocket(host, port, props, prefix, false);
    }
    
    private static Socket createSocket(final InetAddress localaddr, final int localport, final String host, final int port, final int cto, final Properties props, final String prefix, SocketFactory sf, final boolean useSSL, final boolean idCheck) throws IOException {
        Socket socket;
        if (sf != null) {
            socket = sf.createSocket();
        }
        else if (useSSL) {
            Label_0127: {
                final String trusted;
                if ((trusted = props.getProperty(prefix + ".ssl.trust")) != null) {
                    try {
                        final MailSSLSocketFactory msf = new MailSSLSocketFactory();
                        if (trusted.equals("*")) {
                            msf.setTrustAllHosts(true);
                        }
                        else {
                            msf.setTrustedHosts(trusted.split("\\s+"));
                        }
                        sf = msf;
                        break Label_0127;
                    }
                    catch (GeneralSecurityException gex) {
                        final IOException ioex = new IOException("Can't create MailSSLSocketFactory");
                        ioex.initCause(gex);
                        throw ioex;
                    }
                }
                sf = SSLSocketFactory.getDefault();
            }
            socket = sf.createSocket();
        }
        else {
            socket = new Socket();
        }
        if (localaddr != null) {
            socket.bind(new InetSocketAddress(localaddr, localport));
        }
        if (cto >= 0) {
            socket.connect(new InetSocketAddress(host, port), cto);
        }
        else {
            socket.connect(new InetSocketAddress(host, port));
        }
        if (idCheck && socket instanceof SSLSocket) {
            checkServerIdentity(host, (SSLSocket)socket);
        }
        if (sf instanceof MailSSLSocketFactory) {
            final MailSSLSocketFactory msf2 = (MailSSLSocketFactory)sf;
            if (!msf2.isServerTrusted(host, (SSLSocket)socket)) {
                try {
                    socket.close();
                }
                finally {
                    throw new IOException("Server is not trusted: " + host);
                }
            }
        }
        return socket;
    }
    
    private static SocketFactory getSocketFactory(final String sfClass) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (sfClass == null || sfClass.length() == 0) {
            return null;
        }
        final ClassLoader cl = getContextClassLoader();
        Class clsSockFact = null;
        if (cl != null) {
            try {
                clsSockFact = Class.forName(sfClass, false, cl);
            }
            catch (ClassNotFoundException ex) {}
        }
        if (clsSockFact == null) {
            clsSockFact = Class.forName(sfClass);
        }
        final Method mthGetDefault = clsSockFact.getMethod("getDefault", (Class[])new Class[0]);
        final SocketFactory sf = (SocketFactory)mthGetDefault.invoke(new Object(), new Object[0]);
        return sf;
    }
    
    public static Socket startTLS(final Socket socket) throws IOException {
        return startTLS(socket, new Properties(), "socket");
    }
    
    public static Socket startTLS(final Socket socket, final Properties props, final String prefix) throws IOException {
        final InetAddress a = socket.getInetAddress();
        final String host = a.getHostName();
        return startTLS(socket, host, props, prefix);
    }
    
    public static Socket startTLS(Socket socket, final String host, final Properties props, final String prefix) throws IOException {
        final int port = socket.getPort();
        if (SocketFetcher.debug) {
            System.out.println("DEBUG SocketFetcher: startTLS host " + host + ", port " + port);
        }
        String sfErr = "unknown socket factory";
        try {
            SSLSocketFactory ssf = null;
            SocketFactory sf = null;
            Object sfo = props.get(prefix + ".ssl.socketFactory");
            if (sfo instanceof SocketFactory) {
                sf = (SocketFactory)sfo;
                sfErr = "SSL socket factory instance " + sf;
            }
            if (sf == null) {
                final String sfClass = props.getProperty(prefix + ".ssl.socketFactory.class");
                sf = getSocketFactory(sfClass);
                sfErr = "SSL socket factory class " + sfClass;
            }
            if (sf != null && sf instanceof SSLSocketFactory) {
                ssf = (SSLSocketFactory)sf;
            }
            if (ssf == null) {
                sfo = props.get(prefix + ".socketFactory");
                if (sfo instanceof SocketFactory) {
                    sf = (SocketFactory)sfo;
                    sfErr = "socket factory instance " + sf;
                }
                if (sf == null) {
                    final String sfClass = props.getProperty(prefix + ".socketFactory.class");
                    sf = getSocketFactory(sfClass);
                    sfErr = "socket factory class " + sfClass;
                }
                if (sf != null && sf instanceof SSLSocketFactory) {
                    ssf = (SSLSocketFactory)sf;
                }
            }
            Label_0465: {
                if (ssf == null) {
                    final String trusted;
                    if ((trusted = props.getProperty(prefix + ".ssl.trust")) != null) {
                        try {
                            final MailSSLSocketFactory msf = new MailSSLSocketFactory();
                            if (trusted.equals("*")) {
                                msf.setTrustAllHosts(true);
                            }
                            else {
                                msf.setTrustedHosts(trusted.split("\\s+"));
                            }
                            ssf = msf;
                            sfErr = "mail SSL socket factory";
                            break Label_0465;
                        }
                        catch (GeneralSecurityException gex) {
                            final IOException ioex = new IOException("Can't create MailSSLSocketFactory");
                            ioex.initCause(gex);
                            throw ioex;
                        }
                    }
                    ssf = (SSLSocketFactory)SSLSocketFactory.getDefault();
                    sfErr = "default SSL socket factory";
                }
            }
            socket = ssf.createSocket(socket, host, port, true);
            final boolean idCheck = PropUtil.getBooleanProperty(props, prefix + ".ssl.checkserveridentity", false);
            if (idCheck) {
                checkServerIdentity(host, (SSLSocket)socket);
            }
            if (ssf instanceof MailSSLSocketFactory) {
                final MailSSLSocketFactory msf = (MailSSLSocketFactory)ssf;
                if (!msf.isServerTrusted(host, (SSLSocket)socket)) {
                    try {
                        socket.close();
                    }
                    finally {
                        throw new IOException("Server is not trusted: " + host);
                    }
                }
            }
            configureSSLSocket(socket, props, prefix);
        }
        catch (Exception ex) {
            if (ex instanceof InvocationTargetException) {
                final Throwable t = ((InvocationTargetException)ex).getTargetException();
                if (t instanceof Exception) {
                    ex = (Exception)t;
                }
            }
            if (ex instanceof IOException) {
                throw (IOException)ex;
            }
            final IOException ioex2 = new IOException("Exception in startTLS using " + sfErr + ": host, port: " + host + ", " + port + "; Exception: " + ex);
            ioex2.initCause(ex);
            throw ioex2;
        }
        return socket;
    }
    
    private static void configureSSLSocket(final Socket socket, final Properties props, final String prefix) throws IOException {
        if (!(socket instanceof SSLSocket)) {
            return;
        }
        final SSLSocket sslsocket = (SSLSocket)socket;
        final String protocols = props.getProperty(prefix + ".ssl.protocols", null);
        if (protocols != null) {
            sslsocket.setEnabledProtocols(stringArray(protocols));
        }
        else {
            sslsocket.setEnabledProtocols(new String[] { "TLSv1" });
        }
        final String ciphers = props.getProperty(prefix + ".ssl.ciphersuites", null);
        if (ciphers != null) {
            sslsocket.setEnabledCipherSuites(stringArray(ciphers));
        }
        if (SocketFetcher.debug) {
            System.out.println("DEBUG SocketFetcher: SSL protocols after " + Arrays.asList(sslsocket.getEnabledProtocols()));
            System.out.println("DEBUG SocketFetcher: SSL ciphers after " + Arrays.asList(sslsocket.getEnabledCipherSuites()));
        }
        sslsocket.startHandshake();
    }
    
    private static void checkServerIdentity(final String server, final SSLSocket sslSocket) throws IOException {
        try {
            final Certificate[] certChain = sslSocket.getSession().getPeerCertificates();
            if (certChain != null && certChain.length > 0 && certChain[0] instanceof X509Certificate && matchCert(server, (X509Certificate)certChain[0])) {
                return;
            }
        }
        catch (SSLPeerUnverifiedException e) {
            sslSocket.close();
            final IOException ioex = new IOException("Can't verify identity of server: " + server);
            ioex.initCause(e);
            throw ioex;
        }
        sslSocket.close();
        throw new IOException("Can't verify identity of server: " + server);
    }
    
    private static boolean matchCert(final String server, final X509Certificate cert) {
        if (SocketFetcher.debug) {
            System.out.println("DEBUG SocketFetcher: matchCert server " + server + ", cert " + cert);
        }
        try {
            final Class hnc = Class.forName("sun.security.util.HostnameChecker");
            final Method getInstance = hnc.getMethod("getInstance", Byte.TYPE);
            final Object hostnameChecker = getInstance.invoke(new Object(), new Byte((byte)2));
            if (SocketFetcher.debug) {
                System.out.println("DEBUG SocketFetcher: using sun.security.util.HostnameChecker");
            }
            final Method match = hnc.getMethod("match", String.class, X509Certificate.class);
            try {
                match.invoke(hostnameChecker, server, cert);
                return true;
            }
            catch (InvocationTargetException cex) {
                if (SocketFetcher.debug) {
                    System.out.println("DEBUG SocketFetcher: FAIL: " + cex);
                }
                return false;
            }
        }
        catch (Exception ex) {
            if (SocketFetcher.debug) {
                System.out.println("DEBUG SocketFetcher: NO sun.security.util.HostnameChecker: " + ex);
            }
            try {
                final Collection names = cert.getSubjectAlternativeNames();
                if (names != null) {
                    boolean foundName = false;
                    for (final List nameEnt : names) {
                        final Integer type = nameEnt.get(0);
                        if (type == 2) {
                            foundName = true;
                            final String name = nameEnt.get(1);
                            if (SocketFetcher.debug) {
                                System.out.println("DEBUG SocketFetcher: found name: " + name);
                            }
                            if (matchServer(server, name)) {
                                return true;
                            }
                            continue;
                        }
                    }
                    if (foundName) {
                        return false;
                    }
                }
            }
            catch (CertificateParsingException ex2) {}
            final Pattern p = Pattern.compile("CN=([^,]*)");
            final Matcher m = p.matcher(cert.getSubjectX500Principal().getName());
            return m.find() && matchServer(server, m.group(1).trim());
        }
    }
    
    private static boolean matchServer(final String server, final String name) {
        if (SocketFetcher.debug) {
            System.out.println("DEBUG SocketFetcher: match server " + server + " with " + name);
        }
        if (!name.startsWith("*.")) {
            return server.equalsIgnoreCase(name);
        }
        final String tail = name.substring(2);
        if (tail.length() == 0) {
            return false;
        }
        final int off = server.length() - tail.length();
        return off >= 1 && server.charAt(off - 1) == '.' && server.regionMatches(true, off, tail, 0, tail.length());
    }
    
    private static String[] stringArray(final String s) {
        final StringTokenizer st = new StringTokenizer(s);
        final List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return tokens.toArray(new String[tokens.size()]);
    }
    
    private static ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction() {
            public Object run() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                }
                catch (SecurityException ex) {}
                return cl;
            }
        });
    }
    
    static {
        SocketFetcher.debug = PropUtil.getBooleanSystemProperty("mail.socket.debug", false);
    }
}
