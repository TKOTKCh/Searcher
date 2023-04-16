package com.searchengine.service;

import com.searchengine.entity.Data;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public interface SearchService {
    // 通过加权模型
    AsyncResult<Map<String, Object>> getDataByScore(String tableName, String keyword, int pageSize, int pageNum, String province, String type, String year, String uid) throws IOException;

    //仅通过关键词的bm25值
    List<Data> getDataByKeyword(String tableName, String keyword, int pageSize, int pageNum);

    //    List<Data> getDataByKeyword2(String tableName,String keyword,int pageSize,int pageNum);
    List<String> complete(String query);

//    Map<String, Object> getDataByScoreAndAddQuery(
//            String tableName, String keyword, int resultNumInOnePage, int pageNum, String province, String type, String year, String uid
//    ) throws IOException, ExecutionException, InterruptedException;
}