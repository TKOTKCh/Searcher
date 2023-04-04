package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
// Data表的原始Java对象
public class Data {
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
}
