package com.searchengine.service;

import com.searchengine.entity.Data;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;


public interface SearchService {
    // 通过
    List<Data> getDataByKeyword(String tableName, String keyword, int pageSize, int pageNum);

    List<String> complete(String query);
}