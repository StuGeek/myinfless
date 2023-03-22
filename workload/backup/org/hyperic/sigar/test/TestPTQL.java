// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.ptql.ProcessQuery;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.ptql.MalformedQueryException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.ptql.ProcessQueryFactory;

public class TestPTQL extends SigarTestCase
{
    private static final String THIS_PROCESS = "Pid.Pid.eq=$$";
    private static final String OTHER_PROCESS = "Pid.Pid.ne=$$";
    private static final String JAVA_PROCESS = "State.Name.eq=java";
    private static final String OTHER_JAVA_PROCESS = "State.Name.eq=java,Pid.Pid.ne=$$";
    private ProcessQueryFactory qf;
    private static final String[] OK_QUERIES;
    private static final String[] OK_RE_QUERIES;
    private static final String[] MALFORMED_QUERIES;
    
    public TestPTQL(final String name) {
        super(name);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        this.qf = new ProcessQueryFactory();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        this.qf.clear();
    }
    
    private int runQuery(final Sigar sigar, final String qs) throws MalformedQueryException, SigarException {
        ProcessQuery query;
        try {
            query = this.qf.getQuery(qs);
        }
        catch (MalformedQueryException e) {
            this.traceln("parse error: " + qs);
            throw e;
        }
        try {
            final long[] pids = query.find(sigar);
            this.traceln(pids.length + " processes match: " + qs);
            if (qs.indexOf("Pid.Pid.ne=$$") != -1) {
                final long pid = sigar.getPid();
                for (int i = 0; i < pids.length; ++i) {
                    assertTrue(pid + "!=" + pids[i], pid != pids[i]);
                }
            }
            return pids.length;
        }
        catch (SigarNotImplementedException e3) {
            return 0;
        }
        catch (SigarException e2) {
            this.traceln("Failed query: " + qs);
            throw e2;
        }
    }
    
    public void testValidQueries() throws Exception {
        final Sigar sigar = this.getSigar();
        assertTrue("Pid.Pid.eq=$$", this.runQuery(sigar, "Pid.Pid.eq=$$") == 1);
        final int numProcs = this.runQuery(sigar, "State.Name.eq=java");
        final int numOtherProcs = this.runQuery(sigar, "State.Name.eq=java,Pid.Pid.ne=$$");
        final String msg = "State.Name.eq=java [" + numProcs + "] vs. [" + numOtherProcs + "] " + "State.Name.eq=java,Pid.Pid.ne=$$";
        this.traceln(msg);
        for (int i = 0; i < TestPTQL.OK_QUERIES.length; ++i) {
            final String qs = TestPTQL.OK_QUERIES[i];
            assertTrue(qs, this.runQuery(sigar, qs) >= 0);
        }
    }
    
    public void testValidRegexQueries() throws Exception {
        for (int i = 0; i < TestPTQL.OK_RE_QUERIES.length; ++i) {
            final String qs = TestPTQL.OK_RE_QUERIES[i];
            assertTrue(qs, this.runQuery(this.getSigar(), qs) >= 0);
        }
    }
    
    public void testMalformedQueries() throws Exception {
        for (int i = 0; i < TestPTQL.MALFORMED_QUERIES.length; ++i) {
            final String qs = TestPTQL.MALFORMED_QUERIES[i];
            try {
                this.runQuery(this.getSigar(), qs);
                fail("'" + qs + "' did not throw MalformedQueryException");
            }
            catch (MalformedQueryException e) {
                this.traceln(qs + ": " + e.getMessage());
                assertTrue(qs + " Malformed", true);
            }
        }
    }
    
    public void testSelf() throws Exception {
        final Sigar sigar = this.getSigar();
        final String q = "Cpu.Percent.ge=0.01";
        final ProcessQuery status = this.qf.getQuery(q);
        final long pid = sigar.getPid();
        this.traceln(q + "=" + status.match(sigar, pid));
    }
    
    static {
        OK_QUERIES = new String[] { "State.Name.eq=java", "Exe.Name.ew=java", "State.Name.eq=java,Exe.Cwd.eq=$user.dir", "State.Name.eq=java,Exe.Cwd.eq=$PWD", "State.Name.ne=java,Exe.Cwd.eq=$user.dir", "State.Name.sw=httpsd,State.Name.Pne=$1", "State.Name.ct=ssh", "State.Name.eq=java,Args.-1.ew=AgentDaemon", "Cred.Uid.eq=1003,State.Name.eq=java,Args.-1.ew=AgentDaemon", "Cred.Uid.gt=0,Cred.Uid.lt=1000", "Cred.Uid.eq=1003,Cred.Gid.eq=1003", "CredName.User.eq=dougm", "Time.Sys.gt=1000", "Fd.Total.gt=20", "Mem.Size.ge=10000000,Mem.Share.le=1000000", "State.Name.eq=sshd,Cred.Uid.eq=0", "State.Name.eq=crond,Cred.Uid.eq=0", "State.State.eq=R", "Args.0.eq=sendmail: accepting connections", "Args.0.sw=sendmail: Queue runner@", "Args.1000.eq=foo", "Args.*.eq=org.apache.tools.ant.Main", "Args.*.ct=java", "Args.*.ew=sigar.jar", "Modules.*.re=libc|kernel", "Port.tcp.eq=80,Cred.Uid.eq=0", "Port.udp.eq=161,Cred.Uid.eq=0", "Port.tcp.eq=8080,Cred.Uid.eq=1003", "Pid.PidFile.eq=pid.file", "Pid.Pid.eq=1", "Pid.Pid.eq=$$", "Pid.Service.eq=Eventlog", "Service.Name.eq=NOSUCHSERVICE", "Service.Name.ct=Oracle", "Service.DisplayName.re=DHCP|DNS", "Service.Path.ct=svchost", "Service.Exe.Ieq=inetinfo.exe", "State.Name.eq=java,Pid.Pid.ne=$$", "Cpu.Percent.ge=0.2", "State.Name.sw=java,Args.*.eq=org.jboss.Main", "State.Name.eq=java,Args.*.eq=com.ibm.ws.runtime.WsServer", "State.Name.eq=java,Args.-1.eq=weblogic.Server", "State.Name.eq=perl,Args.*.eq=v" };
        OK_RE_QUERIES = new String[] { "Args.-1.eq=weblogic.Server,Env.WEBLOGIC_CLASSPATH.re=.*weblogic.jar.*", "State.Name.re=https?d.*|[Aa]pache2?$,State.Name.Pne=$1", "State.Name.re=post(master|gres),State.Name.Pne=$1,Args.0.re=.*post(master|gres)$", "State.Name.re=cfmx7|java,State.Name.Pne=$1,Args.*.ct=jrun.jar" };
        MALFORMED_QUERIES = new String[] { "foo", "State.Name", "State.Name.eq", "State.Namex.eq=foo", "Statex.Name.eq=foo", "State.Name.eqx=foo", "State.Name.Xeq=foo", "State.Name.eq=foo,Args.*.eq=$3", "State.Name.eq=$1", "State.State.eq=read", "Args.x.eq=foo", "Time.Sys.gt=x", "Pid.Pid.eq=foo", "Cpu.Percent.ge=x", "Port.foo.eq=8080", "Port.tcp.gt=8080", "Port.tcp.eq=http", "Cpu.Sys.ew=lots", "Service.Invalid.ew=.exe", "", null };
    }
}
