// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup;

import java.io.IOException;

public class UncheckedIOException extends RuntimeException
{
    public UncheckedIOException(final IOException cause) {
        super(cause);
    }
    
    public IOException ioException() {
        return (IOException)this.getCause();
    }
}
