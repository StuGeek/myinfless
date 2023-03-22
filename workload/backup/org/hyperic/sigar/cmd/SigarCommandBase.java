// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandUsageException;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.pager.PageFetchException;
import org.hyperic.sigar.pager.PageFetcher;
import org.hyperic.sigar.pager.StaticPageFetcher;
import java.util.Iterator;
import java.util.Arrays;
import org.hyperic.sigar.shell.ProcessQueryCompleter;
import org.hyperic.sigar.shell.ShellBase;
import java.util.ArrayList;
import org.hyperic.sigar.util.PrintfFormat;
import java.util.Collection;
import org.hyperic.sigar.shell.CollectionCompleter;
import java.util.List;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Sigar;
import java.io.PrintStream;
import org.hyperic.sigar.util.GetlineCompleter;
import org.hyperic.sigar.shell.ShellCommandBase;

public abstract class SigarCommandBase extends ShellCommandBase implements GetlineCompleter
{
    protected Shell shell;
    protected PrintStream out;
    protected PrintStream err;
    protected Sigar sigar;
    protected SigarProxy proxy;
    protected List output;
    private CollectionCompleter completer;
    private GetlineCompleter ptqlCompleter;
    private Collection completions;
    private PrintfFormat formatter;
    private ArrayList printfItems;
    
    public SigarCommandBase(final Shell shell) {
        this.out = System.out;
        this.err = System.err;
        this.output = new ArrayList();
        this.completions = new ArrayList();
        this.printfItems = new ArrayList();
        this.shell = shell;
        this.out = shell.getOutStream();
        this.err = shell.getErrStream();
        this.sigar = shell.getSigar();
        this.proxy = shell.getSigarProxy();
        this.completer = new CollectionCompleter(shell);
        if (this.isPidCompleter()) {
            this.ptqlCompleter = new ProcessQueryCompleter(shell);
        }
    }
    
    public SigarCommandBase() {
        this(new Shell());
        this.shell.setPageSize(-1);
    }
    
    public void setOutputFormat(final String format) {
        this.formatter = new PrintfFormat(format);
    }
    
    public PrintfFormat getFormatter() {
        return this.formatter;
    }
    
    public String sprintf(final String format, final Object[] items) {
        return new PrintfFormat(format).sprintf(items);
    }
    
    public void printf(final String format, final Object[] items) {
        this.println(this.sprintf(format, items));
    }
    
    public void printf(final Object[] items) {
        final PrintfFormat formatter = this.getFormatter();
        if (formatter == null) {
            this.printfItems.add(items);
        }
        else {
            this.println(formatter.sprintf(items));
        }
    }
    
    public void printf(final List items) {
        this.printf(items.toArray(new Object[0]));
    }
    
    public void println(final String line) {
        if (this.shell.isInteractive()) {
            this.output.add(line);
        }
        else {
            this.out.println(line);
        }
    }
    
    private void flushPrintfItems() {
        if (this.printfItems.size() == 0) {
            return;
        }
        int[] max = null;
        for (final Object[] items : this.printfItems) {
            if (max == null) {
                max = new int[items.length];
                Arrays.fill(max, 0);
            }
            for (int i = 0; i < items.length; ++i) {
                final int len = items[i].toString().length();
                if (len > max[i]) {
                    max[i] = len;
                }
            }
        }
        final StringBuffer format = new StringBuffer();
        for (int j = 0; j < max.length; ++j) {
            format.append("%-" + max[j] + "s");
            if (j < max.length - 1) {
                format.append("    ");
            }
        }
        final Iterator it2 = this.printfItems.iterator();
        while (it2.hasNext()) {
            this.printf(format.toString(), it2.next());
        }
        this.printfItems.clear();
    }
    
    public void flush() {
        this.flushPrintfItems();
        try {
            this.shell.performPaging(new StaticPageFetcher(this.output));
        }
        catch (PageFetchException e) {
            this.err.println("Error paging: " + e.getMessage());
        }
        finally {
            this.output.clear();
        }
    }
    
    public abstract void output(final String[] p0) throws SigarException;
    
    protected boolean validateArgs(final String[] args) {
        return args.length == 0;
    }
    
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        if (!this.validateArgs(args)) {
            throw new ShellCommandUsageException(this.getSyntax());
        }
        try {
            this.output(args);
        }
        catch (SigarException e) {
            throw new ShellCommandExecException(e.getMessage());
        }
    }
    
    public Collection getCompletions() {
        return this.completions;
    }
    
    public GetlineCompleter getCompleter() {
        return null;
    }
    
    public boolean isPidCompleter() {
        return false;
    }
    
    public String completePid(final String line) {
        if (line.length() >= 1 && Character.isDigit(line.charAt(0))) {
            return line;
        }
        return this.ptqlCompleter.complete(line);
    }
    
    public String complete(final String line) {
        if (this.isPidCompleter()) {
            return this.completePid(line);
        }
        final GetlineCompleter c = this.getCompleter();
        if (c != null) {
            return c.complete(line);
        }
        this.completer.setCollection(this.getCompletions());
        return this.completer.complete(line);
    }
}
