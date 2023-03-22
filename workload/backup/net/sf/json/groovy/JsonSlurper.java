// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.groovy;

import net.sf.json.JSONSerializer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import net.sf.json.JSON;
import java.io.File;
import net.sf.json.JsonConfig;
import groovy.lang.GroovyObjectSupport;

public class JsonSlurper extends GroovyObjectSupport
{
    private JsonConfig jsonConfig;
    
    public JsonSlurper() {
        this(new JsonConfig());
    }
    
    public JsonSlurper(final JsonConfig jsonConfig) {
        this.jsonConfig = ((jsonConfig != null) ? jsonConfig : new JsonConfig());
    }
    
    public JSON parse(final File file) throws IOException {
        return this.parse(new FileReader(file));
    }
    
    public JSON parse(final URL url) throws IOException {
        return this.parse(url.openConnection().getInputStream());
    }
    
    public JSON parse(final InputStream input) throws IOException {
        return this.parse(new InputStreamReader(input));
    }
    
    public JSON parse(final String uri) throws IOException {
        return this.parse(new URL(uri));
    }
    
    public JSON parse(final Reader reader) throws IOException {
        final StringBuffer buffer = new StringBuffer();
        final BufferedReader in = new BufferedReader(reader);
        String line = null;
        while ((line = in.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        return this.parseText(buffer.toString());
    }
    
    public JSON parseText(final String text) {
        return JSONSerializer.toJSON((Object)text, this.jsonConfig);
    }
}
