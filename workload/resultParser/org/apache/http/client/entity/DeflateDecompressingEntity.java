// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.entity;

import org.apache.http.HttpEntity;

public class DeflateDecompressingEntity extends DecompressingEntity
{
    public DeflateDecompressingEntity(final HttpEntity entity) {
        super(entity, DeflateInputStreamFactory.getInstance());
    }
}
