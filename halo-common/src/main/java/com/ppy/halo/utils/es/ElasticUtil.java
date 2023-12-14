package com.ppy.halo.utils.es;

import com.alibaba.fastjson.JSON;
import com.ppy.halo.exception.DomainBizException;
import com.ppy.halo.utils.es.bo.DocumentBO;
import com.ppy.halo.utils.es.factory.ElasticClientPool;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: jackie
 * @date: 2023/12/25 17:55
 **/
@Slf4j
@Component
public class ElasticUtil {

    private static final int MAX_RETRY_TIMES = 3;


    /**
     * index是否存在
     *
     * @return
     */
    public boolean existIndex(String index) {
        RestHighLevelClient client = null;
        try {
            client = ElasticClientPool.getClient();
            GetIndexRequest getIndexRequest = new GetIndexRequest(index);
            boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            return exists;
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("existIndex error,{}", e.getMessage());
            if (!message.contains("201 Created") && !message.contains("200 OK")) {
                throw new DomainBizException(message);
            }
        } finally {
            ElasticClientPool.returnClient(client);
        }
        return false;
    }

    /**
     * 创建索引
     *
     * @param index    索引名称必须小写
     * @param mappings 设置mapping,json
     * @throws Exception
     */
    public void createIndex(String index, String mappings) {
        RestHighLevelClient client = null;
        try {
            if (existIndex(index)) {
                log.error("index={}已经存在,mappings={}", index, mappings);
                return;
            }
            client = ElasticClientPool.getClient();
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
            //createIndexRequest.settings(settings, XContentType.JSON);
            createIndexRequest.mapping(mappings, XContentType.JSON);
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (!createIndexResponse.isAcknowledged()) {
                throw new RuntimeException("创建索引" + index + "失败");
            }
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("创建index={}错误", index);
            if (!message.contains("201 Created") && !message.contains("200 OK")) {
                throw new DomainBizException(message);
            }
        } finally {
            ElasticClientPool.returnClient(client);
        }
    }

