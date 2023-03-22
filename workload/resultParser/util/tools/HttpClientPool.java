// 
// Decompiled by Procyon v0.5.36
// 

package util.tools;

import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.content.ContentBody;
import java.nio.charset.Charset;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.ContentType;
import org.apache.http.Consts;
import java.io.File;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import pojo.TwoTuple;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import java.io.IOException;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.config.Registry;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.TrustStrategy;
import java.security.KeyStore;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientPool
{
    private PoolingHttpClientConnectionManager poolConnManager;
    private final int maxTotalPool = 1000;
    private final int maxConPerRoute = 1000;
    private final int socketTimeout = 5000;
    private final int connectionRequestTimeout = 5000;
    private final int connectTimeout = 5000;
    private static String htmlStr;
    private static HttpClientPool httpClientDemo;
    
    static {
        HttpClientPool.htmlStr = "";
        HttpClientPool.httpClientDemo = null;
    }
    
    private HttpClientPool() {
        this.init();
    }
    
    public static synchronized HttpClientPool getInstance() {
        if (HttpClientPool.httpClientDemo == null) {
            HttpClientPool.httpClientDemo = new HttpClientPool();
        }
        return HttpClientPool.httpClientDemo;
    }
    
    public void init() {
        try {
            final SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            final HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
            final Registry<ConnectionSocketFactory> socketFactoryRegistry = (Registry<ConnectionSocketFactory>)RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", (PlainConnectionSocketFactory)sslsf).build();
            (this.poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry)).setMaxTotal(1000);
            this.poolConnManager.setDefaultMaxPerRoute(1000);
            final SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5000).build();
            this.poolConnManager.setDefaultSocketConfig(socketConfig);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public CloseableHttpClient getConnection() {
        final RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(5000).build();
        final CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.poolConnManager).setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }
    
    public static String getResponseHtml(final CloseableHttpClient httpclient, final String URL) {
        final HttpGet httpget = new HttpGet(URL);
        try {
            final CloseableHttpResponse response = httpclient.execute((HttpUriRequest)httpget);
            final HttpEntity entity = response.getEntity();
            HttpClientPool.htmlStr = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(response.getEntity());
        }
        catch (IOException e) {
            e.printStackTrace();
            return HttpClientPool.htmlStr;
        }
        finally {
            httpget.releaseConnection();
        }
        httpget.releaseConnection();
        return HttpClientPool.htmlStr;
    }
    
    public static int getResponseTime(final CloseableHttpClient httpclient, String URL) {
        URL = URL.trim();
        if (URL.endsWith("g") || URL.endsWith("f")) {
            return requestPic(httpclient, URL);
        }
        return requestHtml(httpclient, URL);
    }
    
    public static TwoTuple<Integer, String> postResponseTimeHtml(final CloseableHttpClient httpClient, final String url, final String jsonObjectStr) {
        final TwoTuple<Integer, String> item = new TwoTuple<Integer, String>();
        String result = "";
        int costTime = 65535;
        final long begin = System.currentTimeMillis();
        final HttpPost post = new HttpPost(url);
        Label_0195: {
            try {
                final StringEntity strEntity = new StringEntity(jsonObjectStr, "utf-8");
                strEntity.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
                final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).setConnectionRequestTimeout(5000).build();
                post.setEntity(strEntity);
                post.setConfig(requestConfig);
                final CloseableHttpResponse response = httpClient.execute((HttpUriRequest)post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    costTime = (int)(System.currentTimeMillis() - begin);
                }
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(response.getEntity());
            }
            catch (IOException e) {
                costTime = 65535;
                break Label_0195;
            }
            finally {
                post.releaseConnection();
            }
            post.releaseConnection();
        }
        item.first = costTime;
        item.second = result;
        return item;
    }
    
    public static int postResponseTime(final CloseableHttpClient httpClient, final String url, final String jsonObjectStr) {
        int costTime = 65535;
        final long begin = System.currentTimeMillis();
        final HttpPost post = new HttpPost(url);
        try {
            final StringEntity strEntity = new StringEntity(jsonObjectStr, "utf-8");
            strEntity.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).setConnectionRequestTimeout(5000).build();
            post.setEntity(strEntity);
            post.setConfig(requestConfig);
            final CloseableHttpResponse response = httpClient.execute((HttpUriRequest)post);
            if (response.getStatusLine().getStatusCode() == 200) {
                costTime = (int)(System.currentTimeMillis() - begin);
            }
            EntityUtils.consume(response.getEntity());
        }
        catch (IOException e) {
            costTime = 65535;
            return costTime;
        }
        finally {
            post.releaseConnection();
        }
        post.releaseConnection();
        return costTime;
    }
    
    public static int postResponseTimeFileUpdate(final CloseableHttpClient httpClient, final String url, final String fileUrl) throws Exception {
        int costTime = 65535;
        final HttpPost httpPost = new HttpPost(url);
        final File file = new File(fileUrl);
        if (!file.exists()) {
            return costTime;
        }
        final FileBody bin = new FileBody(file, ContentType.create("image/jpg", Consts.UTF_8));
        final HttpEntity entity = MultipartEntityBuilder.create().setCharset(Charset.forName("utf-8")).addPart("file", bin).build();
        httpPost.setEntity(entity);
        final long begin = System.currentTimeMillis();
        HttpResponse response = null;
        try {
            response = httpClient.execute((HttpUriRequest)httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpClientPool.htmlStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                costTime = (int)(System.currentTimeMillis() - begin);
            }
            EntityUtils.consume(response.getEntity());
        }
        catch (IOException e) {
            e.printStackTrace();
            costTime = 65535;
            return costTime;
        }
        finally {
            httpPost.releaseConnection();
        }
        httpPost.releaseConnection();
        return costTime;
    }
    
    private static int requestPic(final CloseableHttpClient httpClient, final String URL) {
        int costTime = 65535;
        long begin = 0L;
        long end = 0L;
        begin = System.currentTimeMillis();
        final HttpGet httpGet = new HttpGet(URL);
        try {
            final CloseableHttpResponse response = httpClient.execute((HttpUriRequest)httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                final InputStream inputStream = response.getEntity().getContent();
                final byte[] b = new byte[16384];
                while (inputStream.read(b) != -1) {}
                end = System.currentTimeMillis();
            }
            else {
                end = 65535L;
            }
            costTime = (int)(end - begin);
            response.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            costTime = 65535;
            return costTime;
        }
        finally {
            httpGet.releaseConnection();
        }
        httpGet.releaseConnection();
        return costTime;
    }
    
    private static int requestHtml(final CloseableHttpClient httpclient, final String URL) {
        int costTime = 65535;
        final long begin = System.currentTimeMillis();
        final HttpGet httpget = new HttpGet(URL);
        try {
            final CloseableHttpResponse response = httpclient.execute((HttpUriRequest)httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpClientPool.htmlStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                costTime = (int)(System.currentTimeMillis() - begin);
            }
            EntityUtils.consume(response.getEntity());
        }
        catch (IOException e) {
            e.printStackTrace();
            costTime = 65535;
            return costTime;
        }
        finally {
            httpget.releaseConnection();
        }
        httpget.releaseConnection();
        return costTime;
    }
    
    public static void main(final String[] args) {
        final HttpClientPool instance = getInstance();
        final CloseableHttpClient httpClient = instance.getConnection();
        final TwoTuple<Integer, String> twoTuple = postResponseTimeHtml(httpClient, "http://192.168.1.106:30300", "{\"n\":5}");
        System.out.println(twoTuple.toString());
    }
}
