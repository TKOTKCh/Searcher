package com.searchengine.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.searchengine.common.Result;
import com.searchengine.entity.User;
import com.searchengine.service.UserService;
import com.searchengine.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zjx
 */
@RestController
@RequestMapping("user")
public class UserManagementController {

    @Resource
    private UserServiceImpl userService;
    @PostMapping("/insertOrUpdate")
    public Result insertOrUpdate(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user)) ;
    }

//    @RequestMapping("/list")
//    public List<User> listAllUsers(){
//        return userService.listUsers();
//    }

    @RequestMapping("/list")
    public List<User> listAllUsers() {
        return userService.list();
    }

    @GetMapping("/getByUid/{uid}")
    public Result getByUid(@PathVariable String uid) {
        return Result.success(userService.getByUid(uid));
    }

    @RequestMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,  //不加default 如果输入框都是“”，则会报错
                                @RequestParam(defaultValue = "") String email,
                                @RequestParam(defaultValue = "") String phone,
                                @RequestParam(defaultValue = "") String address) {
        return userService.findPage(pageNum, pageSize, username, email, phone, address);
    }

    //    @DeleteMapping("/{uid}")
//    public int deleteByUid(@PathVariable Integer uid) {
//        // {xx} 和 方法参数的 xx 名字要一模一样
//        return userService.deleteByUid(uid);
//    }

    @DeleteMapping("/delete/{id}")
    public Result deleteByUid(@PathVariable Integer id) {

        return Result.success(userService.removeUserById(id)) ;

    }

    @PostMapping("/delete/batch")
    public Result deleteByUids(@RequestBody List<Integer> ids) {
        // {xx} 和 方法参数的 xx 名字要一模一样
        return Result.success(userService.removeBatchByIds(ids)) ;
    }


}
