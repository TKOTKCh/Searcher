package com.search.service;

import javafx.util.Pair;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


public interface SearchService {
    // 通过
    Pair<List<String>, Map<String, Object>> getDataByKeyword(String keyword, int pageSize, int pageNum);
    public Map<Double, Object> getBM25(List<String> keywords,Map<String, Object> mp);
}
