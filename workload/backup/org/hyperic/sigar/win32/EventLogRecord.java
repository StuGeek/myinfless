// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import java.util.Date;

public class EventLogRecord
{
    private static final String NA = "N/A";
    long recordNumber;
    long timeGenerated;
    long timeWritten;
    long eventId;
    short eventType;
    short category;
    String categoryString;
    String source;
    String computerName;
    String user;
    String message;
    String logName;
    
    EventLogRecord() {
    }
    
    public String getLogName() {
        return this.logName;
    }
    
    void setLogName(final String logName) {
        this.logName = logName;
    }
    
    public long getRecordNumber() {
        return this.recordNumber;
    }
    
    public long getTimeGenerated() {
        return this.timeGenerated;
    }
    
    public long getTimeWritten() {
        return this.timeWritten;
    }
    
    public long getEventId() {
        return this.eventId;
    }
    
    public short getEventType() {
        return this.eventType;
    }
    
    public String getEventTypeString() {
        switch (this.eventType) {
            case 1: {
                return "Error";
            }
            case 2: {
                return "Warning";
            }
            case 4: {
                return "Information";
            }
            case 8: {
                return "Success Audit";
            }
            case 16: {
                return "Failure Audit";
            }
            default: {
                return "Unknown";
            }
        }
    }
    
    public short getCategory() {
        return this.category;
    }
    
    public String getCategoryString() {
        if (this.categoryString != null) {
            return this.categoryString.trim();
        }
        if (this.category == 0) {
            return "None";
        }
        return "(" + this.category + ")";
    }
    
    public String getSource() {
        return this.source;
    }
    
    public String getComputerName() {
        return this.computerName;
    }
    
    public String getUser() {
        return this.user;
    }
    
    private String getUserString() {
        if (this.user == null) {
            return "N/A";
        }
        return this.user;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getStringData() {
        return this.getMessage();
    }
    
    public String toString() {
        return "[" + this.getEventTypeString() + "] " + "[" + new Date(this.getTimeGenerated() * 1000L) + "] " + "[" + this.getSource() + "] " + "[" + this.getCategoryString() + "] " + "[" + (this.getEventId() & 0xFFFFL) + "] " + "[" + this.getUserString() + "] " + "[" + this.getComputerName() + "] " + this.getMessage();
    }
}
