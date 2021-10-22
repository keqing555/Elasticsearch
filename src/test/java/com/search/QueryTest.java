package com.search;

import com.alibaba.fastjson.JSON;
import com.search.bean.Role;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Elasticsearch高级查询
 */
@SpringBootTest
public class QueryTest {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void query() throws IOException {
        //构建查询请求对象，指定查询的索引名称
        SearchRequest searchRequest = new SearchRequest("roles");
        //创建查询条件构造器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = null;
        //查询所有
        //  queryBuilder = QueryBuilders.matchAllQuery();

        //termQuery，词条查询————查不到：rname中文值-elementalForce-star
        //queryBuilder = QueryBuilders.termQuery("rname", "keqing");

        //matchQuery,分词查询
        //queryBuilder = QueryBuilders.matchQuery("rname", "刻晴keqing");

        //模糊查询
        //queryBuilder = QueryBuilders.wildcardQuery("rname", "神");

        //范围查询
        /*RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
        rangeQuery.gte(18);
        rangeQuery.lte(20);*/

        //queryString，多条件（字段）查询，默认并集，分词，
        queryBuilder = QueryBuilders.queryStringQuery("雷").field("rname").field("elementalForce").defaultOperator(Operator.AND);


        //指定查询条件
        sourceBuilder.query(queryBuilder);
        //添加分页信息，默认十条
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        //添加查询条件构造器
        searchRequest.source(sourceBuilder);
        //查询
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //获取命中对象
        SearchHits hits = searchResponse.getHits();
        //获取总记录数
        long total = hits.getTotalHits().value;
        System.out.println("总数：" + total);
        //获取hits数据
        SearchHit[] searchHits = hits.getHits();
        //转为json格式字符串
        List<Role> list = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            String sourceAsString = searchHit.getSourceAsString();
            //转为java对象
            Role role = JSON.parseObject(sourceAsString, Role.class);
            list.add(role);
        }
        list.forEach(System.out::println);

    }
}
