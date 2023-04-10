package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 分词实体类
 */
@Data
@AllArgsConstructor
public class Segment {
    private int id;
    private String word;
}