    /**
     * 添加文档
     *
     * @param index
     * @param document
     */
    public void addDocument(String index, DocumentBO document) {
        RestHighLevelClient client = null;
        try {
            client = ElasticClientPool.getClient();
            IndexRequest request = new IndexRequest(index);
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            request.id(document.getId());
            request.source(JSON.toJSONString(document.getData()), XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("添加文档出错,error={}", message);
            if (!message.contains("201 Created") && !message.contains("200 OK")) {
                throw new DomainBizException(message);
            }
        } finally {
            ElasticClientPool.returnClient(client);
        }
    }

    /**
     * 更新文档
     *
     * @param index
     * @param document
     */
    public void updateDocument(String index, DocumentBO document) {
        RestHighLevelClient client = null;
        try {
            client = ElasticClientPool.getClient();
            UpdateRequest request = new UpdateRequest(index, document.getId());
            request.doc(JSON.toJSONString(document.getData()), XContentType.JSON);
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            client.update(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("更新文档出错,error={}", message);
            if (!message.contains("201 Created") && !message.contains("200 OK")) {
                throw new DomainBizException(message);
            }
        } finally {
            ElasticClientPool.returnClient(client);
        }
    }

    /**
     * 批量添加文档
     *
     * @param index
     * @param documents
     */
    public void batchAddDocument(String index, List<DocumentBO> documents) {
        RestHighLevelClient client = null;
        try {
            client = ElasticClientPool.getClient();
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            documents.forEach(item -> bulkRequest.add(new IndexRequest(index).id(item.getId()).source(JSON.toJSONString(item.getData()), XContentType.JSON)));
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("批量添加文档出错，error={}", message);
            if (!message.contains("201 Created") && !message.contains("200 OK")) {
                throw new DomainBizException(message);
            }
        } finally {
            ElasticClientPool.returnClient(client);
        }
    }

    /**
     * 批量删除文档
     *
     * @param index
     * @param idList
     */
    public <T> void batchDeleteDoc(String index, Collection<T> idList) {
        RestHighLevelClient client = null;
        try {
            client = ElasticClientPool.getClient();
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            idList.forEach(item -> bulkRequest.add(new DeleteRequest(index, item.toString())));
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("批量删除文档出错，error={}", e.getMessage());
            if (!message.contains("201 Created") && !message.contains("200 OK")) {
                throw new DomainBizException(message);
            }
        } finally {
            ElasticClientPool.returnClient(client);
        }
    }

    /**
     * 查询接口
     *
     * @param index
     * @param builder
     * @param clz
     * @param <T>
     * @return
     */
    public <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> clz) {
        RestHighLevelClient client = null;
        try {
            client = ElasticClientPool.getClient();
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.source(builder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), clz));
            }
            return res;
        } catch (Exception e) {
            String message = e.getMessage();
            if (!message.contains("201 Created") && !message.contains("200 OK")) {
                throw new DomainBizException(message);
            }
        } finally {
            ElasticClientPool.returnClient(client);
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param index
     * @param builder
     * @param from
     * @param size
     * @return
     */
    public SearchResponse searchForPage(String index, SearchSourceBuilder builder, Integer from, Integer size) {
        int retryTime;
        SearchRequest searchRequest = new SearchRequest(index);
        builder.from(from);
        builder.size(size);
        searchRequest.source(builder);
        RestHighLevelClient client = null;
        try {
            log.info("SearchForPage ...");
            client = ElasticClientPool.getClient();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse;
        } catch (Exception e) {
            log.error("ES:searchForPage error={}", e.getMessage());
            do {
                SearchResponse searchResponse = null;
                try {
                    searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
                } catch (IOException ex) {
                    String message = ex.getMessage();
                    if (!message.contains("201 Created") && !message.contains("200 OK")) {
                        throw new DomainBizException(message);
                    }
                }
                return searchResponse;
            } while (++retryTime <= MAX_RETRY_TIMES);
        } finally {
            ElasticClientPool.returnClient(client);
        }
    }

    /**
     * 根据query删除文档
     *
     * @param index
     * @param builder
     */
    public void deleteByQuery(String index, QueryBuilder builder) {
        RestHighLevelClient client = null;
        try {
            client = ElasticClientPool.getClient();
            DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest();
            deleteByQueryRequest.setQuery(builder);
            client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("删除文档error={}", e.getMessage());
            throw new RuntimeException("ES:deleteByQuery, error={}" + e.getMessage());
        } finally {
            ElasticClientPool.returnClient(client);
        }
    }
    /*public boolean createIndex(String indexName, Map<String, Property> mappings) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        CreateIndexResponse createIndexResponse = client.indices()
                .create(c -> c.index(indexName)
                        .mappings(m -> m.properties(mappings)));
        boolean acknowledged = createIndexResponse.acknowledged();
        EsClientPool.returnClient(client);
        return acknowledged;
    }


    public boolean deleteIndex(List<String> indexes) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        boolean exists = checkIndexExists(client, indexes);
        if (!exists) {
            return false;
        }
        DeleteIndexResponse deleteIndexResponse = client.indices()
                .delete(e -> e.index(indexes));
        boolean acknowledged = deleteIndexResponse.acknowledged();
        EsClientPool.returnClient(client);
        return acknowledged;
    }

    private boolean checkIndexExists(RestHighLevelClient client, List<String> indexes) throws Exception {
        return client.indices()
                .exists(e -> e.index(indexes)).value();
    }

    public <T> void createDocument(String indexName, List<T> classes) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        //创建BulkOperation列表准备批量插入doc
        List<BulkOperation> bulkOperations = new ArrayList<>();
        classes.forEach(doc -> bulkOperations.add(BulkOperation.of(b -> b
                .index(c -> c
                        .document(doc)))));
        client.bulk(x -> x
                .index(indexName)
                .operations(bulkOperations));
        EsClientPool.returnClient(client);
    }

    public List<BulkResponseItem> deleteDocument(String indexName, List<String> docIdList) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        // 构建批量操作对象BulkOperation的集合
        List<BulkOperation> bulkOperations = new ArrayList<>();
        // 向集合中添加需要删除的文档id信息
        for (int i = 0; i < docIdList.size(); i++) {
            int finalI = i;
            bulkOperations.add(BulkOperation.of(b -> b
                    .delete((d -> d
                            .index(indexName)
                            .id(docIdList.get(finalI)
                            )
                    ))
            ));
        }
        // 调用客户端的bulk方法，并获取批量操作响应结果
        BulkResponse response = client
                .bulk(e -> e
                        .index(indexName)
                        .operations(bulkOperations));
        List<BulkResponseItem> items = response.items();
        EsClientPool.returnClient(client);
        return items;
    }

    public List<BulkResponseItem> updateDocument(String indexName, List<String> docIdList, List<Object> objList) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        //创建BulkOperation列表准备批量插入doc
        List<BulkOperation> bulkOperations = new ArrayList<>();
        //将id作为es id，也可不指定id es会自动生成id
        for (int i = 0; i < objList.size(); i++) {
            int finalI = i;
            bulkOperations.add(BulkOperation.of(b -> b
                    .update(u -> u
                            .index(indexName)
                            .id(docIdList.get(finalI))
                            .action(a -> a
                                    .doc(objList.get(finalI))
                            ))
            ));
        }
        BulkResponse bulk = client
                .bulk(x -> x
                        .index(indexName)
                        .operations(bulkOperations));
        List<BulkResponseItem> items = bulk.items();
        EsClientPool.returnClient(client);
        return items;
    }

    public <T> List<T> queryByTerms(String index, String field, List<FieldValue> fieldValues, Integer from, Integer size, Class<T> clz) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        SearchResponse<T> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .terms(t -> t
                                        .field(field)
                                        .terms(terms -> terms
                                                .value(fieldValues)
                                        )
                                )
                        )
                        .query(q -> q
                                .matchAll(m -> m))
                        .from(from)
                        .size(size)
                , clz);
        List<Hit<T>> hits = searchResponse.hits().hits();
        List<T> result = new ArrayList<>();
        for (Hit<T> hit : hits) {
            result.add(hit.source());
        }
        EsClientPool.returnClient(client);
        return result;
    }

    public Hit<Object> queryByTerm(String index, String field, long value) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        SearchResponse<Object> response = client.search(e -> e
                        .index(index)
                        .query(q -> q.term(t -> t.field(field).value(value)))
                        .query(q -> q
                                .matchAll(m -> m))
                , Object.class);
        HitsMetadata<Object> hits = response.hits();
        EsClientPool.returnClient(client);
        return hits.hits().get(0);
    }

    public List<Object> queryMultiMatch(String indexName, List<String> fields, String query, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .multiMatch(m -> m
                                        .fields(fields)
                                        .query(query)
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        List<Hit<Object>> list = searchResponse.hits().hits();
        List<Object> result = new ArrayList<>();
        for (Hit<Object> hit : list) {
            result.add(hit.source());
        }
        EsClientPool.returnClient(client);
        return result;
    }

    public <T> List<T> queryFuzzy(String indexName, String field, String value, String fuzziness, String sortField, SortOrder order, Integer from, Integer size, Class<T> clz) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        SearchResponse<T> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field(field)
                                        .value(value)
                                        .fuzziness(fuzziness))
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , clz);
        List<Hit<T>> list = searchResponse.hits().hits();
        List<T> result = new ArrayList<>();
        for (Hit<T> hit : list) {
            result.add(hit.source());
        }
        EsClientPool.returnClient(client);
        return result;
    }

    public Object queryAll(String index) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        SearchResponse<Object> obj = client.search(e -> e.index(index).query(q -> q.matchAll(m -> m)),
                Object.class);
        HitsMetadata<Object> hits = obj.hits();
        TotalHits total = hits.total();
        List<Hit<Object>> hits1 = hits.hits();
        Object source = hits1.get(0).source();
        EsClientPool.returnClient(client);
        return source;
    }

    public SearchResponse queryByNested(String index, Class<T> clz) throws Exception {
        RestHighLevelClient client = EsClientPool.getClient();
        //client.search()
        return null;
    }*/
}
