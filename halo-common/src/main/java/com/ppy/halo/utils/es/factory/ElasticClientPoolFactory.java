package com.ppy.halo.utils.es.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author: jackie
 * @date: 2023/12/25 17:09
 **/
@Slf4j
@Component
public class ElasticClientPoolFactory implements PooledObjectFactory<RestHighLevelClient> {

    /**
     * ip:port,ip:port...
     */
    private static String hosts;
    private static String username;
    private static String password;
    private static String scheme;

    @Value("${es.hosts}")
    public void setHosts(String hosts) {
        ElasticClientPoolFactory.hosts = hosts;
    }

    @Value("${es.username}")
    public void setUsername(String username) {
        ElasticClientPoolFactory.username = username;
    }

    @Value("${es.password}")
    public void setPassword(String password) {
        ElasticClientPoolFactory.password = password;
    }

    @Value("${es.scheme}")
    public void setScheme(String scheme) {
        ElasticClientPoolFactory.scheme = scheme;
    }

    @Override
    public void activateObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {

    }

    @Override
    public void destroyObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {
        pooledObject.getObject().close();
    }

    @Override
    public PooledObject<RestHighLevelClient> makeObject() throws Exception {
        //解析es集群host
        List<HttpHost> httpHosts = parseHosts(hosts);
        //创建低级客户端
        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));
        //设置请求头
        Header[] defaultHeaders = {new BasicHeader("header", "value")};
        restClientBuilder.setDefaultHeaders(defaultHeaders);
        //配置请求超时
        restClientBuilder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                // 连接5秒超时，套接字连接60s超时
                return requestConfigBuilder.setConnectTimeout(10000).setSocketTimeout(60000);
            }
        });
        if (!Objects.isNull(username) && !Objects.isNull(password)) {
            final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLIOSessionStrategy sessionStrategy = new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE);
            restClientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    httpClientBuilder.disableAuthCaching();
                    httpClientBuilder.setSSLStrategy(sessionStrategy);
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    return httpClientBuilder;
                }
            });
        }
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        log.info("Elastic client 创建完成！！！");
        return new DefaultPooledObject<>(restHighLevelClient);
        //以下注释代码是es8推荐使用的客户端
        /**使用Jackson映射器创建传输层
         ElasticsearchTransport transport = new RestClientTransport(
         restClient, new JacksonJsonpMapper()
         );
         //创建JAVA API客户端
         ElasticsearchClient client = new ElasticsearchClient(transport);*/
    }

    @Override
    public void passivateObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<RestHighLevelClient> pooledObject) {
        return true;
    }

    private List<HttpHost> parseHosts(String hosts) {
        List<HttpHost> httpHosts = new ArrayList<>();
        List<String> hostList = Arrays.asList(hosts.split(","));
        for (String host : hostList) {
            httpHosts.add(new HttpHost(host.substring(0, host.indexOf(":")), Integer.parseInt(host.substring(host.indexOf(":") + 1)), scheme));
        }
        return httpHosts;
    }
}
