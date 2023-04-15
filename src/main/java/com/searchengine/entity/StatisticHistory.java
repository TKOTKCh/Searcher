package com.searchengine.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "statistic") // 一般mybatis plus会根据 类名 找 表名
@ToString
public class StatisticHistory implements Serializable {

    String key;
    String value;

}
