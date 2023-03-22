// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup;

import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import org.jsoup.helper.DataUtil;
import java.io.File;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;

public class Jsoup
{
    private Jsoup() {
    }
    
    public static Document parse(final String html, final String baseUri) {
        return Parser.parse(html, baseUri);
    }
    
    public static Document parse(final String html, final String baseUri, final Parser parser) {
        return parser.parseInput(html, baseUri);
    }
    
    public static Document parse(final String html) {
        return Parser.parse(html, "");
    }
    
    public static Connection connect(final String url) {
        return HttpConnection.connect(url);
    }
    
    public static Document parse(final File in, final String charsetName, final String baseUri) throws IOException {
        return DataUtil.load(in, charsetName, baseUri);
    }
    
    public static Document parse(final File in, final String charsetName) throws IOException {
        return DataUtil.load(in, charsetName, in.getAbsolutePath());
    }
    
    public static Document parse(final InputStream in, final String charsetName, final String baseUri) throws IOException {
        return DataUtil.load(in, charsetName, baseUri);
    }
    
    public static Document parse(final InputStream in, final String charsetName, final String baseUri, final Parser parser) throws IOException {
        return DataUtil.load(in, charsetName, baseUri, parser);
    }
    
    public static Document parseBodyFragment(final String bodyHtml, final String baseUri) {
        return Parser.parseBodyFragment(bodyHtml, baseUri);
    }
    
    public static Document parseBodyFragment(final String bodyHtml) {
        return Parser.parseBodyFragment(bodyHtml, "");
    }
    
    public static Document parse(final URL url, final int timeoutMillis) throws IOException {
        final Connection con = HttpConnection.connect(url);
        con.timeout(timeoutMillis);
        return con.get();
    }
    
    public static String clean(final String bodyHtml, final String baseUri, final Whitelist whitelist) {
        final Document dirty = parseBodyFragment(bodyHtml, baseUri);
        final Cleaner cleaner = new Cleaner(whitelist);
        final Document clean = cleaner.clean(dirty);
        return clean.body().html();
    }
    
    public static String clean(final String bodyHtml, final Whitelist whitelist) {
        return clean(bodyHtml, "", whitelist);
    }
    
    public static String clean(final String bodyHtml, final String baseUri, final Whitelist whitelist, final Document.OutputSettings outputSettings) {
        final Document dirty = parseBodyFragment(bodyHtml, baseUri);
        final Cleaner cleaner = new Cleaner(whitelist);
        final Document clean = cleaner.clean(dirty);
        clean.outputSettings(outputSettings);
        return clean.body().html();
    }
    
    public static boolean isValid(final String bodyHtml, final Whitelist whitelist) {
        return new Cleaner(whitelist).isValidBodyHtml(bodyHtml);
    }
}
