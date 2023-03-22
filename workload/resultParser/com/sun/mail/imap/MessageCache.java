// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.mail.imap;

import java.util.Arrays;
import javax.mail.Message;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

public class MessageCache
{
    private IMAPMessage[] messages;
    private int[] seqnums;
    private int size;
    private IMAPFolder folder;
    private boolean debug;
    private PrintStream out;
    private static final int SLOP = 64;
    
    MessageCache(final IMAPFolder folder, final IMAPStore store, final int size) {
        this.folder = folder;
        this.debug = store.getMessageCacheDebug();
        this.out = store.getSession().getDebugOut();
        if (this.debug) {
            this.out.println("DEBUG IMAP MC: create cache of size " + size);
        }
        this.ensureCapacity(size);
    }
    
    public int size() {
        return this.size;
    }
    
    public IMAPMessage getMessage(final int msgnum) {
        if (msgnum < 1 || msgnum > this.size) {
            throw new ArrayIndexOutOfBoundsException("message number out of bounds");
        }
        IMAPMessage msg = this.messages[msgnum - 1];
        if (msg == null) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: create message number " + msgnum);
            }
            msg = new IMAPMessage(this.folder, msgnum);
            this.messages[msgnum - 1] = msg;
            if (this.seqnumOf(msgnum) <= 0) {
                if (this.debug) {
                    this.out.println("DEBUG IMAP MC: it's expunged!");
                }
                msg.setExpunged(true);
            }
        }
        return msg;
    }
    
    public IMAPMessage getMessageBySeqnum(final int seqnum) {
        final int msgnum = this.msgnumOf(seqnum);
        if (msgnum < 0) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: no message seqnum " + seqnum);
            }
            return null;
        }
        return this.getMessage(msgnum);
    }
    
    public void expungeMessage(final int seqnum) {
        final int msgnum = this.msgnumOf(seqnum);
        if (msgnum < 0) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: expunge no seqnum " + seqnum);
            }
            return;
        }
        final IMAPMessage msg = this.messages[msgnum - 1];
        if (msg != null) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: expunge existing " + msgnum);
            }
            msg.setExpunged(true);
        }
        if (this.seqnums == null) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: create seqnums array");
            }
            this.seqnums = new int[this.messages.length];
            for (int i = 1; i < msgnum; ++i) {
                this.seqnums[i - 1] = i;
            }
            this.seqnums[msgnum - 1] = 0;
            for (int i = msgnum + 1; i <= this.seqnums.length; ++i) {
                this.seqnums[i - 1] = i - 1;
            }
        }
        else {
            this.seqnums[msgnum - 1] = 0;
            for (int i = msgnum + 1; i <= this.seqnums.length; ++i) {
                assert this.seqnums[i - 1] != 1;
                if (this.seqnums[i - 1] > 0) {
                    final int[] seqnums = this.seqnums;
                    final int n = i - 1;
                    --seqnums[n];
                }
            }
        }
    }
    
    public IMAPMessage[] removeExpungedMessages() {
        if (this.debug) {
            this.out.println("DEBUG IMAP MC: remove expunged messages");
        }
        final List mlist = new ArrayList();
        int oldnum = 1;
        int newnum = 1;
        while (oldnum <= this.size) {
            if (this.seqnumOf(oldnum) <= 0) {
                final IMAPMessage m = this.getMessage(oldnum);
                mlist.add(m);
            }
            else {
                if (newnum != oldnum) {
                    this.messages[newnum - 1] = this.messages[oldnum - 1];
                    if (this.messages[newnum - 1] != null) {
                        this.messages[newnum - 1].setMessageNumber(newnum);
                    }
                }
                ++newnum;
            }
            ++oldnum;
        }
        this.seqnums = null;
        this.shrink(newnum, oldnum);
        final IMAPMessage[] rmsgs = new IMAPMessage[mlist.size()];
        if (this.debug) {
            this.out.println("DEBUG IMAP MC: return " + rmsgs.length);
        }
        mlist.toArray(rmsgs);
        return rmsgs;
    }
    
    public IMAPMessage[] removeExpungedMessages(final Message[] msgs) {
        if (this.debug) {
            this.out.println("DEBUG IMAP MC: remove expunged messages");
        }
        final List mlist = new ArrayList();
        final int[] mnum = new int[msgs.length];
        for (int i = 0; i < msgs.length; ++i) {
            mnum[i] = msgs[i].getMessageNumber();
        }
        Arrays.sort(mnum);
        int oldnum = 1;
        int newnum = 1;
        int mnumi = 0;
        boolean keepSeqnums = false;
        while (oldnum <= this.size) {
            if (mnumi < mnum.length && oldnum == mnum[mnumi] && this.seqnumOf(oldnum) <= 0) {
                final IMAPMessage m = this.getMessage(oldnum);
                mlist.add(m);
                while (mnumi < mnum.length && mnum[mnumi] <= oldnum) {
                    ++mnumi;
                }
            }
            else {
                if (newnum != oldnum) {
                    this.messages[newnum - 1] = this.messages[oldnum - 1];
                    if (this.messages[newnum - 1] != null) {
                        this.messages[newnum - 1].setMessageNumber(newnum);
                    }
                    if (this.seqnums != null) {
                        this.seqnums[newnum - 1] = this.seqnums[oldnum - 1];
                    }
                }
                if (this.seqnums != null && this.seqnums[newnum - 1] != newnum) {
                    keepSeqnums = true;
                }
                ++newnum;
            }
            ++oldnum;
        }
        if (!keepSeqnums) {
            this.seqnums = null;
        }
        this.shrink(newnum, oldnum);
        final IMAPMessage[] rmsgs = new IMAPMessage[mlist.size()];
        if (this.debug) {
            this.out.println("DEBUG IMAP MC: return " + rmsgs.length);
        }
        mlist.toArray(rmsgs);
        return rmsgs;
    }
    
    private void shrink(final int newend, final int oldend) {
        this.size = newend - 1;
        if (this.debug) {
            this.out.println("DEBUG IMAP MC: size now " + this.size);
        }
        if (this.size == 0) {
            this.messages = null;
            this.seqnums = null;
        }
        else if (this.size > 64 && this.size < this.messages.length / 2) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: reallocate array");
            }
            final IMAPMessage[] newm = new IMAPMessage[this.size + 64];
            System.arraycopy(this.messages, 0, newm, 0, this.size);
            this.messages = newm;
            if (this.seqnums != null) {
                final int[] news = new int[this.size + 64];
                System.arraycopy(this.seqnums, 0, news, 0, this.size);
                this.seqnums = news;
            }
        }
        else {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: clean " + newend + " to " + oldend);
            }
            for (int msgnum = newend; msgnum < oldend; ++msgnum) {
                this.messages[msgnum - 1] = null;
                if (this.seqnums != null) {
                    this.seqnums[msgnum - 1] = 0;
                }
            }
        }
    }
    
    public void addMessages(final int count) {
        if (this.debug) {
            this.out.println("DEBUG IMAP MC: add " + count + " messages");
        }
        this.ensureCapacity(this.size + count);
    }
    
    private void ensureCapacity(final int newsize) {
        if (this.messages == null) {
            this.messages = new IMAPMessage[newsize + 64];
        }
        else if (this.messages.length < newsize) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: expand capacity to " + newsize);
            }
            final IMAPMessage[] newm = new IMAPMessage[newsize + 64];
            System.arraycopy(this.messages, 0, newm, 0, this.messages.length);
            this.messages = newm;
            if (this.seqnums != null) {
                final int[] news = new int[newsize + 64];
                System.arraycopy(this.seqnums, 0, news, 0, this.seqnums.length);
                this.seqnums = news;
            }
        }
        else if (newsize < this.size) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: shrink capacity to " + newsize);
            }
            for (int msgnum = newsize + 1; msgnum <= this.size; ++msgnum) {
                this.messages[msgnum - 1] = null;
                if (this.seqnums != null) {
                    this.seqnums[msgnum - 1] = -1;
                }
            }
        }
        this.size = newsize;
    }
    
    public int seqnumOf(final int msgnum) {
        if (this.seqnums == null) {
            return msgnum;
        }
        return this.seqnums[msgnum - 1];
    }
    
    private int msgnumOf(final int seqnum) {
        if (this.seqnums == null) {
            return seqnum;
        }
        if (seqnum < 1) {
            if (this.debug) {
                this.out.println("DEBUG IMAP MC: bad seqnum " + seqnum);
            }
            return -1;
        }
        for (int msgnum = seqnum; msgnum <= this.size; ++msgnum) {
            if (this.seqnums[msgnum - 1] == seqnum) {
                return msgnum;
            }
            if (this.seqnums[msgnum - 1] > seqnum) {
                break;
            }
        }
        return -1;
    }
}
