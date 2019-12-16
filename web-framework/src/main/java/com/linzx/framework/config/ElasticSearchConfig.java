package com.linzx.framework.config;

import com.linzx.framework.config.properties.ElasticSearchProperties;
import com.linzx.framework.es.service.ElasticsearchTemplate;
import com.linzx.framework.es.service.impl.ElasticsearchTemplateImpl;
import com.linzx.utils.Convert;
import com.linzx.utils.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@DependsOn({"elasticSearchProperties"})
public class ElasticSearchConfig {

    private Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Bean(destroyMethod="close")//这个close是调用RestHighLevelClient中的close
    @Scope("singleton")
    public RestHighLevelClient createRestClient() {
        RestHighLevelClient restHighLevelClient = null;
        try {
            if (StringUtils.isEmpty(ElasticSearchProperties.host)) {
                logger.warn("restClient host empty");
                return null;
            }
            String[] hostArr = Convert.toStrArray(ElasticSearchProperties.host, ",");
            List<HttpHost> httpHosts = new ArrayList<>(hostArr.length);
            for (int i = 0; i < 0; i++) {
                String[] hostAndPort = hostArr[i].split(":");
                httpHosts.add(new HttpHost(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
            }
            restHighLevelClient = EsClientBuilder.build(httpHosts)
                    .setConnectionRequestTimeoutMillis(ElasticSearchProperties.connectionRequestTimeoutMillis)
                    .setConnectTimeoutMillis(ElasticSearchProperties.connectTimeoutMillis)
                    .setMaxConnectPerRoute(ElasticSearchProperties.maxConnectPerRoute)
                    .setSocketTimeoutMillis(ElasticSearchProperties.socketTimeoutMillis)
                    .create();
        } catch (Exception e) {
            logger.error("createRestClient error：", e);
        }
        return restHighLevelClient;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(RestHighLevelClient restHighLevelClient) {
        return new ElasticsearchTemplateImpl(restHighLevelClient);
    }

    public static class EsClientBuilder {

        private int connectTimeoutMillis = 1000;
        private int socketTimeoutMillis = 30000;
        private int connectionRequestTimeoutMillis = 500;
        private int maxConnectPerRoute = 10;
        private int maxConnectTotal = 30;

        private final List<HttpHost> httpHosts;


        private EsClientBuilder(List<HttpHost> httpHosts) {
            this.httpHosts = httpHosts;
        }


        public EsClientBuilder setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public EsClientBuilder setSocketTimeoutMillis(int socketTimeoutMillis) {
            this.socketTimeoutMillis = socketTimeoutMillis;
            return this;
        }

        public EsClientBuilder setConnectionRequestTimeoutMillis(int connectionRequestTimeoutMillis) {
            this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
            return this;
        }

        public EsClientBuilder setMaxConnectPerRoute(int maxConnectPerRoute) {
            this.maxConnectPerRoute = maxConnectPerRoute;
            return this;
        }

        public EsClientBuilder setMaxConnectTotal(int maxConnectTotal) {
            this.maxConnectTotal = maxConnectTotal;
            return this;
        }


        public static EsClientBuilder build(List<HttpHost> httpHosts) {
            return new EsClientBuilder(httpHosts);
        }


        public RestHighLevelClient create() {
            HttpHost[] httpHostArr = httpHosts.toArray(new HttpHost[0]);
            RestClientBuilder builder = RestClient.builder(httpHostArr);
            builder.setRequestConfigCallback(requestConfigBuilder -> {
                requestConfigBuilder.setConnectTimeout(connectTimeoutMillis);
                requestConfigBuilder.setSocketTimeout(socketTimeoutMillis);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeoutMillis);
                return requestConfigBuilder;
            });
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.setMaxConnTotal(maxConnectTotal);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                return httpClientBuilder;
            });
            return new RestHighLevelClient(builder);
        }
    }

}
