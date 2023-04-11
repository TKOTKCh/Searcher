package com.searchengine.entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
// 除了正文的所有信息对应datanobody表
public class DataNoBody {
    private int id;
    private String word;
}
