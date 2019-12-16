package com.linzx.utils.http;

import com.linzx.utils.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

public class HttpRequestTemplate {

    private Logger LOG = LoggerFactory.getLogger(HttpRequestTemplate.class);

    protected CloseableHttpClient httpClient;

    protected PoolingHttpClientConnectionManager connectionManager;

    protected HttpHost httpProxy;

    HttpConfig configStorage;

    /**
     * 获取代理带代理地址的 HttpHost
     *
     * @return 获取代理带代理地址的 HttpHost
     */
    public HttpHost getHttpProxy() {
        return httpProxy;
    }

    public CloseableHttpClient getHttpClient() {
        if (httpClient != null) {
            return httpClient;
        }
        if (configStorage == null) {
            return httpClient = HttpClients.createDefault();
        }

        CloseableHttpClient httpClient = HttpClients
                .custom()
                //网络提供者
                .setDefaultCredentialsProvider(createCredentialsProvider(configStorage))
                //设置httpclient的SSLSocketFactory
                .setSSLSocketFactory(createSSL(configStorage))
                .setConnectionManager(connectionManager(configStorage))
                .setDefaultRequestConfig(createRequestConfig(configStorage))
                .build();
        if (connectionManager == null) {
            return this.httpClient = httpClient;
        }
        return httpClient;
    }

    private RequestConfig createRequestConfig(HttpConfig configStorage) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(configStorage.getSocketTimeout())
                .setConnectTimeout(configStorage.getConnectTimeout())
// .setConnectionRequestTimeout(1000)
                .build();
        return requestConfig;
    }

    /**
     * 初始化
     *
     * @param configStorage 请求配置
     */
    public HttpRequestTemplate(HttpConfig configStorage) {
        setHttpConfigStorage(configStorage);
    }

    public HttpRequestTemplate() {
        setHttpConfigStorage(null);
    }

    /**
     * 设置HTTP请求的配置
     *
     * @param configStorage 请求配置
     * @return 当前HTTP请求的客户端模板
     */
    public HttpRequestTemplate setHttpConfigStorage(HttpConfig configStorage) {
        this.configStorage = configStorage;
        if (configStorage != null && StringUtils.isNotBlank(configStorage.getHttpProxyHost())) {
            //http代理地址设置
            httpProxy = new HttpHost(configStorage.getHttpProxyHost(), configStorage.getHttpProxyPort());
            ;
        }
        return this;
    }

    /**
     * 初始化连接池
     * @param configStorage 配置
     * @return 连接池对象
     */
    public PoolingHttpClientConnectionManager connectionManager(HttpConfig configStorage){
        if (null != connectionManager){
            return connectionManager;
        }
        if (0 == configStorage.getMaxTotal() || 0 == configStorage.getDefaultMaxPerRoute()){
            return null;
        }
        if (LOG.isInfoEnabled()) {
            LOG.info(String.format("Initialize the PoolingHttpClientConnectionManager -- maxTotal:%s, defaultMaxPerRoute:%s", configStorage.getMaxTotal(), configStorage.getDefaultMaxPerRoute()));
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", createSSL(configStorage))
                .register("http", new PlainConnectionSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(configStorage.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(configStorage.getDefaultMaxPerRoute());

        return connectionManager;
    }

    /**
     * 创建凭据提供程序
     *
     * @param configStorage 请求配置
     * @return 凭据提供程序
     */
    public CredentialsProvider createCredentialsProvider(HttpConfig configStorage) {
        if (StringUtils.isBlank(configStorage.getAuthUsername())) {
            return null;
        }
        // 需要用户认证的代理服务器
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(configStorage.getAuthUsername(), configStorage.getAuthPassword()));
        return credsProvider;
    }

    /**
     *  创建ssl配置
     * @param configStorage 请求配置
     * @return SSLConnectionSocketFactory  Layered socket factory for TLS/SSL connections.
     */
    public SSLConnectionSocketFactory createSSL(HttpConfig configStorage){
        if (null == configStorage.getKeystore()){
            try {
                return new SSLConnectionSocketFactory(SSLContext.getDefault());
            } catch (NoSuchAlgorithmException e) {
                LOG.error("createSSL error", e);
            }
        }

        //读取本机存放的PKCS12证书文件
        try(InputStream instream = configStorage.getKeystoreInputStream()){
            //指定读取证书格式为PKCS12
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            char[] password = configStorage.getStorePassword().toCharArray();
            //指定PKCS12的密码
            keyStore.load(instream, password);
            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, password);
            // 创建 SSLContext
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, password).build();

            //指定TLS版本
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext, new String[]{"TLSv1"}, null,
                    new DefaultHostnameVerifier());

            return sslsf;
        } catch (Exception e) {
            LOG.error("createSSL error", e);
        }
        return null;
    }

    /**
     * http 请求执行
     * @param clientHttpRequest 客户端请求
     * @param <T> 响应类型
     * @return 类型对象
     */
    public <T> T doExecute(ClientHttpRequest<T> clientHttpRequest) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("uri:%s, httpMethod:%s ", clientHttpRequest.getURI().toString(), clientHttpRequest.getMethod()));
        }
        //判断是否有代理设置
        if (httpProxy != null){
            clientHttpRequest.proxy(httpProxy);
        }
        try (CloseableHttpResponse response = getHttpClient().execute(clientHttpRequest)) {
            return clientHttpRequest.handleResponse(response);
        }finally {
            clientHttpRequest.releaseConnection();
        }

    }

}
