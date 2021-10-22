package com.search;

import com.alibaba.fastjson.JSON;
import com.search.bean.Role;
import com.search.dao.RoleDao;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * bulk批量操作，导入数据库数据
 */
@SpringBootTest
public class bulk {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void bulkTest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        List<Role> roles = roleDao.selectList(null);//表示查询所有
        for (Role role : roles) {
            String data = JSON.toJSONString(role);
            IndexRequest request = new IndexRequest("roles").source(data, XContentType.JSON);
            bulkRequest.add(request);
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("导入状态:" + bulkResponse.status());
    }
}
