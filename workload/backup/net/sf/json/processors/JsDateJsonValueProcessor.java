// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.processors;

import net.sf.json.JsonConfig;

public class JsDateJsonValueProcessor implements JsonValueProcessor
{
    private JsonBeanProcessor processor;
    
    public JsDateJsonValueProcessor() {
        this.processor = new JsDateJsonBeanProcessor();
    }
    
    public Object processArrayValue(final Object value, final JsonConfig jsonConfig) {
        return this.process(value, jsonConfig);
    }
    
    public Object processObjectValue(final String key, final Object value, final JsonConfig jsonConfig) {
        return this.process(value, jsonConfig);
    }
    
    private Object process(final Object value, final JsonConfig jsonConfig) {
        return this.processor.processBean(value, jsonConfig);
    }
}
