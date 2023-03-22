// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.pager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;

public class Pager
{
    public static final String DEFAULT_PROCESSOR_CLASSNAME = "org.hyperic.sigar.pager.DefaultPagerProcessor";
    private static Map PagerProcessorMap;
    private PagerProcessor processor;
    private boolean skipNulls;
    private PagerEventHandler eventHandler;
    
    private Pager(final PagerProcessor processor) {
        this.processor = null;
        this.skipNulls = false;
        this.eventHandler = null;
        this.processor = processor;
        this.skipNulls = false;
        this.eventHandler = null;
        if (this.processor instanceof PagerProcessorExt) {
            this.skipNulls = ((PagerProcessorExt)this.processor).skipNulls();
            this.eventHandler = ((PagerProcessorExt)this.processor).getEventHandler();
        }
    }
    
    public static Pager getDefaultPager() {
        try {
            return getPager("org.hyperic.sigar.pager.DefaultPagerProcessor");
        }
        catch (Exception e) {
            throw new IllegalStateException("This should never happen: " + e);
        }
    }
    
    public static Pager getPager(final String pageProcessorClassName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Pager p = null;
        p = Pager.PagerProcessorMap.get(pageProcessorClassName);
        if (p == null) {
            final PagerProcessor processor = (PagerProcessor)Class.forName(pageProcessorClassName).newInstance();
            p = new Pager(processor);
            Pager.PagerProcessorMap.put(pageProcessorClassName, p);
        }
        return p;
    }
    
    public PageList seek(final Collection source, final int pagenum, final int pagesize) {
        return this.seek(source, pagenum, pagesize, null);
    }
    
    public PageList seek(final Collection source, PageControl pc) {
        if (pc == null) {
            pc = new PageControl();
        }
        return this.seek(source, pc.getPagenum(), pc.getPagesize(), null);
    }
    
    public PageList seek(final Collection source, final int pagenum, final int pagesize, final Object procData) {
        final PageList dest = new PageList();
        this.seek(source, dest, pagenum, pagesize, procData);
        dest.setTotalSize(source.size());
        return dest;
    }
    
    public void seek(final Collection source, final Collection dest, final int pagenum, final int pagesize) {
        this.seek(source, dest, pagenum, pagesize, null);
    }
    
    public void seek(final Collection source, final Collection dest, int pagenum, int pagesize, final Object procData) {
        final Iterator iter = source.iterator();
        if (pagesize == -1 || pagenum == -1) {
            pagenum = 0;
            pagesize = Integer.MAX_VALUE;
        }
        for (int i = 0, currentPage = 0; iter.hasNext() && currentPage < pagenum; currentPage += ((i % pagesize == 0) ? 1 : 0)) {
            iter.next();
            ++i;
        }
        if (this.eventHandler != null) {
            this.eventHandler.init();
        }
        if (this.skipNulls) {
            while (iter.hasNext() && dest.size() < pagesize) {
                Object elt;
                if (this.processor instanceof PagerProcessorExt) {
                    elt = ((PagerProcessorExt)this.processor).processElement(iter.next(), procData);
                }
                else {
                    elt = this.processor.processElement(iter.next());
                }
                if (elt == null) {
                    continue;
                }
                dest.add(elt);
            }
        }
        else {
            while (iter.hasNext() && dest.size() < pagesize) {
                dest.add(this.processor.processElement(iter.next()));
            }
        }
        if (this.eventHandler != null) {
            this.eventHandler.cleanup();
        }
    }
    
    public PageList seekAll(final Collection source, final int pagenum, final int pagesize, final Object procData) {
        final PageList dest = new PageList();
        this.seekAll(source, dest, pagenum, pagesize, procData);
        dest.setTotalSize(source.size());
        return dest;
    }
    
    public void seekAll(final Collection source, final Collection dest, int pagenum, int pagesize, final Object procData) {
        final Iterator iter = source.iterator();
        if (pagesize == -1 || pagenum == -1) {
            pagenum = 0;
            pagesize = Integer.MAX_VALUE;
        }
        for (int i = 0, currentPage = 0; iter.hasNext() && currentPage < pagenum; currentPage += ((i != 0 && i % pagesize == 0) ? 1 : 0)) {
            if (this.processor instanceof PagerProcessorExt) {
                final Object ret = ((PagerProcessorExt)this.processor).processElement(iter.next(), procData);
                if (ret != null) {
                    ++i;
                }
            }
            else {
                this.processor.processElement(iter.next());
                ++i;
            }
        }
        if (this.eventHandler != null) {
            this.eventHandler.init();
        }
        if (this.skipNulls) {
            while (iter.hasNext()) {
                Object elt;
                if (this.processor instanceof PagerProcessorExt) {
                    elt = ((PagerProcessorExt)this.processor).processElement(iter.next(), procData);
                }
                else {
                    elt = this.processor.processElement(iter.next());
                }
                if (elt == null) {
                    continue;
                }
                if (dest.size() >= pagesize) {
                    continue;
                }
                dest.add(elt);
            }
        }
        else {
            while (iter.hasNext()) {
                final Object elt = this.processor.processElement(iter.next());
                if (dest.size() < pagesize) {
                    dest.add(elt);
                }
            }
        }
        if (this.eventHandler != null) {
            this.eventHandler.cleanup();
        }
    }
    
    public PageList processAll(final PageList source) {
        final PageList dest = new PageList();
        final Iterator it = source.iterator();
        while (it.hasNext()) {
            final Object elt = this.processor.processElement(it.next());
            if (elt == null) {
                continue;
            }
            dest.add(elt);
        }
        dest.setTotalSize(source.getTotalSize());
        return dest;
    }
    
    static {
        Pager.PagerProcessorMap = Collections.synchronizedMap(new HashMap<Object, Object>());
    }
}
