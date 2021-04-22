package com.h3c.bigdata.zhgx.function.report.util;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author q16926
 * @Title: EsClient
 * @ProjectName dcplum
 * @Description: TODO
 * @date 2019/1/1619:32
 */
@Configuration
public class EsClient {
    private static Logger log = LoggerFactory.getLogger(EsClient.class);
    private static String esNodeCluster;
    private static int esNodePort;
    private static String esNodeAddress;
    
    @Value("${spring.es.node.cluster}")
    public void setEsNodeCluster(String esNodeCluster) {
        EsClient.esNodeCluster = esNodeCluster;
    }

    @Value("${spring.es.node.address}")
    public void setEsNodeAddress(String esNodeAddress) {
        EsClient.esNodeAddress = esNodeAddress;
    }

    @Value("${spring.es.node.port}")
    public void setEsNodePort(int esNodePort) {
        EsClient.esNodePort = esNodePort;
    }
    /**
     * ES连接
     */
    private static TransportClient client;

    public static TransportClient getClient() {
        if (client == null) {
            //防止高并发时创建多个查询对象
            synchronized (EsClient.class) {   
                if (client == null) {
                    Settings settings = Settings.builder().put("cluster.name",esNodeCluster).
                            put("client.transport.sniff", true).build();
                    client = new PreBuiltTransportClient(settings);
                    try {
                        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esNodeAddress),
                                esNodePort));
                        List<DiscoveryNode> connectedNodes = client.connectedNodes();
                        connectedNodes.stream().forEach(node -> {
                            try {
                                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(node.getHostName()),
                                        esNodePort));
                            } catch (UnknownHostException e) {
                                log.error("get ES client node list failed", e);
                            }
                        });
                    } catch (UnknownHostException e) {
                        log.error("get ES client failed", e);
                    }
                }
            }
        }
        log.info("get client success");
        return client;
    }
}
