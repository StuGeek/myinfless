// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.mail.util.logging;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import javax.mail.internet.AddressException;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.ArrayList;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;
import javax.mail.Multipart;
import java.util.Iterator;
import java.util.Date;
import javax.mail.internet.MimeMultipart;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Transport;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;
import java.io.UnsupportedEncodingException;
import java.util.logging.ErrorManager;
import java.lang.reflect.Array;
import javax.mail.MessagingException;
import javax.activation.DataSource;
import java.io.IOException;
import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.Part;
import java.util.Locale;
import java.util.logging.LogRecord;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.Formatter;
import java.util.Comparator;
import java.util.Collection;
import javax.mail.Authenticator;
import java.util.Properties;
import java.util.logging.Handler;

public class MailHandler extends Handler
{
    private static final int offValue;
    private volatile boolean sealed;
    private boolean isWriting;
    private Properties mailProps;
    private Authenticator auth;
    private Collection data;
    private int capacity;
    private Comparator comparator;
    private Formatter subjectFormatter;
    private Level pushLevel;
    private Filter pushFilter;
    private Filter[] attachmentFilters;
    private Formatter[] attachmentFormatters;
    private Formatter[] attachmentNames;
    
    public MailHandler() {
        this.init();
        this.sealed = true;
    }
    
    public MailHandler(final int capacity) {
        this.init();
        this.sealed = true;
        this.setCapacity(capacity);
    }
    
    public MailHandler(final Properties props) {
        this.init();
        this.sealed = true;
        this.setMailProperties(props);
    }
    
    public boolean isLoggable(final LogRecord record) {
        final int levelValue = this.getLevel().intValue();
        if (record.getLevel().intValue() < levelValue || levelValue == MailHandler.offValue) {
            return false;
        }
        final Filter filter = this.getFilter();
        return filter == null || filter.isLoggable(record) || this.isAttachmentLoggable(record);
    }
    
    public void publish(final LogRecord record) {
        if (this.isLoggable(record)) {
            record.getSourceMethodName();
            synchronized (this) {
                this.data.add(record);
                final boolean priority = this.isPushable(record);
                if (priority || this.data.size() >= this.capacity) {
                    this.push(1, priority);
                }
            }
        }
    }
    
    public void push() {
        this.push(2, true);
    }
    
    public void flush() {
        this.push(2, false);
    }
    
    public synchronized void close() {
        super.setLevel(Level.OFF);
        this.push(3, false);
        if (this.capacity > 0) {
            this.capacity = -this.capacity;
            if (this.data.isEmpty()) {
                this.data = this.newData(1);
            }
        }
        assert this.capacity < 0;
    }
    
    public synchronized void setLevel(final Level newLevel) {
        if (this.capacity > 0) {
            super.setLevel(newLevel);
        }
        else {
            if (newLevel == null) {
                throw new NullPointerException();
            }
            this.checkAccess();
        }
    }
    
    public final synchronized Level getPushLevel() {
        return this.pushLevel;
    }
    
    public final synchronized void setPushLevel(final Level level) {
        this.checkAccess();
        if (level == null) {
            throw new NullPointerException();
        }
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.pushLevel = level;
    }
    
    public final synchronized Filter getPushFilter() {
        return this.pushFilter;
    }
    
    public final synchronized void setPushFilter(final Filter filter) {
        this.checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.pushFilter = filter;
    }
    
    public final synchronized Comparator getComparator() {
        return this.comparator;
    }
    
    public final synchronized void setComparator(final Comparator c) {
        this.checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.comparator = c;
    }
    
    public final synchronized int getCapacity() {
        assert this.capacity != Integer.MIN_VALUE;
        return Math.abs(this.capacity);
    }
    
    public final synchronized Authenticator getAuthenticator() {
        this.checkAccess();
        return this.auth;
    }
    
    public final synchronized void setAuthenticator(final Authenticator auth) {
        this.checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.auth = auth;
    }
    
