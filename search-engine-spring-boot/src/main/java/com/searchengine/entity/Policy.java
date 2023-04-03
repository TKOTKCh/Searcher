package com.searchengine.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "policy") // 一般mybatis plus会根据 类名 找 表名
@ToString
public class Policy {
    private String id;
    private String title;
    private String grade;
    private String pubAgencyId;
    private String pubAgency;
    private String pubAgencyFullName;
    private String pubNumber;
    private String pubTime;
    private String policyType;
    private String policyBody;
    private String province;
    private String city;
    private String policySource;
    private String updateDate;

}
