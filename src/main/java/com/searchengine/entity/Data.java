package com.searchengine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private String pubTimeYear;

    @TableField(exist = false)
    private Integer count=0;//政策在对应搜索词中匹配到的关键词个数
    @TableField(exist = false)
    private double bm25=0;//政策在对应搜索词中的bm25值
    @TableField(exist = false)
    private double score=0.0;

    public static Map<String,Integer>hasht=new LinkedHashMap<String,Integer>() {{
        put("国家级", 3);
        put("省级", 2);
        put("市级", 1);
        put("区县级", 0);
    }};;
    @Override
    public int compareTo(Data o)
    {
        if(this.count==o.count){
//            if(this.bm25==o.bm25){
                String[]list1=this.pubTime.split("/");
                String[]list2=o.pubTime.split("/");
                for(int i=0;i<list1.length;i++){
                    int temp1=Integer.parseInt(list1[i]);
                    int temp2=Integer.parseInt(list2[i]);
                    if(temp1>temp2){
                        return -1;
                    }
                    if(temp1<temp2){
                        return 1;
                    }
                }
                int level1=hasht.get(this.policyGrade);
                int level2=hasht.get(o.policyGrade);
                if(level1==level2){
                    return 0;
                }else{
                    return level1>level2?-1:1;
                }
//            }else{
//                return this.bm25>o.bm25?-1:1;
//            }
        }else{
            return this.count>o.count?-1:1;
        }

    }
}
