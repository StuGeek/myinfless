// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.pager;

import java.io.Serializable;

public class SortAttribute implements Serializable
{
    public static final int DEFAULT = 0;
    public static final int NAME = 1;
    public static final int CTIME = 2;
    public static final int ROLE_NAME = 1;
    public static final int RESGROUP_NAME = 2;
    public static final int RESTYPE_NAME = 4;
    public static final int RESOURCE_NAME = 5;
    public static final int OPERATION_NAME = 6;
    public static final int ROLE_MEMBER_CNT = 17;
    public static final int SUBJECT_NAME = 3;
    public static final int FIRST_NAME = 7;
    public static final int LAST_NAME = 8;
    public static final int EVENT_LOG_CTIME = 1;
    public static final int CONTROL_ACTION = 9;
    public static final int CONTROL_STATUS = 10;
    public static final int CONTROL_STARTED = 11;
    public static final int CONTROL_ELAPSED = 12;
    public static final int CONTROL_DATESCHEDULED = 13;
    public static final int CONTROL_DESCRIPTION = 14;
    public static final int CONTROL_NEXTFIRE = 15;
    public static final int CONTROL_ENTITYNAME = 16;
    public static final int OWNER_NAME = 21;
    public static final int SERVICE_NAME = 22;
    public static final int SERVICE_TYPE = 23;
    public static final int RT_NAME = 24;
    public static final int RT_LOW = 25;
    public static final int RT_AVG = 26;
    public static final int RT_PEAK = 27;
    
    private SortAttribute() {
    }
}
