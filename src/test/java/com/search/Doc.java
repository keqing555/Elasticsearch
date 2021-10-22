package com.search;

import com.alibaba.fastjson.JSON;
import com.search.bean.Role;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Elasticsearch文档操作
 */
@SpringBootTest
public class Doc {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 添加文档，使用map作为数据，存在就修改
     */
    @Test
    public void addDoc_map() throws IOException {
        Map map = new HashMap();
        //  map.put("id",1);
        map.put("name", "刻晴");
        map.put("sex", "女");
        map.put("age", 18);
        map.put("elemental_force", "雷系");
        //获取操作文档的对象
        IndexRequest indexRequest = new IndexRequest("genshin").id("1").source(map);
        //添加数据，获取结果
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("添加文档ID：" + index.getId());
    }

    /**
     * 添加文档，使用对象作为数据，存在就修改
     */
    @Test
    public void addDoc_bean() throws IOException {
        Role role = new Role();
        role.setRid(2);
        role.setRname("keqing");
        role.setArea("璃月");
        role.setSex("女");
        role.setAge(18);
        role.setStar("★★★★★");
        role.setElementalForce("雷系");
        //将对象转为Json
        String str = JSON.toJSONString(role);
        //获取操作文档的对象
        IndexRequest indexRequest = new IndexRequest("genshin").id(String.valueOf(role.getRid())).source(str, XContentType.JSON);
        //添加数据，获取结果
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("添加文档ID：" + indexResponse.getId());
    }

    /**
     * 根据ID查询文档
     */
    @Test
    public void queryDoc_ID() throws IOException {
        GetRequest getRequest = new GetRequest("genshin", "2");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println("文档数据：" + getResponse.getSourceAsString());
    }

    /**
     * 根据ID删除文档
     */

    @Test
    public void delDoc_ID() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("genshin", "1");
        DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("删除的文档ID:" + response.getId());
    }
}
