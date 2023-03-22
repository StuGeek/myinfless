// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.ptql;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class ProcessQueryFactory
{
    private static ProcessQueryFactory instance;
    private Map cache;
    
    public ProcessQueryFactory() {
        this.cache = new HashMap();
    }
    
    public void clear() {
        for (final SigarProcessQuery query : this.cache.values()) {
            query.destroy();
        }
        this.cache.clear();
    }
    
    public static ProcessQueryFactory getInstance() {
        if (ProcessQueryFactory.instance == null) {
            ProcessQueryFactory.instance = new ProcessQueryFactory();
        }
        return ProcessQueryFactory.instance;
    }
    
    public ProcessQuery getQuery(final String query) throws MalformedQueryException {
        if (query == null) {
            throw new MalformedQueryException("null query");
        }
        if (query.length() == 0) {
            throw new MalformedQueryException("empty query");
        }
        ProcessQuery pQuery = this.cache.get(query);
        if (pQuery != null) {
            return pQuery;
        }
        pQuery = new SigarProcessQuery();
        ((SigarProcessQuery)pQuery).create(query);
        this.cache.put(query, pQuery);
        return pQuery;
    }
    
    public static ProcessQuery getInstance(final String query) throws MalformedQueryException {
        return getInstance().getQuery(query);
    }
    
    static {
        ProcessQueryFactory.instance = null;
    }
}
