package com.topview.file.service.impl;

import com.topview.file.entity.bo.CreateActivityBo;
import com.topview.file.entity.po.Activity;
import com.topview.file.entity.po.UserActivity;
import com.topview.file.mapper.ActivityMapper;
import com.topview.file.mapper.UserActivityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/28
 */
@Service
public class ActivityServiceImpl {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private UserActivityMapper userActivityMapper;

    public void createActivity(CreateActivityBo createActivityBo){
        //创建活动
        Activity activity=CreateActivityBo.convert(createActivityBo);
        activityMapper.insert(activity);

        //用户活动关联
        UserActivity userActivity=new UserActivity();
        userActivity.setActivityId(activity.getId());
        userActivity.setUserId(createActivityBo.getUserId());
        userActivityMapper.insert(userActivity);
    }

}
