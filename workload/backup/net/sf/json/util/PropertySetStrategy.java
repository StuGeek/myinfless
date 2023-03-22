// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import org.apache.commons.beanutils.PropertyUtils;
import java.lang.reflect.Field;
import java.util.Map;
import net.sf.json.JsonConfig;
import net.sf.json.JSONException;

public abstract class PropertySetStrategy
{
    public static final PropertySetStrategy DEFAULT;
    
    public abstract void setProperty(final Object p0, final String p1, final Object p2) throws JSONException;
    
    public void setProperty(final Object bean, final String key, final Object value, final JsonConfig jsonConfig) throws JSONException {
        this.setProperty(bean, key, value);
    }
    
    static {
        DEFAULT = new DefaultPropertySetStrategy();
    }
    
    private static final class DefaultPropertySetStrategy extends PropertySetStrategy
    {
        public void setProperty(final Object bean, final String key, final Object value) throws JSONException {
            this.setProperty(bean, key, value, new JsonConfig());
        }
        
        public void setProperty(final Object bean, final String key, final Object value, final JsonConfig jsonConfig) throws JSONException {
            if (bean instanceof Map) {
                ((Map)bean).put(key, value);
            }
            else if (!jsonConfig.isIgnorePublicFields()) {
                try {
                    final Field field = bean.getClass().getField(key);
                    if (field != null) {
                        field.set(bean, value);
                    }
                }
                catch (Exception e) {
                    this._setProperty(bean, key, value);
                }
            }
            else {
                this._setProperty(bean, key, value);
            }
        }
        
        private void _setProperty(final Object bean, final String key, final Object value) {
            try {
                PropertyUtils.setSimpleProperty(bean, key, value);
            }
            catch (Exception e) {
                throw new JSONException(e);
            }
        }
    }
}
