package com.searchengine.service;

import com.searchengine.entity.Data;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;


public interface SearchService {
    // 通过加权模型
    Map<String,Object> getDataByScore(String tableName, String keyword, int pageSize, int pageNum);
    //仅通过关键词的bm25值
    List<Data> getDataByKeyword(String tableName, String keyword, int pageSize, int pageNum);
//    List<Data> getDataByKeyword2(String tableName,String keyword,int pageSize,int pageNum);
    List<String> complete(String query);
}