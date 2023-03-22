// 
// Decompiled by Procyon v0.5.36
// 

package util.test;

class FunctionBean
{
    private String functionName;
    private float usageRate;
    private int functionId;
    private int placement;
    
    public FunctionBean(final String functionName, final float usageRate, final int functionId, final int placement) {
        this.functionName = functionName;
        this.usageRate = usageRate;
        this.functionId = functionId;
        this.placement = placement;
    }
    
    public String getFunctionName() {
        return this.functionName;
    }
    
    public float getUsageRate() {
        return this.usageRate;
    }
    
    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }
    
    public void setUsageRate(final float usageRate) {
        this.usageRate = usageRate;
    }
    
    public int getFunctionId() {
        return this.functionId;
    }
    
    public int getPlacement() {
        return this.placement;
    }
    
    public void setFunctionId(final int functionId) {
        this.functionId = functionId;
    }
    
    public void setPlacement(final int placement) {
        this.placement = placement;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("FunctionBean [functionName=");
        builder.append(this.functionName);
        builder.append(", usageRate=");
        builder.append(this.usageRate);
        builder.append(", functionId=");
        builder.append(this.functionId);
        builder.append(", placement=");
        builder.append(this.placement);
        builder.append("]");
        return builder.toString();
    }
}
