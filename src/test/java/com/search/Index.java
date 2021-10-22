package com.search;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

/**
 * Elasticsearch索引操作
 */
@SpringBootTest
class Index {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
        System.out.println(restHighLevelClient);
    }

    /**
     * 添加索引
     */
    @Test
    public void addIndex() throws IOException {
        //使用restHighLevelClient获取操作索引的对象
        IndicesClient indices = restHighLevelClient.indices();
        //1，创建索引对象，设置索引名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("roles");
        //2，创建索引，获得返回值
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        //根据返回值判断结果
        System.out.println("创建索引结果：" + createIndexResponse.isAcknowledged());
    }

    /**
     * 添加索引，并添加映射
     */
    @Test
    public void addIndexAndMapping() throws IOException {
        IndicesClient indices = restHighLevelClient.indices();
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("example");
        //设置mapping
        String mapping = "{\n" +
                " \"properties\" : {\n" +
                " \"address\" : {\n " + " \"type\" : \"text\",\n" +
                " \"analyzer\" : \"ik_max_word\"\n" + " },\n" +
                " \"age\" : {\n" + " \"type\" : \"long\"\n" + " },\n" +
                " \"name\" : {\n" +
                " \"type\" : \"keyword\"\n" +
                " }\n" +
                " }\n"
                + " }";
        createIndexRequest.mapping(mapping, XContentType.JSON);
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println("船舰索引和映射：" + createIndexResponse.isAcknowledged());
    }

    /**
     * 查询索引
     */
    @Test
    public void queryIndex() throws IOException {
        //使用restHighLevelClient获取操作索引的对象
        IndicesClient indices = restHighLevelClient.indices();
        GetIndexRequest getIndexRequest = new GetIndexRequest("roles");
        //判断索引是否存在
        boolean exists = indices.exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println("索引是否存在：" + exists);
        //执行查询，获取返回值
        GetIndexResponse getIndexResponse = indices.get(getIndexRequest, RequestOptions.DEFAULT);
        Map<String, MappingMetaData> mappings = getIndexResponse.getMappings();
        for (String key : mappings.keySet()) {
            System.out.println(key + ":" + mappings.get(key).getSourceAsMap());
        }
    }

    /**
     * 删除索引
     */
    @Test
    public void delete() throws IOException {
        IndicesClient indices = restHighLevelClient.indices();
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("genshin");
        AcknowledgedResponse delete = indices.delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println("删除索引结果：" + delete.isAcknowledged());
    }
}
