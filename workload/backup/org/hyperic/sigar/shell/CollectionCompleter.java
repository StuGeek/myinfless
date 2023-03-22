// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.io.PrintStream;
import java.util.ArrayList;
import org.hyperic.sigar.util.GetlineCompleter;

public class CollectionCompleter implements GetlineCompleter
{
    private ArrayList completions;
    private ShellBase shell;
    private PrintStream out;
    private Collection collection;
    
    public CollectionCompleter() {
        this.completions = new ArrayList();
        this.shell = null;
        this.out = System.out;
    }
    
    public CollectionCompleter(final ShellBase shell) {
        this.completions = new ArrayList();
        this.shell = null;
        this.out = System.out;
        this.shell = shell;
        this.out = shell.getOutStream();
    }
    
    public CollectionCompleter(final ShellBase shell, final Collection collection) {
        this(shell);
        this.setCollection(collection);
    }
    
    public Iterator getIterator() {
        return this.getCollection().iterator();
    }
    
    public Collection getCollection() {
        return this.collection;
    }
    
    public void setCollection(final Collection collection) {
        this.collection = collection;
    }
    
    private boolean startsWith(final String substr, final String[] possible) {
        for (int i = 0; i < possible.length; ++i) {
            if (!possible[i].startsWith(substr)) {
                return false;
            }
        }
        return true;
    }
    
    public String getPartialCompletion(final String[] possible) {
        if (possible.length == 0) {
            return "";
        }
        final String match = possible[0];
        final StringBuffer lcd = new StringBuffer();
        for (int i = 0; i < match.length() && this.startsWith(match.substring(0, i + 1), possible); ++i) {
            lcd.append(match.charAt(i));
        }
        return lcd.toString();
    }
    
    public String displayPossible(final List possible) {
        return this.displayPossible(possible.toArray(new String[possible.size()]));
    }
    
    public String displayPossible(final String[] possible) {
        final int size = possible.length;
        final String partial = this.getPartialCompletion(possible);
        for (final String match : possible) {
            this.out.println();
            this.out.print(match + " ");
        }
        if (this.shell != null) {
            this.shell.getGetline().redraw();
        }
        if (partial.length() > 0) {
            return partial;
        }
        return null;
    }
    
    public String complete(final String line) {
        this.completions.clear();
        final int len = line.length();
        final Iterator it = this.getIterator();
        while (it.hasNext()) {
            final String name = it.next();
            if (len == 0 || name.startsWith(line)) {
                this.completions.add(name);
            }
        }
        final int size = this.completions.size();
        switch (size) {
            case 0: {
                return line;
            }
            case 1: {
                return this.completions.get(0);
            }
            default: {
                final String partial = this.displayPossible(this.completions);
                if (partial != null) {
                    return partial;
                }
                return line;
            }
        }
    }
}
