package com.ppy.halo.utils.es.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: jackie
 * @date: 2023/4/25 17:31
 **/
@Slf4j
@Component
public class ElasticClientPool {

    @Autowired
    private ElasticClientPoolFactory factory;

    /**
     * 利用对象工厂类和配置类生成对象池
     */
    private static GenericObjectPool<RestHighLevelClient> clientPool;

    @PostConstruct
    public void init() {
        log.info("Init RestHighLevelClient pool...");
        clientPool = new GenericObjectPool<>(factory);
        log.info("Init RestHighLevelClient pool completed!");
    }

    /**
     * 获取client对象
     */
    public static RestHighLevelClient getClient() {
        log.info("从client池中获取对象");
        RestHighLevelClient client = null;
        try {
            client = clientPool.borrowObject();
        } catch (Exception e) {
            log.error("BorrowObject from pool error,{}", e.getMessage());
            //简单处理，直接抛出异常
            throw new RuntimeException("Borrow RestHighLevelClient error");
        }
        log.info("从client池中获取对象:{}", client);
        return client;
    }

    /**
     * 归还对象
     *
     * @param client
     */
    public static void returnClient(RestHighLevelClient client) {
        log.info("归还client对象");
        try {
            clientPool.returnObject(client);
        } catch (Exception e) {
            log.error("Return client error,{}", e.getMessage());
            throw new RuntimeException("Return client error");
        }
    }
}
