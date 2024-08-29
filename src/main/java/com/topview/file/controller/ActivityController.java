package com.topview.file.controller;

import com.topview.file.common.result.CommonResult;
import com.topview.file.entity.bo.CreateActivityBo;
import com.topview.file.service.impl.ActivityServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/29
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Resource
    ActivityServiceImpl activityService;

    @PostMapping("/")
    @ApiOperation("创建活动")
    public CommonResult createActivity(CreateActivityBo createActivityBo) {
        activityService.createActivity(createActivityBo);
        return CommonResult.operateSuccess();
    }

}
