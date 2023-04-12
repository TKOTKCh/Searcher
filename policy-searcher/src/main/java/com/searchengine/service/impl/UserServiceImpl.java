package com.searchengine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.searchengine.dao.StatisticDao;
import com.searchengine.dao.UserDao;
import com.searchengine.entity.LoginUser;
import com.searchengine.entity.StatisticHistory;
import com.searchengine.entity.TreeNode;
import com.searchengine.entity.User;
import com.searchengine.service.UserService;
import com.searchengine.utils.JwtUtil;
import com.searchengine.utils.RedisUtil_db0;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.annotation.Inherited;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private StatisticDao statisticDao;

    @Autowired
    private RedisUtil_db0 redisUtil;

    @Override
    public String checkToken(String username) {
        User user = userDao.queryOne(username);
        String token = (String) redisUtil.get("login" + user.getId());
        return token;
    }

    @Override
    public boolean checkUserName(String username) {
        User user = userDao.queryOne(username);
        return user == null;
    }


    @Override
    public int register(User user) {
//        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        statisticService.addUserCount();
        return userDao.insertOne(user);
    }

    @Override
    public Map<String, String> login(User user) {
        Map<String, String> rs = new HashMap<>();
        //用户认证
        LoginUser userDetails = (LoginUser) userDetailsService.loadUserByUsername(user.getUsername());
        //判断认证是否通过
        if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            //认证通过
            //使用userId生成一个jwt,存到redis中
            User detailsUser = userDetails.getUser();
            String id = detailsUser.getId();
            String jwt = JwtUtil.createJWT("ourTeam", 1000 * 60 * 60 * 6, id);//6小时

            rs.put("token", jwt);
            rs.put("username", detailsUser.getUsername());
            rs.put("id", id);
            rs.put("message", "success");

//            redisUtil.set("login" + detailsUser.getId(), jwt, 60 * 60 * 6); //6小时
            return rs;
        } else {
            rs.put("message", "failure");
            return rs;
        }
    }

    @Override
    public User getUserByName(String username) {
        return userDao.queryOne(username);
    }

    @Override
    public List<TreeNode> getFavorite(String username) {
        return userDao.queryAll(username);
    }

    @Override
    public IPage<User> findPage(Integer pageNum, Integer pageSize, String username, String email, String phone, String address) {
        IPage<User> page = new Page<>(pageNum, pageSize);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        queryWrapper.orderByDesc("uid");
        IPage<User> userPage = this.page(page, queryWrapper);
        return userPage;
    }

    public User getByUid(String id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User one = getOne(queryWrapper);
        return one;
    }

    @Override
    public List<String> getUserQuery(Integer userId){
        return userDao.getUserQuery(userId);
    }
    @Override
    public void addUserQuery(Integer userId,String query,double time){
//        int num=userDao.getUserQueryCount(userId);
        List<String>querys=getUserQuery(userId);
        for(String q:querys){
            if(q.equals(query)){
                return;
            }
        }
        if(querys.size()>=5){
            userDao.updateUserQuery(userId,query,time);
        }else{
            userDao.addUserQuery(userId,query,time);
        }
    }

    @Override
    public String  adminLogin(User user) {
        Map<String, String> rs = new HashMap<>();
        //用户认证
        LoginUser userDetails = (LoginUser) userDetailsService.loadAdminByUsername(user.getUsername());
        //判断认证是否通过
        if (user.getPassword().equals(userDetails.getPassword()) ) {
            //认证通过
            //使用userId生成一个jwt,存到redis中
            System.out.println("passwordok");
            return "admin login successfully";
//            redisUtil.set("login" + detailsUser.getId(), jwt, 60 * 60 * 6); //6小时
        } else {
            return "login fail";
        }
    }

    public boolean removeUserById(Integer id) {
        try {
            this.removeById(id);
            statisticService.subOneUserCount();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}