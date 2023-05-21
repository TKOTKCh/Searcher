package com.searchengine.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.searchengine.common.Result;
import com.searchengine.entity.Data;
import com.searchengine.entity.User;
import com.searchengine.service.DataService;
import com.searchengine.service.impl.DataServiceImpl;
import com.searchengine.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                           @RequestParam Integer pageSize
                           ) {
        IPage<Data> page = dataService.findPage(pageNum, pageSize);
        return Result.success(page);
    }

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

    @RequestMapping("/addData")
    public Result addData(@RequestParam Integer policyId,
                          @RequestParam String policyTitle,
                          @RequestParam String policyGrade,
                          @RequestParam String pubAgencyId,
                          @RequestParam String pubAgency,
                          @RequestParam String pubAgencyFullname,
                          @RequestParam String pubNumber,
                          @RequestParam String pubTime,
                          @RequestParam String policyType,
                          @RequestParam String policyBody,
                          @RequestParam String province,
                          @RequestParam String city,
                          @RequestParam String policySource,
                          @RequestParam Integer pubTimeYear
    ) {
        dataService.addData(policyId,
                policyTitle,
                policyGrade,
                pubAgencyId,
                pubAgency,
                pubAgencyFullname,
                pubNumber,
                pubTime,
                policyType,
                policyBody,
                province,
                city,
                policySource,
                pubTimeYear);
        return Result.success();
    }
    @RequestMapping("/addDataByFile")
    public Result addDataByFile(@RequestParam("fileObj") MultipartFile formData) {
        dataService.addDataByFile(formData);
        System.out.println(formData.getOriginalFilename());
        return Result.success();
    }
}
