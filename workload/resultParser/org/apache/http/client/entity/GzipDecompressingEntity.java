// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.entity;

import org.apache.http.HttpEntity;

public class GzipDecompressingEntity extends DecompressingEntity
{
    public GzipDecompressingEntity(final HttpEntity entity) {
        super(entity, GZIPInputStreamFactory.getInstance());
    }
}
