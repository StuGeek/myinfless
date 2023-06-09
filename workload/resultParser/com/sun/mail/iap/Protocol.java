// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.mail.iap;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.SocketFetcher;
import java.util.Vector;
import java.io.DataOutputStream;
import com.sun.mail.util.TraceOutputStream;
import com.sun.mail.util.TraceInputStream;
import java.util.Properties;
import java.io.PrintStream;
import java.net.Socket;

public class Protocol
{
    protected String host;
    private Socket socket;
    protected boolean debug;
    protected boolean quote;
    protected PrintStream out;
    protected Properties props;
    protected String prefix;
    private boolean connected;
    private TraceInputStream traceInput;
    private volatile ResponseInputStream input;
    private TraceOutputStream traceOutput;
    private volatile DataOutputStream output;
    private int tagCounter;
    private String localHostName;
    private final Vector handlers;
    private volatile long timestamp;
    private static final byte[] CRLF;
    
    public Protocol(final String host, final int port, final boolean debug, final PrintStream out, final Properties props, final String prefix, final boolean isSSL) throws IOException, ProtocolException {
        this.connected = false;
        this.tagCounter = 0;
        this.handlers = new Vector();
        try {
            this.host = host;
            this.debug = debug;
            this.out = out;
            this.props = props;
            this.prefix = prefix;
            this.socket = SocketFetcher.getSocket(host, port, props, prefix, isSSL);
            this.quote = PropUtil.getBooleanProperty(props, "mail.debug.quote", false);
            this.initStreams(out);
            this.processGreeting(this.readResponse());
            this.timestamp = System.currentTimeMillis();
            this.connected = true;
        }
        finally {
            if (!this.connected) {
                this.disconnect();
            }
        }
    }
    
    private void initStreams(final PrintStream out) throws IOException {
        (this.traceInput = new TraceInputStream(this.socket.getInputStream(), out)).setTrace(this.debug);
        this.traceInput.setQuote(this.quote);
        this.input = new ResponseInputStream(this.traceInput);
        (this.traceOutput = new TraceOutputStream(this.socket.getOutputStream(), out)).setTrace(this.debug);
        this.traceOutput.setQuote(this.quote);
        this.output = new DataOutputStream(new BufferedOutputStream(this.traceOutput));
    }
    
