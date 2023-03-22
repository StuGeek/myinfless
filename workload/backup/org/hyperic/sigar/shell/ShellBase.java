// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.util.StringTokenizer;
import org.hyperic.sigar.pager.PageFetchException;
import org.hyperic.sigar.pager.PageFetcher;
import org.hyperic.sigar.pager.PageList;
import org.hyperic.sigar.pager.PageControl;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.hyperic.sigar.Sigar;
import java.io.EOFException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import org.hyperic.sigar.util.IteratorIterator;
import java.util.Iterator;
import java.io.IOException;
import java.io.File;
import java.io.PrintStream;
import org.hyperic.sigar.util.Getline;
import java.util.HashMap;
import java.util.Map;
import org.hyperic.sigar.util.GetlineCompleter;

public abstract class ShellBase implements ShellCommandMapper, GetlineCompleter, SIGINT
{
    public static final String PROP_PAGE_SIZE = "page.size";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private String name;
    private String prompt;
    private Map handlers;
    private HashMap hiddenCommands;
    protected Getline gl;
    protected PrintStream out;
    protected PrintStream err;
    private boolean doHistoryAdd;
    private int pageSize;
    private boolean isRedirected;
    private GetlineCompleter completer;
    
    public ShellBase() {
        this.name = null;
        this.prompt = null;
        this.handlers = null;
        this.out = System.out;
        this.err = System.err;
    }
    
    public void handleSIGINT() {
        this.gl.reset();
    }
    
    public void initHistory() throws IOException {
        final String historyFileName = "." + this.name + "_history";
        this.initHistory(new File(System.getProperty("user.home"), historyFileName));
    }
    
    public void initHistory(final File file) throws IOException {
        this.doHistoryAdd = true;
        this.gl.initHistoryFile(file);
    }
    
    public void registerSigIntHandler() {
        ShellIntHandler.register(this);
    }
    
    public void init(final String applicationName, final PrintStream out, final PrintStream err) {
        this.name = applicationName;
        this.prompt = applicationName;
        this.gl = new Getline();
        this.out = out;
        this.err = err;
        this.doHistoryAdd = false;
        this.pageSize = Integer.getInteger("page.size", 20);
        if (this.pageSize != -1) {
            --this.pageSize;
            if (this.pageSize < 1) {
                this.pageSize = 1;
            }
        }
        this.isRedirected = false;
        this.handlers = new HashMap();
        this.hiddenCommands = new HashMap();
        try {
            final ShellCommand_quit quitCommand = new ShellCommand_quit();
            final ShellCommand_source sourceCommand = new ShellCommand_source();
            this.registerCommandHandler(".", sourceCommand);
            this.registerCommandHandler("alias", new ShellCommand_alias());
            this.registerCommandHandler("exit", quitCommand);
            this.registerCommandHandler("get", new ShellCommand_get());
            this.registerCommandHandler("help", new ShellCommand_help());
            this.registerCommandHandler("q", quitCommand);
            this.registerCommandHandler("quit", quitCommand);
            this.registerCommandHandler("set", new ShellCommand_set());
            this.registerCommandHandler("source", sourceCommand);
            this.registerCommandHandler("sleep", new ShellCommand_sleep());
        }
        catch (Exception e) {
            err.println("ERROR: could not register standard commands: " + e);
            e.printStackTrace(err);
        }
        this.setHandlerHidden(".", true);
        this.setHandlerHidden("q", true);
        this.setHandlerHidden("exit", true);
        this.registerSigIntHandler();
        this.completer = new CollectionCompleter(this) {
            public Iterator getIterator() {
                final IteratorIterator it = new IteratorIterator();
                it.add(ShellBase.this.getCommandNameIterator());
                it.add(ShellCommand_alias.getAliases());
                return it;
            }
        };
    }
    
