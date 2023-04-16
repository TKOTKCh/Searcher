package com.searchengine.controller;

import com.searchengine.common.Result;
import com.searchengine.dao.DataDao;
import com.searchengine.entity.Data;
import com.searchengine.entity.Segment;
import com.searchengine.service.DataService;
import com.searchengine.service.SearchService;
import com.searchengine.service.UserService;
import com.searchengine.service.impl.SearchServiceImpl;
import com.searchengine.service.impl.StatisticService;
import com.searchengine.utils.RedisUtil_db0;
import com.searchengine.utils.Trie;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private UserService userService;
    @Autowired
    private DataDao dataDao;
    @Autowired
    private RedisUtil_db0 redisUtil;

    // 通过分词的方式去搜索
//    @GetMapping("/search")
//    public Result searchBySegment(@Param("tableName")String tableName, @RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNum) throws IOException {
////        List<Data> data = searchService.getDataByKeyword(tableName, keyword, resultNumInOnePage, pageNum);
//        Map<String ,Object> dataByScore = searchService.getDataByScore(tableName, keyword, resultNumInOnePage, pageNum,null,null,null,null,null);
//
//        return Result.success(dataByScore);
//    }
//    @RequestParam("position")String position,

//    @RequestParam("profession")String profession
    @GetMapping("/search_condition")
    @Async
    public Result searchBySegmentAndConditions(
            @Param("tableName")String tableName,
            @RequestParam("keyword") String keyword,
            @RequestParam("pageNum") int pageNum,
            @RequestParam("province") String province,
            @RequestParam("type") String type,
            @RequestParam("year") String year,
            @RequestParam("uid") String uid
    ) throws IOException, ExecutionException, InterruptedException {

        if (tableName == null || "".equals(tableName)) {
            tableName = "datatitle";
        }


        Future<Map<String, Object>> task = searchService.getDataByScore(
                tableName, keyword, resultNumInOnePage, pageNum,province,type,year,uid
        );
//                tableName, content, pageSize,  pageNum, province, type, year, id
//        );
//        List<Data> data = searchService.getDataByKeyword(tableName, keyword, resultNumInOnePage, pageNum);


        while(true) {
            if(task.isDone()) {
                // 三个任务都调用完成，退出循环等待
                break;
            }
        }
        return Result.success(task.get());
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
        try {
            //获取当前热搜，每两小时更新一次
            double time = System.currentTimeMillis() / 1000;
            if (dataService.judgeUpdate(time) == true) {
                dataService.updateHotdata();
                List<Data> hotData = dataDao.getHotdata();
                redisUtil.set("hot", hotData);
                return Result.success(hotData);
            }

            return Result.success(statisticService.getHotData());
        } catch (Exception e) {
            return Result.fail();
        }

    }

    @PostMapping("/count")
    public Result addCount(@RequestParam("id") Integer  id) {
        return Result.success( dataService.addCount(id));
    }


}
