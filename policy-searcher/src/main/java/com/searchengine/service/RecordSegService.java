package com.searchengine.service;

import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordSegService {

    List<Integer> queryRecordBySeg(Segment segment);

    boolean addBatch(List<RecordSeg> relations);
}
