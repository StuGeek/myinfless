// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.processors;

import java.util.Calendar;
import java.sql.Date;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsDateJsonBeanProcessor implements JsonBeanProcessor
{
    public JSONObject processBean(Object bean, final JsonConfig jsonConfig) {
        JSONObject jsonObject = null;
        if (bean instanceof Date) {
            bean = new java.util.Date(((Date)bean).getTime());
        }
        if (bean instanceof java.util.Date) {
            final Calendar c = Calendar.getInstance();
            c.setTime((java.util.Date)bean);
            jsonObject = new JSONObject().element("year", c.get(1)).element("month", c.get(2)).element("day", c.get(5)).element("hours", c.get(11)).element("minutes", c.get(12)).element("seconds", c.get(13)).element("milliseconds", c.get(14));
        }
        else {
            jsonObject = new JSONObject(true);
        }
        return jsonObject;
    }
}
