package com.searchengine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.searchengine.entity.TreeNode;
import com.searchengine.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao extends BaseMapper<User> {

    User queryOne(String username);


}
