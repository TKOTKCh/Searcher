package com.searchengine.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.searchengine.common.Result;
import com.searchengine.entity.Data;
import com.searchengine.entity.User;
import com.searchengine.service.DataService;
import com.searchengine.service.impl.DataServiceImpl;
import com.searchengine.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zjx
 */
@RestController
@RequestMapping("data")
public class DataManagementController {

    @Resource
    private DataServiceImpl dataService;

    @PostMapping("/insertOrUpdate")
    public Result insertOrUpdate(@RequestBody Data data) {

        return Result.success(dataService.saveOrUpdate(data));
    }

    @RequestMapping("/list")
    public Result listAllDatas() {
        return Result.success(dataService.list());
    }

    @GetMapping("/getById/{id}")
    public Result getByUid(@PathVariable String id) {
        return Result.success(dataService.getById(id));
    }

    @RequestMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,  //不加default 如果输入框都是“”，则会报错
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String phone,
                           @RequestParam(defaultValue = "") String address) {
        IPage<Data> page = dataService.findPage(pageNum, pageSize, username, email, phone, address);
        return Result.success(page);
    }

    //    @DeleteMapping("/{uid}")
//    public int deleteByUid(@PathVariable Integer uid) {
//        // {xx} 和 方法参数的 xx 名字要一模一样
//        return userService.deleteByUid(uid);
//    }

    @DeleteMapping("/delete/{id}")
    public Result deleteByUid(@PathVariable Integer id) {
        // {xx} 和 方法参数的 xx 名字要一模一样
        return Result.success(dataService.removeById(id));

    }

    @PostMapping("/delete/batch")
    public Result deleteByIds(@RequestBody List<Integer> ids) {
        // {xx} 和 方法参数的 xx 名字要一模一样
        return Result.success(dataService.removeBatchByIds(ids));
    }


}
