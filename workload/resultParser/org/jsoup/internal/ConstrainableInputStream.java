// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.internal;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.jsoup.helper.Validate;
import java.io.InputStream;
import java.io.BufferedInputStream;

public final class ConstrainableInputStream extends BufferedInputStream
{
    private static final int DefaultSize = 32768;
    private final boolean capped;
    private final int maxSize;
    private long startTime;
    private long timeout;
    private int remaining;
    private boolean interrupted;
    
    private ConstrainableInputStream(final InputStream in, final int bufferSize, final int maxSize) {
        super(in, bufferSize);
        this.timeout = 0L;
        Validate.isTrue(maxSize >= 0);
        this.maxSize = maxSize;
        this.remaining = maxSize;
        this.capped = (maxSize != 0);
        this.startTime = System.nanoTime();
    }
    
    public static ConstrainableInputStream wrap(final InputStream in, final int bufferSize, final int maxSize) {
        return (ConstrainableInputStream)((in instanceof ConstrainableInputStream) ? in : new ConstrainableInputStream(in, bufferSize, maxSize));
    }
    
    @Override
    public int read(final byte[] b, final int off, int len) throws IOException {
        if (this.interrupted || (this.capped && this.remaining <= 0)) {
            return -1;
        }
        if (Thread.interrupted()) {
            this.interrupted = true;
            return -1;
        }
        if (this.expired()) {
            throw new SocketTimeoutException("Read timeout");
        }
        if (this.capped && len > this.remaining) {
            len = this.remaining;
        }
        try {
            final int read = super.read(b, off, len);
            this.remaining -= read;
            return read;
        }
        catch (SocketTimeoutException e) {
            return 0;
        }
    }
    
    public ByteBuffer readToByteBuffer(final int max) throws IOException {
        Validate.isTrue(max >= 0, "maxSize must be 0 (unlimited) or larger");
        final boolean localCapped = max > 0;
        final int bufferSize = (localCapped && max < 32768) ? max : 32768;
        final byte[] readBuffer = new byte[bufferSize];
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        int remaining = max;
        while (true) {
            final int read = this.read(readBuffer);
            if (read == -1) {
                break;
            }
            if (localCapped) {
                if (read >= remaining) {
                    outStream.write(readBuffer, 0, remaining);
                    break;
                }
                remaining -= read;
            }
            outStream.write(readBuffer, 0, read);
        }
        return ByteBuffer.wrap(outStream.toByteArray());
    }
    
    @Override
    public void reset() throws IOException {
        super.reset();
        this.remaining = this.maxSize - this.markpos;
    }
    
    public ConstrainableInputStream timeout(final long startTimeNanos, final long timeoutMillis) {
        this.startTime = startTimeNanos;
        this.timeout = timeoutMillis * 1000000L;
        return this;
    }
    
    private boolean expired() {
        if (this.timeout == 0L) {
            return false;
        }
        final long now = System.nanoTime();
        final long dur = now - this.startTime;
        return dur > this.timeout;
    }
}
