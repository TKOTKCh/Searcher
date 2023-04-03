package com.searchengine.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.searchengine.common.Constants;
import com.searchengine.common.Result;
import com.searchengine.dao.UserDao;
import com.searchengine.entity.Role;
import com.searchengine.entity.TreeNode;
import com.searchengine.entity.User;
import com.searchengine.service.UserService;
import com.searchengine.utils.RedisUtil_db0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtil_db0 redisUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //判断用户名称是否存在
    @GetMapping(value = "/existUserName")
    public void existUserName(@RequestParam("username") String username) {
        boolean result = userService.checkUserName(username);
    }

    //登录
    @CrossOrigin
    @PostMapping(value = "/user/login")
    @ResponseBody
    public Result login(@RequestBody User user) {
        Map<String, String> result = userService.login(user);
        return Result.success(result);
    }

    //注册
    @PostMapping(value = "/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        if (!userService.checkUserName(user.getUsername())) {
            logger.error("用户名已存在");
            return Result.fail(Constants.REGISTER_EXIST_USERNAME,"userNameExist");
        } else {
            int register = userService.register(user);
            if (register > 0) {
                return Result.success();
            } else {
                return Result.fail();
            }
        }
    }

    @CrossOrigin
    @GetMapping(value = "/user/logout")
    @ResponseBody
    public Result logout(@RequestParam("username") String username, @RequestParam("token") String token) {
        String tokenInRedis = userService.checkToken(username);
        if (tokenInRedis != null && token.equals(tokenInRedis)) {
            redisUtil.del("login" + userService.getUserByName(username).getId());
            return Result.success();
        }
        return Result.fail();
    }

    //判断token是否还存在
    @GetMapping(value = "/survival")
    @ResponseBody
    public Result checkJJwt(@RequestParam("token") String token, @RequestParam("username") String username) {
        String tokenInRedis = userService.checkToken(username);
        if (token.equals(tokenInRedis)) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("/getFavorites")
    @ResponseBody
    public Result getFavorite(@RequestParam("username") String username) {
        return Result.success(userService.getFavorite(username));
    }

    @PostMapping("/updateTreeNodeName")
    @ResponseBody
    public Result updateTreeNodeNameById(@RequestBody Map<String, Object> params) {
        boolean b = userDao.updateTreeNodeNameById(params.get("newName").toString(), params.get("id").toString());
        return Result.success(b);
    }

    @PostMapping("/deleteTreeNode")
    @ResponseBody
    public boolean deleteTreeNodeById(@RequestBody Map<String, Object> params) {
        return userDao.deleteTreeNodeById(params.get("id").toString());
    }

    @PostMapping("/addTreeNode")
    @ResponseBody
    public boolean addTreeNode(@RequestBody Map<String, Object> params) {
        String userId = userDao.getUserIdByUsername(params.get("username").toString());
        String url = null;
        if (params.get("url") != null) {
            url = params.get("url").toString();
        }
        return userDao.addTreeNode(params.get("id").toString(),
                params.get("pid").toString(),
                params.get("name").toString(),
                params.get("isLeaf").toString().equals("1") ? true : false,
                userId,
                url);
    }
}