    public final void setMailProperties(Properties props) {
        this.checkAccess();
        props = (Properties)props.clone();
        synchronized (this) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.mailProps = props;
        }
    }
    
    public final Properties getMailProperties() {
        this.checkAccess();
        final Properties props;
        synchronized (this) {
            props = this.mailProps;
        }
        return (Properties)props.clone();
    }
    
    public final Filter[] getAttachmentFilters() {
        return this.readOnlyAttachmentFilters().clone();
    }
    
    public final void setAttachmentFilters(Filter[] filters) {
        this.checkAccess();
        filters = filters.clone();
        synchronized (this) {
            if (this.attachmentFormatters.length != filters.length) {
                throw attachmentMismatch(this.attachmentFormatters.length, filters.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentFilters = filters;
        }
    }
    
    public final Formatter[] getAttachmentFormatters() {
        final Formatter[] formatters;
        synchronized (this) {
            formatters = this.attachmentFormatters;
        }
        return formatters.clone();
    }
    
    public final void setAttachmentFormatters(Formatter[] formatters) {
        this.checkAccess();
        formatters = formatters.clone();
        for (int i = 0; i < formatters.length; ++i) {
            if (formatters[i] == null) {
                throw new NullPointerException(atIndexMsg(i));
            }
        }
        synchronized (this) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentFormatters = formatters;
            this.fixUpAttachmentFilters();
            this.fixUpAttachmentNames();
        }
    }
    
    public final Formatter[] getAttachmentNames() {
        final Formatter[] formatters;
        synchronized (this) {
            formatters = this.attachmentNames;
        }
        return formatters.clone();
    }
    
    public final void setAttachmentNames(final String[] names) {
        this.checkAccess();
        final Formatter[] formatters = new Formatter[names.length];
        for (int i = 0; i < names.length; ++i) {
            final String name = names[i];
            if (name == null) {
                throw new NullPointerException(atIndexMsg(i));
            }
            if (name.length() <= 0) {
                throw new IllegalArgumentException(atIndexMsg(i));
            }
            formatters[i] = new TailNameFormatter(name);
        }
        synchronized (this) {
            if (this.attachmentFormatters.length != names.length) {
                throw attachmentMismatch(this.attachmentFormatters.length, names.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentNames = formatters;
        }
    }
    
    public final void setAttachmentNames(Formatter[] formatters) {
        this.checkAccess();
        formatters = formatters.clone();
        for (int i = 0; i < formatters.length; ++i) {
            if (formatters[i] == null) {
                throw new NullPointerException(atIndexMsg(i));
            }
        }
        synchronized (this) {
            if (this.attachmentFormatters.length != formatters.length) {
                throw attachmentMismatch(this.attachmentFormatters.length, formatters.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentNames = formatters;
        }
    }
    
    public final synchronized Formatter getSubject() {
        return this.subjectFormatter;
    }
    
    public final void setSubject(final String subject) {
        if (subject != null) {
            this.setSubject(new TailNameFormatter(subject));
            return;
        }
        throw new NullPointerException();
    }
    
    public final synchronized void setSubject(final Formatter format) {
        this.checkAccess();
        if (format == null) {
            throw new NullPointerException();
        }
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.subjectFormatter = format;
    }
    
    protected void reportError(final String msg, final Exception ex, final int code) {
        if (msg != null) {
            super.reportError(Level.SEVERE.getName() + ": " + msg, ex, code);
        }
        else {
            super.reportError(null, ex, code);
        }
    }
    
    final void checkAccess() {
        if (this.sealed) {
            LogManagerProperties.manager.checkAccess();
        }
    }
    
    private String contentTypeOf(final String head) {
        if (head != null) {
            final Locale locale = Locale.ENGLISH;
            if (head.trim().toUpperCase(locale).indexOf("<HTML") > -1) {
                return "text/html";
            }
            if (head.trim().toUpperCase(locale).indexOf("<XML") > -1) {
                return "text/xml";
            }
        }
        return null;
    }
    
    private void setContent(final Part part, final StringBuffer buf, final String type) throws MessagingException {
        if (type != null && !"text/plain".equals(type)) {
            try {
                final DataSource source = (DataSource)new ByteArrayDataSource(buf.toString(), type);
                part.setDataHandler(new DataHandler(source));
            }
            catch (IOException IOE) {
                this.reportError(IOE.getMessage(), IOE, 5);
                part.setText(buf.toString());
            }
        }
        else {
            part.setText(buf.toString());
        }
    }
    
    private final synchronized void setCapacity(final int newCapacity) {
        if (newCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        if (this.capacity < 0) {
            this.capacity = -newCapacity;
        }
        else {
            this.capacity = newCapacity;
        }
    }
    
    private synchronized Filter[] readOnlyAttachmentFilters() {
        return this.attachmentFilters;
    }
    
    private boolean fixUpAttachmentNames() {
        assert Thread.holdsLock(this);
        final int expect = this.attachmentFormatters.length;
        final int current = this.attachmentNames.length;
        if (current != expect) {
            this.attachmentNames = (Formatter[])copyOf(this.attachmentNames, expect);
            for (int i = 0; i < expect; ++i) {
                if (this.attachmentNames[i] == null) {
                    this.attachmentNames[i] = new TailNameFormatter(String.valueOf(this.attachmentFormatters[i]));
                }
            }
            return current != 0;
        }
        return false;
    }
    
    private boolean fixUpAttachmentFilters() {
        assert Thread.holdsLock(this);
        final int expect = this.attachmentFormatters.length;
        final int current = this.attachmentFilters.length;
        if (current != expect) {
            this.attachmentFilters = (Filter[])copyOf(this.attachmentFilters, expect);
            return current != 0;
        }
        return false;
    }
    
    private static Object[] copyOf(final Object[] a, final int size) {
        final Object[] copy = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
        System.arraycopy(a, 0, copy, 0, Math.min(a.length, size));
        return copy;
    }
    
    private synchronized void init() {
        final LogManager manager = LogManagerProperties.manager;
        final String p = this.getClass().getName();
        this.mailProps = new Properties();
        final ErrorManager em = (ErrorManager)this.initObject(p.concat(".errorManager"), ErrorManager.class);
        if (em != null) {
            this.setErrorManager(em);
        }
        try {
            final String val = manager.getProperty(p.concat(".level"));
            if (val != null) {
                super.setLevel(Level.parse(val));
            }
            else {
                super.setLevel(Level.WARNING);
            }
        }
        catch (SecurityException SE) {
            throw SE;
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), RE, 4);
            try {
                super.setLevel(Level.WARNING);
            }
            catch (RuntimeException fail) {
                this.reportError(fail.getMessage(), fail, 4);
            }
        }
        try {
            super.setFilter((Filter)this.initObject(p.concat(".filter"), Filter.class));
        }
        catch (SecurityException SE) {
            throw SE;
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), RE, 4);
        }
        final int DEFAULT_CAPACITY = 1000;
        try {
            final String value = manager.getProperty(p.concat(".capacity"));
            if (value != null) {
                this.setCapacity(Integer.parseInt(value));
            }
            else {
                this.setCapacity(1000);
            }
        }
        catch (RuntimeException RE2) {
            this.reportError(RE2.getMessage(), RE2, 4);
        }
        if (this.capacity == 0) {
            this.capacity = 1000;
        }
        this.data = this.newData(10);
        this.auth = (Authenticator)this.initObject(p.concat(".authenticator"), Authenticator.class);
        try {
            super.setEncoding(manager.getProperty(p.concat(".encoding")));
        }
        catch (SecurityException SE2) {
            throw SE2;
        }
        catch (UnsupportedEncodingException UEE) {
            this.reportError(UEE.getMessage(), UEE, 4);
        }
        catch (RuntimeException RE2) {
            this.reportError(RE2.getMessage(), RE2, 4);
        }
        try {
            final Formatter formatter = (Formatter)this.initObject(p.concat(".formatter"), Formatter.class);
            if (formatter != null) {
                super.setFormatter(formatter);
            }
            else {
                super.setFormatter(new SimpleFormatter());
            }
        }
        catch (SecurityException SE2) {
            throw SE2;
        }
        catch (RuntimeException RE2) {
            this.reportError(RE2.getMessage(), RE2, 4);
            try {
                super.setFormatter(new SimpleFormatter());
            }
            catch (RuntimeException fail2) {
                this.reportError(fail2.getMessage(), fail2, 4);
            }
        }
        try {
            this.comparator = this.initComparator(p.concat(".comparator"));
        }
        catch (Exception RE3) {
            this.reportError(RE3.getMessage(), RE3, 4);
        }
        try {
            final String val2 = manager.getProperty(p.concat(".pushLevel"));
            if (val2 != null) {
                this.pushLevel = Level.parse(val2);
            }
        }
        catch (RuntimeException RE2) {
            this.reportError(RE2.getMessage(), RE2, 4);
        }
        if (this.pushLevel == null) {
            this.pushLevel = Level.OFF;
        }
        this.pushFilter = (Filter)this.initObject(p.concat(".pushFilter"), Filter.class);
        this.subjectFormatter = (Formatter)this.initObject(p.concat(".subject"), Formatter.class);
        if (this.subjectFormatter == null) {
            this.subjectFormatter = new TailNameFormatter("");
        }
        this.attachmentFormatters = (Formatter[])this.initArray(p.concat(".attachment.formatters"), Formatter.class);
        this.attachmentFilters = (Filter[])this.initArray(p.concat(".attachment.filters"), Filter.class);
        this.attachmentNames = (Formatter[])this.initArray(p.concat(".attachment.names"), Formatter.class);
        for (int attachments = this.attachmentFormatters.length, i = 0; i < attachments; ++i) {
            if (this.attachmentFormatters[i] == null) {
                final NullPointerException NPE = new NullPointerException(atIndexMsg(i));
                this.attachmentFormatters[i] = new SimpleFormatter();
                this.reportError("attachment formatter.", NPE, 4);
            }
            else if (this.attachmentFormatters[i] instanceof TailNameFormatter) {
                final ClassNotFoundException CNFE = new ClassNotFoundException(this.attachmentFormatters[i].toString());
                this.attachmentFormatters[i] = new SimpleFormatter();
                this.reportError("attachment formatter.", CNFE, 4);
            }
        }
        if (this.fixUpAttachmentFilters()) {
            this.reportError("attachment filters.", attachmentMismatch("length mismatch"), 4);
        }
        if (this.fixUpAttachmentNames()) {
            this.reportError("attachment names.", attachmentMismatch("length mismatch"), 4);
        }
    }
    
    private Object objectFromNew(final String name, final Class type) throws NoSuchMethodException {
        final Object obj = null;
        try {
            try {
                try {
                    final Class clazz = LogManagerProperties.findClass(name);
                    return clazz.getConstructor((Class<?>[])null).newInstance((Object[])null);
                }
                catch (NoClassDefFoundError NCDFE) {
                    throw (ClassNotFoundException)new ClassNotFoundException(NCDFE.getMessage()).initCause(NCDFE);
                }
            }
            catch (ClassNotFoundException CNFE) {
                if (type == Formatter.class) {
                    return new TailNameFormatter(name);
                }
                throw CNFE;
            }
        }
        catch (NoSuchMethodException NSME) {
            throw NSME;
        }
        catch (Exception E) {
            this.reportError(E.getMessage(), E, 4);
            return obj;
        }
    }
    
    private Object initObject(final String key, final Class type) {
        final String name = LogManagerProperties.manager.getProperty(key);
        if (name != null && name.length() > 0 && !"null".equalsIgnoreCase(name)) {
            try {
                return this.objectFromNew(name, type);
            }
            catch (NoSuchMethodException E) {
                this.reportError(E.getMessage(), E, 4);
            }
        }
        return null;
    }
    
    private Object[] initArray(final String key, final Class type) {
        final String list = LogManagerProperties.manager.getProperty(key);
        if (list != null && list.length() > 0) {
            final String[] names = list.split(",");
            final Object[] a = (Object[])Array.newInstance(type, names.length);
            for (int i = 0; i < a.length; ++i) {
                names[i] = names[i].trim();
                if (!"null".equalsIgnoreCase(names[i])) {
                    try {
                        a[i] = this.objectFromNew(names[i], type);
                    }
                    catch (NoSuchMethodException E) {
                        this.reportError(E.getMessage(), E, 4);
                    }
                }
            }
            return a;
        }
        return (Object[])Array.newInstance(type, 0);
    }
    
    private Comparator initComparator(final String key) throws Exception {
        return (Comparator)this.initObject(key, Comparator.class);
    }
    
    private boolean isAttachmentLoggable(final LogRecord record) {
        final Filter[] filters = this.readOnlyAttachmentFilters();
        for (int i = 0; i < filters.length; ++i) {
            final Filter f = filters[i];
            if (f == null || f.isLoggable(record)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isPushable(final LogRecord record) {
        assert Thread.holdsLock(this);
        final int value = this.getPushLevel().intValue();
        if (value == MailHandler.offValue || record.getLevel().intValue() < value) {
            return false;
        }
        final Filter filter = this.getPushFilter();
        return filter == null || filter.isLoggable(record);
    }
    
    private void push(final int code, final boolean priority) {
        Message msg = null;
        try {
            msg = this.writeLogRecords(priority);
            if (msg != null) {
                Transport.send(msg);
            }
        }
        catch (Exception E) {
            try {
                super.reportError(this.toRawString(msg), E, code);
            }
            catch (MessagingException rawMe) {
                this.reportError(rawMe.toString(), E, code);
            }
            catch (IOException rawIo) {
                this.reportError(rawIo.toString(), E, code);
            }
        }
    }
    
    private synchronized Message writeLogRecords(final boolean priority) throws IOException, MessagingException {
        if (this.data.isEmpty()) {
            return null;
        }
        if (this.isWriting) {
            return null;
        }
        this.isWriting = true;
        Message msg;
        try {
            msg = this.createMessage();
            this.setPriority(msg, priority);
            Collection records = this.sortAsReadOnlyData();
            BodyPart[] parts = new BodyPart[this.attachmentFormatters.length];
            final StringBuffer[] buffers = new StringBuffer[parts.length];
            String contentType = null;
            StringBuffer buf = null;
            this.appendSubject(msg, this.head(this.subjectFormatter));
            final Formatter bodyFormat = this.getFormatter();
            final Filter bodyFilter = this.getFilter();
            for (final LogRecord r : records) {
                this.appendSubject(msg, this.format(this.subjectFormatter, r));
                if (bodyFilter == null || bodyFilter.isLoggable(r)) {
                    if (buf == null) {
                        buf = new StringBuffer();
                        final String head = this.head(bodyFormat);
                        buf.append(head);
                        contentType = this.contentTypeOf(head);
                    }
                    buf.append(this.format(bodyFormat, r));
                }
                for (int i = 0; i < parts.length; ++i) {
                    if (this.attachmentFilters[i] == null || this.attachmentFilters[i].isLoggable(r)) {
                        if (parts[i] == null) {
                            parts[i] = this.createBodyPart(i);
                            (buffers[i] = new StringBuffer()).append(this.head(this.attachmentFormatters[i]));
                            this.appendFileName(parts[i], this.head(this.attachmentNames[i]));
                        }
                        this.appendFileName(parts[i], this.format(this.attachmentNames[i], r));
                        buffers[i].append(this.format(this.attachmentFormatters[i], r));
                    }
                }
            }
            for (int j = parts.length - 1; j >= 0; --j) {
                if (parts[j] != null) {
                    this.appendFileName(parts[j], this.tail(this.attachmentNames[j], "err"));
                    buffers[j].append(this.tail(this.attachmentFormatters[j], ""));
                    final String content = buffers[j].toString();
                    if (content.length() > 0) {
                        final String name = parts[j].getFileName();
                        if (name == null || name.length() == 0) {
                            parts[j].setFileName(this.attachmentFormatters[j].toString());
                        }
                        parts[j].setText(content);
                    }
                    else {
                        parts[j] = null;
                    }
                    buffers[j] = null;
                }
            }
            if (buf != null) {
                buf.append(this.tail(bodyFormat, ""));
            }
            else {
                buf = new StringBuffer(0);
            }
            this.appendSubject(msg, this.tail(this.subjectFormatter, ""));
            records = null;
            this.data.clear();
            if (parts.length > 0) {
                final Multipart multipart = new MimeMultipart();
                if (buf.length() > 0) {
                    final BodyPart body = this.createBodyPart();
                    this.setContent(body, buf, contentType);
                    multipart.addBodyPart(body);
                }
                buf = null;
                for (int k = 0; k < parts.length; ++k) {
                    if (parts[k] != null) {
                        multipart.addBodyPart(parts[k]);
                    }
                }
                parts = null;
                msg.setContent(multipart);
            }
            else {
                this.setContent(msg, buf, contentType);
                buf = null;
            }
        }
        finally {
            this.isWriting = false;
            if (!this.data.isEmpty()) {
                this.data.clear();
            }
        }
        msg.setSentDate(new Date());
        msg.saveChanges();
        return msg;
    }
    
    private Message createMessage() throws MessagingException {
        assert Thread.holdsLock(this);
        final Properties proxyProps = new LogManagerProperties(this.mailProps, this.getClass().getName());
        final Session session = Session.getInstance(proxyProps, this.auth);
        final MimeMessage msg = new MimeMessage(session);
        this.setFrom(msg, proxyProps);
        this.setRecipient(msg, proxyProps, "mail.to", Message.RecipientType.TO);
        this.setRecipient(msg, proxyProps, "mail.cc", Message.RecipientType.CC);
        this.setRecipient(msg, proxyProps, "mail.bcc", Message.RecipientType.BCC);
        this.setReplyTo(msg, proxyProps);
        this.setSender(msg, proxyProps);
        this.setMailer(msg);
        return msg;
    }
    
    private BodyPart createBodyPart() throws MessagingException {
        final MimeBodyPart part = new MimeBodyPart();
        part.setDisposition("inline");
        part.setDescription(this.descriptionFrom(this.getFormatter(), this.getFilter()));
        return part;
    }
    
    private BodyPart createBodyPart(final int index) throws MessagingException {
        assert Thread.holdsLock(this);
        final MimeBodyPart part = new MimeBodyPart();
        part.setDisposition("attachment");
        part.setDescription(this.descriptionFrom(this.attachmentFormatters[index], this.attachmentFilters[index]));
        return part;
    }
    
    private String descriptionFrom(final Formatter formatter, final Filter filter) {
        return "Formatted using " + formatter.getClass().getName() + " and filtered with " + ((filter == null) ? "no filter" : filter.getClass().getName()) + '.';
    }
    
    private Collection newData(final int initialCapacity) {
        return new ArrayList(initialCapacity);
    }
    
    private void appendFileName(final Part part, final String chunk) {
        if (chunk != null) {
            if (chunk.length() > 0) {
                try {
                    final String old = part.getFileName();
                    part.setFileName((old != null) ? old.concat(chunk) : chunk);
                }
                catch (MessagingException ME) {
                    this.reportError(ME.getMessage(), ME, 5);
                }
            }
        }
        else {
            this.reportError("null", new NullPointerException(), 5);
        }
    }
    
    private void appendSubject(final Message msg, final String chunk) {
        if (chunk != null) {
            if (chunk.length() > 0) {
                try {
                    final String old = msg.getSubject();
                    msg.setSubject((old != null) ? old.concat(chunk) : chunk);
                }
                catch (MessagingException ME) {
                    this.reportError(ME.getMessage(), ME, 5);
                }
            }
        }
        else {
            this.reportError("null", new NullPointerException(), 5);
        }
    }
    
    private String head(final Formatter f) {
        try {
            return f.getHead(this);
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), RE, 5);
            return "";
        }
    }
    
    private String format(final Formatter f, final LogRecord r) {
        try {
            return f.format(r);
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), RE, 5);
            return "";
        }
    }
    
    private String tail(final Formatter f, final String def) {
        try {
            return f.getTail(this);
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), RE, 5);
            return def;
        }
    }
    
    private Collection sortAsReadOnlyData() {
        Collection records;
        if (this.comparator != null) {
            final LogRecord[] a = this.data.toArray(new LogRecord[this.data.size()]);
            try {
                Arrays.sort(a, this.comparator);
                records = Arrays.asList(a);
            }
            catch (RuntimeException RE) {
                this.reportError(RE.getMessage(), RE, 5);
                records = this.data;
            }
        }
        else {
            records = this.data;
        }
        return records;
    }
    
    private void setMailer(final Message msg) {
        try {
            final Class mail = MailHandler.class;
            final Class k = this.getClass();
            String value;
            if (k == mail) {
                value = mail.getName();
            }
            else {
                value = mail.getName() + " using the " + k.getName() + " extension.";
            }
            msg.setHeader("X-Mailer", value);
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), ME, 5);
        }
    }
    
    private void setPriority(final Message msg, final boolean priority) {
        if (priority) {
            try {
                msg.setHeader("X-Priority", "2");
            }
            catch (MessagingException ME) {
                this.reportError(ME.getMessage(), ME, 5);
            }
        }
    }
    
    private void setFrom(final Message msg, final Properties props) {
        final String from = props.getProperty("mail.from");
        if (from != null && from.length() > 0) {
            try {
                final InternetAddress[] address = InternetAddress.parse(from, false);
                if (address == null || address.length == 0) {
                    this.setDefaultFrom(msg);
                }
                else if (address.length == 1) {
                    msg.setFrom(address[0]);
                }
                else {
                    msg.addFrom(address);
                }
            }
            catch (MessagingException ME) {
                this.reportError(ME.getMessage(), ME, 5);
                this.setDefaultFrom(msg);
            }
        }
        else {
            this.setDefaultFrom(msg);
        }
    }
    
    private void setDefaultFrom(final Message msg) {
        try {
            msg.setFrom();
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), ME, 5);
        }
    }
    
    private void setReplyTo(final Message msg, final Properties props) {
        final String reply = props.getProperty("mail.reply.to");
        if (reply != null && reply.length() > 0) {
            try {
                final InternetAddress[] address = InternetAddress.parse(reply, false);
                if (address != null && address.length > 0) {
                    msg.setReplyTo(address);
                }
            }
            catch (MessagingException ME) {
                this.reportError(ME.getMessage(), ME, 5);
            }
        }
    }
    
    private void setSender(final MimeMessage msg, final Properties props) {
        final String sender = props.getProperty("mail.sender");
        if (sender != null && sender.length() > 0) {
            try {
                final InternetAddress[] address = InternetAddress.parse(sender, false);
                if (address != null && address.length > 0) {
                    msg.setSender(address[0]);
                    if (address.length > 1) {
                        this.reportError("Ignoring other senders.", new AddressException(Arrays.asList(address).subList(1, address.length).toString()), 5);
                    }
                }
            }
            catch (MessagingException ME) {
                this.reportError(ME.getMessage(), ME, 5);
            }
        }
    }
    
    private void setRecipient(final Message msg, final Properties props, final String key, final Message.RecipientType type) {
        final String value = props.getProperty(key);
        if (value != null && value.length() > 0) {
            try {
                final InternetAddress[] address = InternetAddress.parse(value, false);
                if (address != null && address.length > 0) {
                    msg.setRecipients(type, address);
                }
            }
            catch (MessagingException ME) {
                this.reportError(ME.getMessage(), ME, 5);
            }
        }
    }
    
    private String toRawString(final Message msg) throws MessagingException, IOException {
        if (msg != null) {
            final int size = Math.max(msg.getSize() + 1024, 1024);
            final ByteArrayOutputStream out = new ByteArrayOutputStream(size);
            msg.writeTo(out);
            return out.toString("US-ASCII");
        }
        return null;
    }
    
    private static RuntimeException attachmentMismatch(final String msg) {
        return new IndexOutOfBoundsException(msg);
    }
    
    private static RuntimeException attachmentMismatch(final int expected, final int found) {
        return attachmentMismatch("Attachments mismatched, expected " + expected + " but given " + found + '.');
    }
    
    private static String atIndexMsg(final int i) {
        return "At index: " + i + '.';
    }
    
    static {
        offValue = Level.OFF.intValue();
    }
    
    private static final class TailNameFormatter extends Formatter
    {
        private final String name;
        
        TailNameFormatter(final String name) {
            assert name != null;
            this.name = name;
        }
        
        public String format(final LogRecord record) {
            return "";
        }
        
        public String getTail(final Handler h) {
            return this.name;
        }
        
        public String toString() {
            return this.name;
        }
    }
}
