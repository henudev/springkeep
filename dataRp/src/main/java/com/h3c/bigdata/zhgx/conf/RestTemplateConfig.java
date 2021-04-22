package com.h3c.bigdata.zhgx.conf;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.*;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    private int socketTimeout = 10000;
    private int connectTimeout = 10000;
    private Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);
    /**
     * 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，等待时间过长将是灾难性的
     */
    private int connectionRequestTimeout = 200;
    /**
     * 最大并发
     */
    private int maxTotal = 1000;
    /**
     * 同路由的并发数
     */
    private int defaultMaxPerRoute = new Double(maxTotal * 0.8).intValue();

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
            }
        }
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                switch (statusCode.series().ordinal()) {
                    case 1:
                        throw new HttpClientErrorException(statusCode, response.getStatusText(), response.getHeaders(), this.getResponseBody(response), this.getCharset(response));
                    case 2:
                        throw new HttpServerErrorException(statusCode, response.getStatusText(), response.getHeaders(), this.getResponseBody(response), this.getCharset(response));
                    default:
                        throw new RestClientException("Unknown status code [" + statusCode + "]");
                }
            }
        });
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory(HttpClient httpClient) {

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean
    public HttpClient httpClient() {


        try {
            //信任https
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType){
                    return true;
                }
            }).build();

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", SSLConnectionSocketFactory.getSocketFactory())
                    .build();

            //连接池配置
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
            connectionManager.setMaxTotal(maxTotal);
            connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

            RequestConfig requestConfig = RequestConfig.custom()
                    //读取超时时间
                    .setSocketTimeout(socketTimeout)
                    //连接超时时间
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .build();

            return HttpClientBuilder.create()
                    .setDefaultRequestConfig(requestConfig)
                    .setConnectionManager(connectionManager)
                    //设置重试次数，默认三次未开启
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
                    .setSSLContext(sslContext)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        //异常的话创建默认配置
        return HttpClients.createDefault();
    }
}
