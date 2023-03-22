// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import sun.misc.Signal;
import java.util.Stack;
import sun.misc.SignalHandler;

public class ShellIntHandler implements SignalHandler
{
    private static ShellBase handlerShell;
    private static Stack handlers;
    
    public static void register(final ShellBase shell) {
        ShellIntHandler.handlerShell = shell;
        ShellIntHandler.handlers = new Stack();
        Signal signal;
        try {
            signal = new Signal("INT");
        }
        catch (IllegalArgumentException e) {
            return;
        }
        try {
            Signal.handle(signal, new ShellIntHandler());
        }
        catch (Exception ex) {}
    }
    
    public void handle(final Signal signal) {
        if (ShellIntHandler.handlers.empty()) {
            ShellIntHandler.handlerShell.shutdown();
            Runtime.getRuntime().halt(0);
        }
        else {
            final SIGINT handler = ShellIntHandler.handlers.peek();
            handler.handleSIGINT();
        }
    }
    
    public static void push(final SIGINT handler) {
        ShellIntHandler.handlers.push(handler);
    }
    
    public static void pop() {
        ShellIntHandler.handlers.pop();
    }
}
