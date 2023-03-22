// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

public class NetFlags
{
    public static final String NULL_HWADDR = "00:00:00:00:00:00";
    public static final String ANY_ADDR = "0.0.0.0";
    public static final String ANY_ADDR_V6 = "::";
    public static final String LOOPBACK_HOSTNAME = "localhost";
    public static final String LOOPBACK_ADDRESS = "127.0.0.1";
    public static final String LOOPBACK_ADDRESS_V6 = "::1";
    public static final int IFF_UP = 1;
    public static final int IFF_BROADCAST = 2;
    public static final int IFF_DEBUG = 4;
    public static final int IFF_LOOPBACK = 8;
    public static final int IFF_POINTOPOINT = 16;
    public static final int IFF_NOTRAILERS = 32;
    public static final int IFF_RUNNING = 64;
    public static final int IFF_NOARP = 128;
    public static final int IFF_PROMISC = 256;
    public static final int IFF_ALLMULTI = 512;
    public static final int IFF_MULTICAST = 2048;
    public static final int IFF_SLAVE = 4096;
    public static final int RTF_UP = 1;
    public static final int RTF_GATEWAY = 2;
    public static final int RTF_HOST = 4;
    public static final int CONN_CLIENT = 1;
    public static final int CONN_SERVER = 2;
    public static final int CONN_TCP = 16;
    public static final int CONN_UDP = 32;
    public static final int CONN_RAW = 64;
    public static final int CONN_UNIX = 128;
    public static final int CONN_PROTOCOLS = 240;
    public static final int TCP_ESTABLISHED = 1;
    public static final int TCP_SYN_SENT = 2;
    public static final int TCP_SYN_RECV = 3;
    public static final int TCP_FIN_WAIT1 = 4;
    public static final int TCP_FIN_WAIT2 = 5;
    public static final int TCP_TIME_WAIT = 6;
    public static final int TCP_CLOSE = 7;
    public static final int TCP_CLOSE_WAIT = 8;
    public static final int TCP_LAST_ACK = 9;
    public static final int TCP_LISTEN = 10;
    public static final int TCP_CLOSING = 11;
    public static final int TCP_IDLE = 12;
    public static final int TCP_BOUND = 13;
    public static final int TCP_UNKNOWN = 14;
    
    private NetFlags() {
    }
    
    public static int getConnectionProtocol(final String protocol) throws SigarException {
        if (protocol.equals("tcp")) {
            return 16;
        }
        if (protocol.equals("udp")) {
            return 32;
        }
        if (protocol.equals("raw")) {
            return 64;
        }
        if (protocol.equals("unix")) {
            return 128;
        }
        final String msg = "Protocol '" + protocol + "' not supported";
        throw new SigarException(msg);
    }
    
    public static native String getIfFlagsString(final long p0);
    
    public static boolean isAnyAddress(final String address) {
        return address == null || address.equals("0.0.0.0") || address.equals("::");
    }
    
    public static boolean isLoopback(final String address) {
        return address.equals("localhost") || address.equals("127.0.0.1") || address.equals("::1");
    }
}