    public void readRCFile(final File rcFile, final boolean echoCommands) throws IOException {
        FileInputStream is = null;
        final boolean oldHistAdd = this.doHistoryAdd;
        this.doHistoryAdd = false;
        try {
            String line = null;
            is = new FileInputStream(rcFile);
            final BufferedReader in = new BufferedReader(new InputStreamReader(is));
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#")) {
                    if (line.length() == 0) {
                        continue;
                    }
                    if (echoCommands) {
                        this.err.println(line);
                    }
                    this.handleCommand(line);
                }
            }
        }
        finally {
            if (is != null) {
                is.close();
            }
            this.doHistoryAdd = oldHistAdd;
        }
    }
    
    public void setPrompt(final String prompt) {
        this.prompt = prompt;
    }
    
    public void registerCommandHandler(final String commandName, final ShellCommandHandler handler) throws ShellCommandInitException {
        this.handlers.put(commandName, handler);
        handler.init(commandName, this);
    }
    
    public String getInput(final String prompt) throws EOFException, IOException {
        return this.gl.getLine(prompt);
    }
    
    public String getInput(final String prompt, final boolean addToHistory) throws EOFException, IOException {
        return this.gl.getLine(prompt, addToHistory);
    }
    
    public String getHiddenInput(final String prompt) throws EOFException, IOException {
        return Sigar.getPassword(prompt);
    }
    
    public void sendToOutStream(final String s) {
        this.out.println(s);
    }
    
    public void sendToErrStream(final String s) {
        this.err.println(s);
    }
    
    public void run() {
        String input = null;
        ShellIntHandler.push(this);
        while (true) {
            try {
                input = this.gl.getLine(this.prompt + "> ", false);
            }
            catch (EOFException e2) {
                break;
            }
            catch (Exception e) {
                this.err.println("Fatal error reading input line: " + e);
                e.printStackTrace(this.err);
                return;
            }
            if (input != null && input.trim().length() != 0) {
                try {
                    this.handleCommand(input);
                    continue;
                }
                catch (NormalQuitCommandException nqce) {}
                break;
            }
            if (!Getline.isTTY()) {
                break;
            }
        }
        if (Getline.isTTY()) {
            this.out.println("Goodbye.");
        }
    }
    
    public void handleCommand(final String line) {
        String[] args;
        try {
            args = explodeQuoted(line);
        }
        catch (IllegalArgumentException exc) {
            this.out.println("Syntax error: Unbalanced quotes");
            return;
        }
        if (args.length != 0) {
            this.handleCommand(line, args);
        }
    }
    
    public void handleCommand(final String line, final String[] args) {
        ShellCommandHandler handler = null;
        PrintStream oldSysOut = null;
        PrintStream oldOut = null;
        final String command = args[0];
        if (args.length == 0) {
            return;
        }
        handler = this.getHandler(command);
        if (handler != null) {
            int useArgs = args.length;
            if (args.length > 2 && args[args.length - 2].equals(">")) {
                oldSysOut = System.out;
                oldOut = this.out;
                PrintStream newOut;
                try {
                    final FileOutputStream fOut = new FileOutputStream(args[args.length - 1]);
                    newOut = new PrintStream(fOut);
                }
                catch (IOException exc) {
                    this.err.println("Failed to redirect to output file: " + exc);
                    return;
                }
                this.isRedirected = true;
                System.setOut(this.out = newOut);
                useArgs -= 2;
            }
            final String[] subArgs = new String[useArgs - 1];
            System.arraycopy(args, 1, subArgs, 0, subArgs.length);
            try {
                this.processCommand(handler, subArgs);
            }
            catch (ShellCommandUsageException e) {
                String msg = e.getMessage();
                if (msg == null || msg.trim().length() == 0) {
                    msg = "an unknown error occurred";
                }
                this.err.println(command + ": " + msg);
            }
            catch (ShellCommandExecException e2) {
                this.err.println(e2.getMessage());
            }
            catch (NormalQuitCommandException e3) {
                throw e3;
            }
            catch (Exception e4) {
                this.err.println("Unexpected exception processing command '" + command + "': " + e4);
                e4.printStackTrace(this.err);
            }
            finally {
                if (this.doHistoryAdd) {
                    this.gl.addToHistory(line);
                }
                if (oldSysOut != null) {
                    this.isRedirected = false;
                    System.setOut(oldSysOut);
                    this.out = oldOut;
                }
            }
            return;
        }
        final String[] aliasArgs = ShellCommand_alias.getAlias(command);
        if (aliasArgs == null) {
            this.err.println("unknown command: " + command);
            return;
        }
        this.handleCommand(line, aliasArgs);
    }
    
    public void processCommand(final ShellCommandHandler handler, final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        handler.processCommand(args);
    }
    
    public PrintStream getOutStream() {
        return this.out;
    }
    
    public PrintStream getErrStream() {
        return this.err;
    }
    
    public Getline getGetline() {
        return this.gl;
    }
    
    public boolean hasCompleter(final ShellCommandHandler handler) {
        return GetlineCompleter.class.isAssignableFrom(handler.getClass());
    }
    
    public String complete(final ShellCommandHandler handler, final String line) {
        if (this.hasCompleter(handler)) {
            return ((GetlineCompleter)handler).complete(line);
        }
        return line;
    }
    
    public String complete(String line) {
        if (line == null) {
            return null;
        }
        final int ix = line.indexOf(" ");
        if (ix != -1) {
            final String cmd = line.substring(0, ix);
            final String sub = line.substring(ix + 1, line.length());
            final ShellCommandHandler handler = this.getHandler(cmd);
            if (handler != null) {
                final String hline = this.complete(handler, sub);
                return cmd + " " + hline;
            }
            return line;
        }
        else {
            line = this.completer.complete(line);
            if (this.getHandler(line) != null) {
                return line + " ";
            }
            return line;
        }
    }
    
    public ShellCommandHandler getHandler(final String command) {
        if (command == null) {
            return null;
        }
        return this.handlers.get(command.toLowerCase());
    }
    
    public void setHandlerHidden(final String handlerName, final boolean isHidden) {
        if (this.getHandler(handlerName) == null) {
            throw new IllegalArgumentException("Unknown handler: " + handlerName);
        }
        this.hiddenCommands.put(handlerName, isHidden ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public boolean handlerIsHidden(final String handlerName) {
        return this.hiddenCommands.get(handlerName) != null;
    }
    
    public Iterator getCommandNameIterator() {
        final ArrayList keyArray = new ArrayList();
        for (final String keyName : this.handlers.keySet()) {
            if (!this.handlerIsHidden(keyName)) {
                keyArray.add(keyName);
            }
        }
        final String[] keys = keyArray.toArray(new String[0]);
        Arrays.sort(keys);
        return Arrays.asList(keys).iterator();
    }
    
    public void shutdown() {
    }
    
    public boolean isRedirected() {
        return this.isRedirected;
    }
    
    public void setPageSize(final int size) {
        if (size == 0 || size < -1) {
            throw new IllegalArgumentException("Page size must be > 0 or -1");
        }
        this.pageSize = size;
    }
    
    public int getPageSize() {
        return this.pageSize;
    }
    
    private int getNumPages(final PageControl control, final PageList list) {
        final int pageSize = control.getPagesize();
        final int totalElems = list.getTotalSize();
        if (pageSize == -1) {
            return 1;
        }
        if (pageSize == 0) {
            return 0;
        }
        if (totalElems % pageSize == 0) {
            return totalElems / pageSize;
        }
        return totalElems / pageSize + 1;
    }
    
    private void printPage(final PrintStream out, final PageList data, int lineNo, final boolean printLineNumbers) {
        final Iterator i = data.iterator();
        while (i.hasNext()) {
            if (printLineNumbers) {
                out.print(lineNo++ + ": ");
            }
            out.println(i.next());
        }
    }
    
    public PageControl getDefaultPageControl() {
        final PageControl res = new PageControl(0, (this.getPageSize() == -1) ? -1 : this.getPageSize());
        return res;
    }
    
    public void performPaging(final PageFetcher fetcher) throws PageFetchException {
        this.performPaging(fetcher, this.getDefaultPageControl());
    }
    
    public void performPaging(final PageFetcher fetcher, final PageControl control) throws PageFetchException {
        if (control.getPagesize() == 0) {
            return;
        }
        boolean lineNumberMode = false;
        final PrintStream out = this.getOutStream();
        if (this.isRedirected()) {
            control.setPagesize(-1);
        }
        PageList data = fetcher.getPage((PageControl)control.clone());
        this.printPage(out, data, control.getPageEntityIndex() + 1, lineNumberMode);
        if (control.getPagesize() == -1 || data.size() < control.getPagesize()) {
            return;
        }
        while (true) {
            boolean printPage = false;
            final int totalPages = this.getNumPages(control, data);
            String cmd;
            try {
                cmd = this.getInput("--More-- (Page " + (control.getPagenum() + 1) + " of " + totalPages + ")", false);
            }
            catch (IOException exc) {
                out.println();
                break;
            }
            if (cmd == null || (cmd = cmd.trim()).length() == 0) {
                printPage = true;
                control.setPagenum(control.getPagenum() + 1);
            }
            else {
                if (cmd.equals("q")) {
                    break;
                }
                if (cmd.equals("b")) {
                    printPage = true;
                    if (control.getPagenum() > 0) {
                        control.setPagenum(control.getPagenum() - 1);
                    }
                }
                else if (cmd.equals("l")) {
                    lineNumberMode = !lineNumberMode;
                    printPage = true;
                }
                else if (cmd.equals("?")) {
                    out.println("  'b'        - Scroll back one page");
                    out.println("  'l'        - Toggle line number mode");
                    out.println("  'q'        - Quit paging");
                    out.println("  '<number>' - Jump to the specified page #");
                    out.println("  '<enter>'  - Scroll forward one page");
                }
                else {
                    int newPageNo;
                    try {
                        newPageNo = Integer.parseInt(cmd);
                    }
                    catch (NumberFormatException exc2) {
                        out.println("Unknown command '" + cmd + "' " + " type '?' for paging help");
                        continue;
                    }
                    if (newPageNo < 1 || newPageNo > totalPages) {
                        out.println(newPageNo + " out of range (must be " + "1 to " + totalPages + ")");
                    }
                    else {
                        control.setPagenum(newPageNo - 1);
                        printPage = true;
                    }
                }
            }
            if (printPage) {
                data = fetcher.getPage((PageControl)control.clone());
                this.printPage(out, data, control.getPageEntityIndex() + 1, lineNumberMode);
                if (data.size() < control.getPagesize()) {
                    break;
                }
                continue;
            }
        }
    }
    
    private static String[] explodeQuoted(String arg) {
        final ArrayList res = new ArrayList();
        boolean inQuote = false;
        arg = arg.trim();
        final StringTokenizer quoteTok = new StringTokenizer(arg, "\"", true);
        while (quoteTok.hasMoreTokens()) {
            final String elem = (String)quoteTok.nextElement();
            if (elem.equals("\"")) {
                inQuote = !inQuote;
            }
            else if (inQuote) {
                res.add(elem);
            }
            else {
                final StringTokenizer spaceTok = new StringTokenizer(elem.trim());
                while (spaceTok.hasMoreTokens()) {
                    res.add(spaceTok.nextToken());
                }
            }
        }
        if (inQuote) {
            throw new IllegalArgumentException("Unbalanced quotation marks");
        }
        return res.toArray(new String[0]);
    }
}
