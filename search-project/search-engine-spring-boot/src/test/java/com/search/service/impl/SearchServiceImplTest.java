package com.search.service.impl;

import com.search.BM25.BM25;
import com.search.entity.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.search.BM25.BM25Test.loadIDFMap;
import static org.junit.jupiter.api.Assertions.*;

import com.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;


@SpringBootTest
class SearchServiceImplTest {
    @Autowired
    private SearchService searchService;

    String keyword = "知识帮助";

    @Test
    public void test(){
        int pageNum =1;
        Map<String, Object> mp = new HashMap<>();

        System.out.println(keyword);
        mp = searchService.getDataByKeyword(keyword,10,1);
        System.out.println(mp);

        List<Data> datas = (List<Data>) mp.get("result");
        System.out.println(datas.get(0));
        System.out.println(datas.get(0).getUrl());
        System.out.println(datas.get(1));
        System.out.println(datas.size());

        getBM25Rank(mp);
    }

    public Map<String, Object> getBM25Rank(Map<String, Object> mp){
        BM25 bm25= new BM25(1.2,0.75);
        Map<String, Object> map = new HashMap<>();
        List<Data> datas = (List<Data>) mp.get("result");

        List<String> keywords = new ArrayList<String>();
        String keyword1 = "知识";
        String keyword2 = "帮助";
        keywords.add(keyword1);
        keywords.add(keyword2);

        List<String> docs = new ArrayList<String>();
        for (int i=0; i<datas.size(); i++){
            docs.add(datas.get(i).getCaption());
        }
        Map<String,Double> idfMap = new HashMap<String,Double>();
        loadIDFMap(idfMap, this.getClass().getResourceAsStream("/jieba/idf_dict.txt"));

        List<Double> scores = new ArrayList<Double>();
        int i = 0;
        for(String doc:docs){
            i++;
            double score = 0;
            for (String keyword :keywords){
                System.out.println("第"+ i +"个值关于关键词"+keyword+"的bm25计算值是:"+bm25.cal(keyword, doc, docs, idfMap));
                score += bm25.cal(keyword, doc, docs, idfMap);
            }
            scores.add(score);
        }

        System.out.println("分数为:"+scores);
        return map;
    }
}
