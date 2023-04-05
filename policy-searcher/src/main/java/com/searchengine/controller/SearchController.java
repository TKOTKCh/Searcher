package com.searchengine.controller;

import com.searchengine.common.Result;
import com.searchengine.entity.Data;
import com.searchengine.entity.Segment;
import com.searchengine.service.DataService;
import com.searchengine.service.SearchService;
import com.searchengine.service.impl.SearchServiceImpl;
import com.searchengine.utils.Trie;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("bm25")
public class SearchController {

    // 一页的所有搜索结果数量
    private final int resultNumInOnePage = 10;

    private int dataCounter=0;

    private String  lastKeyword = "";

    @Autowired
    private SearchService searchService;

    @Autowired
    private DataService dataService;

    // 通过分词的方式去搜索
    @GetMapping("/search")
    public Result searchBySegment(@Param("tableName")String tableName, @RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNum) {
        List<Data> data = searchService.getDataByKeyword(tableName, keyword, resultNumInOnePage, pageNum);
//        List<Data>data=searchService.getDataByScore(tableName, keyword, resultNumInOnePage, pageNum);
        Map<String, Object> result = new HashMap<>();

        if (this.lastKeyword.equals(keyword)) {
            this.dataCounter+=data.size();
        }
        else{
            this.dataCounter=data.size();
        }
        result.put("count", this.dataCounter);
        result.put("data", data);
        return Result.success(result);
    }

    @GetMapping("/test")
    public void test() {
        System.out.println("TEST");
    }

    @GetMapping("/completion")
    public Result complete(@RequestParam("query") String query) {
        List<String> completion = searchService.complete(query);
        return Result.success(completion);
    }

    @GetMapping("/hot")
    public Map<String, Object> getHotdata() {
        //获取当前热搜，每两小时更新一次
        double timeMillis=System.currentTimeMillis();
        if(dataService.judgeUpdate(timeMillis)==true){
            dataService.updateHotdata();
        }
        return dataService.getHotdata();
    }
}
