package com.searchengine.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.searchengine.entity.TreeNode;
import com.searchengine.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService  {

    String checkToken(String id);

    boolean checkUserName(String username);

    int register(User user);

    Map<String, String> login(User user);



    User getUserByName(String username);

    List<TreeNode> getFavorite(String username);

    IPage<User> findPage(Integer pageNum, Integer pageSize, String username, String email, String phone, String address);

    //根据用户id得到用户最近的五条搜索记录
    List<String> getUserQuery(Integer userid);
    //添加用户的搜索记录，其中如果超过五条就修改，小于五条就插入
    void addUserQuery(Integer userid,String query,double time);

    String adminLogin(User user);
}
