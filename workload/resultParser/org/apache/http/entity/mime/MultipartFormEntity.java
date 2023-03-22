// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.http.ContentTooLongException;
import java.io.InputStream;
import org.apache.http.message.BasicHeader;
import org.apache.http.entity.ContentType;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

class MultipartFormEntity implements HttpEntity
{
    private final AbstractMultipartForm multipart;
    private final Header contentType;
    private final long contentLength;
    
    MultipartFormEntity(final AbstractMultipartForm multipart, final ContentType contentType, final long contentLength) {
        this.multipart = multipart;
        this.contentType = new BasicHeader("Content-Type", contentType.toString());
        this.contentLength = contentLength;
    }
    
    AbstractMultipartForm getMultipart() {
        return this.multipart;
    }
    
    @Override
    public boolean isRepeatable() {
        return this.contentLength != -1L;
    }
    
    @Override
    public boolean isChunked() {
        return !this.isRepeatable();
    }
    
    @Override
    public boolean isStreaming() {
        return !this.isRepeatable();
    }
    
    @Override
    public long getContentLength() {
        return this.contentLength;
    }
    
    @Override
    public Header getContentType() {
        return this.contentType;
    }
    
    @Override
    public Header getContentEncoding() {
        return null;
    }
    
    @Override
    public void consumeContent() {
    }
    
    @Override
    public InputStream getContent() throws IOException {
        if (this.contentLength < 0L) {
            throw new ContentTooLongException("Content length is unknown");
        }
        if (this.contentLength > 25600L) {
            throw new ContentTooLongException("Content length is too long: " + this.contentLength);
        }
        final ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        this.writeTo(outstream);
        outstream.flush();
        return new ByteArrayInputStream(outstream.toByteArray());
    }
    
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        this.multipart.writeTo(outstream);
    }
}
