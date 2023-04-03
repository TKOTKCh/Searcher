package com.searchengine.dto;

import com.searchengine.entity.Data;
import com.searchengine.entity.RecordSeg;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
public class DataDto extends Data {
    private List<RecordSeg> recordSegs = new ArrayList<>();
    private double weight;
}
