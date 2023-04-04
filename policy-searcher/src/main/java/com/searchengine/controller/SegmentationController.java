package com.searchengine.controller;

import com.searchengine.entity.Segment;
import com.searchengine.service.SegmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分词操作
 */
@RestController
@RequestMapping("/seg")
@Slf4j
public class SegmentationController {


    @Autowired
    private SegmentService segmentService;

    @GetMapping("/getAll")
    public List<Segment> getAllSeg(){
        log.info("查询所有分词");
        List<Segment> segments = segmentService.queryAllSeg();
        return segments;
    }
}
