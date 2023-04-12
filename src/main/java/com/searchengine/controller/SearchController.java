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
import org.springframework.web.bind.annotation.*;

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
//        List<Data> data = searchService.getDataByKeyword(tableName, keyword, resultNumInOnePage, pageNum);
        Map<String ,Object> dataByScore = searchService.getDataByScore(tableName, keyword, resultNumInOnePage, pageNum,null,null,null,null,null);

        return Result.success(dataByScore);
    }

    @GetMapping("/search_condition")
    public Result searchBySegmentAndConditions(
            @Param("tableName")String tableName,
            @RequestParam("keyword") String keyword,
            @RequestParam("pageNum") int pageNum,
            @RequestParam("province") String province,
            @RequestParam("type") String type,
            @RequestParam("year") String year
    ) {
        System.out.println("here");
//        List<Data> data = searchService.getDataByKeyword(tableName, keyword, resultNumInOnePage, pageNum);
        Map<String ,Object> dataByScore = searchService.getDataByScore(tableName, keyword, resultNumInOnePage, pageNum,province,type,year,"天津","教育");

        return Result.success(dataByScore);
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
    public Result getHotdata() {
        //获取当前热搜，每两小时更新一次
        double time=System.currentTimeMillis()/1000;
        if(dataService.judgeUpdate(time)==true){
            dataService.updateHotdata();
        }
        return Result.success( dataService.getHotdata());
    }

    @PostMapping("/count")
    public Result addCount(@RequestParam("id") Integer  id) {
        return Result.success( dataService.addCount(id));
    }
}
