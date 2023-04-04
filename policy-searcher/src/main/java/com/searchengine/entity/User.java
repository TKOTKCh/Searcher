package com.searchengine.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user") // 一般mybatis plus会根据 类名 找 表名
@ToString
public class User {
    //自增
    //主键
    @TableId
    private String id;

    private String username;
    private String password;

    private String email;
    private String phone;
    private String address;
    private String career;
    private Integer age;
    private String sex;
    //数据库中 兴趣字段 每个兴趣用#隔开
    private String interests;

    private String token;

    //权限
    //权限默认为普通用户
    private Role role = Role.DEFAULT;
}
