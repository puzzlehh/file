package com.topview.file.entity.bo;

import com.topview.file.entity.po.Activity;
import lombok.Data;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/28
 */
@Data
public class CreateActivityBo {

    private String activityDescription;

    private String activityName;

    //发起人id
    private Integer UserId;

    //活动开始时间
    //private Long beginTime;

    //活动结束时间
    private Long endTime;

    public static Activity convert(CreateActivityBo createActivityBo){
        Activity activity = new Activity();
        activity.setDescription(createActivityBo.getActivityDescription());
        activity.setEndTime(createActivityBo.getEndTime());
        activity.setName(createActivityBo.getActivityName());
        return activity;
    }

}
