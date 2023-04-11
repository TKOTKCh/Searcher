package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
// Data表的原始Java对象
public class Data implements Comparable<Data>{
    private Integer id;
    private Integer policyId;
    private String policyTitle;
    private String policyGrade;
    private String pubAgencyId;
    private String pubAgency;
    private String pubAgencyFullname;
    private String pubNumber;
    private String pubTime;
    private String policyType;
    private String policyBody;
    private String province;
    private String city;
    private String policySource;

    private Integer count=0;//政策在对应搜索词中匹配到的关键词个数
    private double bm25=0;//政策在对应搜索词中的bm25值
    private double score=0.0;

    @Override
    public int compareTo(Data o)
    {
        return this.score>o.score?-1:(this.score==o.score?0:1);
    }
}
