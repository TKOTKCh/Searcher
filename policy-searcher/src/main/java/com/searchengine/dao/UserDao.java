package com.searchengine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.searchengine.entity.TreeNode;
import com.searchengine.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface UserDao extends BaseMapper<User> {

    User queryOne(String username);

    int insertOne(User user);

    // 返回最高级结点的id
    List<Integer> queryAllNodesByUsername(@Param("username") String username);

    // 返回所有
    List<TreeNode> queryAll(@Param("username") String username);

    boolean updateTreeNodeNameById(@Param("newName") String newName, @Param("id") String id);

    boolean deleteTreeNodeById(@Param("id") String id);

    String getUserIdByUsername(@Param("username") String username);

    boolean addTreeNode(@Param("id") String id, @Param("pid") String pid, @Param("name") String name,
                        @Param("isLeaf") boolean isLeaf, @Param("userId") String userId, @Param("url") String url);

}
