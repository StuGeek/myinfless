// 
// Decompiled by Procyon v0.5.36
// 

package util.test;

class ServerBean
{
    private int serverId;
    private String serverName;
    private float availRate;
    
    public ServerBean(final int serverId, final String serverName, final float availRate) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.availRate = availRate;
        System.out.println("create new server id=" + serverId);
    }
    
    public int getServerId() {
        return this.serverId;
    }
    
    public void setServerId(final int serverId) {
        this.serverId = serverId;
    }
    
    public String getServerName() {
        return this.serverName;
    }
    
    public float getAvailRate() {
        return this.availRate;
    }
    
    public void setServerName(final String serverName) {
        this.serverName = serverName;
    }
    
    public void setAvailRate(final float availRate) {
        this.availRate = availRate;
    }
}