    public Protocol(final InputStream in, final OutputStream out, final boolean debug) throws IOException {
        this.connected = false;
        this.tagCounter = 0;
        this.handlers = new Vector();
        this.host = "localhost";
        this.debug = debug;
        this.quote = false;
        this.out = System.out;
        (this.traceInput = new TraceInputStream(in, System.out)).setTrace(debug);
        this.traceInput.setQuote(this.quote);
        this.input = new ResponseInputStream(this.traceInput);
        (this.traceOutput = new TraceOutputStream(out, System.out)).setTrace(debug);
        this.traceOutput.setQuote(this.quote);
        this.output = new DataOutputStream(new BufferedOutputStream(this.traceOutput));
        this.timestamp = System.currentTimeMillis();
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public void addResponseHandler(final ResponseHandler h) {
        this.handlers.addElement(h);
    }
    
    public void removeResponseHandler(final ResponseHandler h) {
        this.handlers.removeElement(h);
    }
    
    public void notifyResponseHandlers(final Response[] responses) {
        if (this.handlers.size() == 0) {
            return;
        }
        for (int i = 0; i < responses.length; ++i) {
            final Response r = responses[i];
            if (r != null) {
                final Object[] h = this.handlers.toArray();
                for (int j = 0; j < h.length; ++j) {
                    if (h[j] != null) {
                        ((ResponseHandler)h[j]).handleResponse(r);
                    }
                }
            }
        }
    }
    
    protected void processGreeting(final Response r) throws ProtocolException {
        if (r.isBYE()) {
            throw new ConnectionException(this, r);
        }
    }
    
    protected ResponseInputStream getInputStream() {
        return this.input;
    }
    
    protected OutputStream getOutputStream() {
        return this.output;
    }
    
    protected synchronized boolean supportsNonSyncLiterals() {
        return false;
    }
    
    public Response readResponse() throws IOException, ProtocolException {
        return new Response(this);
    }
    
    protected ByteArray getResponseBuffer() {
        return null;
    }
    
    public String writeCommand(final String command, final Argument args) throws IOException, ProtocolException {
        final String tag = "A" + Integer.toString(this.tagCounter++, 10);
        this.output.writeBytes(tag + " " + command);
        if (args != null) {
            this.output.write(32);
            args.write(this);
        }
        this.output.write(Protocol.CRLF);
        this.output.flush();
        return tag;
    }
    
    public synchronized Response[] command(final String command, final Argument args) {
        this.commandStart(command);
        final Vector v = new Vector();
        boolean done = false;
        String tag = null;
        Response r = null;
        try {
            tag = this.writeCommand(command, args);
        }
        catch (LiteralException lex) {
            v.addElement(lex.getResponse());
            done = true;
        }
        catch (Exception ex) {
            v.addElement(Response.byeResponse(ex));
            done = true;
        }
        while (!done) {
            try {
                r = this.readResponse();
            }
            catch (IOException ioex) {
                r = Response.byeResponse(ioex);
            }
            catch (ProtocolException pex) {
                continue;
            }
            v.addElement(r);
            if (r.isBYE()) {
                done = true;
            }
            if (r.isTagged() && r.getTag().equals(tag)) {
                done = true;
            }
        }
        final Response[] responses = new Response[v.size()];
        v.copyInto(responses);
        this.timestamp = System.currentTimeMillis();
        this.commandEnd();
        return responses;
    }
    
    public void handleResult(final Response response) throws ProtocolException {
        if (response.isOK()) {
            return;
        }
        if (response.isNO()) {
            throw new CommandFailedException(response);
        }
        if (response.isBAD()) {
            throw new BadCommandException(response);
        }
        if (response.isBYE()) {
            this.disconnect();
            throw new ConnectionException(this, response);
        }
    }
    
    public void simpleCommand(final String cmd, final Argument args) throws ProtocolException {
        final Response[] r = this.command(cmd, args);
        this.notifyResponseHandlers(r);
        this.handleResult(r[r.length - 1]);
    }
    
    public synchronized void startTLS(final String cmd) throws IOException, ProtocolException {
        this.simpleCommand(cmd, null);
        this.socket = SocketFetcher.startTLS(this.socket, this.host, this.props, this.prefix);
        this.initStreams(this.out);
    }
    
    protected synchronized void disconnect() {
        if (this.socket != null) {
            try {
                this.socket.close();
            }
            catch (IOException ex) {}
            this.socket = null;
        }
    }
    
    protected synchronized String getLocalHost() {
        if (this.localHostName == null || this.localHostName.length() <= 0) {
            this.localHostName = this.props.getProperty(this.prefix + ".localhost");
        }
        if (this.localHostName == null || this.localHostName.length() <= 0) {
            this.localHostName = this.props.getProperty(this.prefix + ".localaddress");
        }
        try {
            if (this.localHostName == null || this.localHostName.length() <= 0) {
                final InetAddress localHost = InetAddress.getLocalHost();
                this.localHostName = localHost.getCanonicalHostName();
                if (this.localHostName == null) {
                    this.localHostName = "[" + localHost.getHostAddress() + "]";
                }
            }
        }
        catch (UnknownHostException ex) {}
        if ((this.localHostName == null || this.localHostName.length() <= 0) && this.socket != null && this.socket.isBound()) {
            final InetAddress localHost = this.socket.getLocalAddress();
            this.localHostName = localHost.getCanonicalHostName();
            if (this.localHostName == null) {
                this.localHostName = "[" + localHost.getHostAddress() + "]";
            }
        }
        return this.localHostName;
    }
    
    protected void finalize() throws Throwable {
        super.finalize();
        this.disconnect();
    }
    
    private void commandStart(final String command) {
    }
    
    private void commandEnd() {
    }
    
    static {
        CRLF = new byte[] { 13, 10 };
    }
}
