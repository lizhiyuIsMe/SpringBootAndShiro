package com.example.demo.test.es;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
class ElasticSearchService implements NBAPlayerService  {

    //es 提供的操作es的对象
    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private NBAPlayerDao nbaPlayerDao;

    //这里通常是索引的别名,不会用索引的真实名字
    //在更换索引mapping时候,只能重建索引
    private static final String NBA_INDEX = "nba_latest";

    private static final int START_OFFSET = 0;

    private static final  int MAX_COUNT = 1000;

    //根据 添加信息 和 文档id 和 索引名 来添加文档
    @Override
    public boolean addPlayer(NBAPlayer player, String id) throws IOException {
        IndexRequest request = new IndexRequest(NBA_INDEX).id(id).source(beanToMap(player));
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));
        return false;
    }

    //根据_doc 的id 去查询数据
    @Override
    public Map<String,Object> getPlayer(String id) throws IOException {
        GetRequest getRequest = new GetRequest(NBA_INDEX,id);
        GetResponse response = client.get(getRequest,RequestOptions.DEFAULT);
        return response.getSource();
    }

    //更新信息
    @Override
    public boolean updatePlayer(NBAPlayer player,String id) throws IOException {
        UpdateRequest request = new UpdateRequest(NBA_INDEX,id).doc(beanToMap(player));
        UpdateResponse response = client.update(request,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));
        return true;
    }

    //根据 _doc 的id 和索引名 去删除数据
    @Override
    public boolean deletePlayer(String id) throws IOException {
        DeleteRequest request = new DeleteRequest(NBA_INDEX,id);
        client.delete(request,RequestOptions.DEFAULT);
        return true;
    }

    //删除所有的信息
    @Override
    public boolean deleteAllPlayer() throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(NBA_INDEX);
        BulkByScrollResponse response = client.deleteByQuery(request,RequestOptions.DEFAULT);
        return true;
    }

    //将库里面数据查询出来存储到 es中
    @Override
    public boolean importAll() throws IOException {
       List<NBAPlayer> list = nbaPlayerDao.selectAll();
       for(NBAPlayer player: list){
           addPlayer(player,String.valueOf(player.getId()));
       }
        return true;
    }

    //key 是字段名， value 是值,  使用term精确查找
    //字段值 和要查找value 完全匹配才可以
    @Override
    public List<NBAPlayer> searchMatch(String key,String value) throws IOException {
        //根据索引别名创建对象
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        //设置筛选参数
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery(key,value));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));
        //hit 符合的结果
        SearchHit[] hits = response.getHits().getHits();
        List<NBAPlayer> playerList = new LinkedList<>();
        for(SearchHit hit: hits){
            NBAPlayer player = JSONObject.parseObject(hit.getSourceAsString(),NBAPlayer.class);
            playerList.add(player);
        }

        return playerList;
    }
    //key 是字段名， value 是值,  使用全局查找
    //字段值 和要查找value 中有分词匹配了就算匹配
    @Override
    public List<NBAPlayer> searchTerm(String key,String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.termQuery(key,value));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));

        SearchHit[] hits = response.getHits().getHits();
        List<NBAPlayer> playerList = new LinkedList<>();
        for(SearchHit hit: hits){
            NBAPlayer player = JSONObject.parseObject(hit.getSourceAsString(),NBAPlayer.class);
            playerList.add(player);
        }

        return playerList;
    }


    //根据开头字母查询    key 是字段名    value查找的值
    //每个分词的开头字母匹配即可
    @Override
    public List<NBAPlayer> searchMatchPrefix(String key,String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //根据开头字母匹配查询
        searchSourceBuilder.query(QueryBuilders.prefixQuery(key,value));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));

        SearchHit[] hits = response.getHits().getHits();
        List<NBAPlayer> playerList = new LinkedList<>();
        for(SearchHit hit: hits){
            NBAPlayer player = JSONObject.parseObject(hit.getSourceAsString(),NBAPlayer.class);
            playerList.add(player);
        }

        return playerList;
    }





    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if(beanMap.get(key) != null)
                    map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }
}
