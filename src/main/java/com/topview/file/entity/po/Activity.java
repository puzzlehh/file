package com.topview.file.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * @Description:文件收集活动
 * @Author: zbj
 * @Date: 2024/8/28
 */
@Data
public class Activity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    //文件种类,限制一些手残党乱交文件
    private String fileType;

    //活动描述
    private String description;

    //关联,用于找到活动关联的所有文件id
    private Integer FileActivityId;

    //活动发起人,不想再搞关联表了
    private Integer UserId;

    //活动结束时间
    private Long endTime;

    @TableLogic(value = "0", delval = "1")
    private Byte deleted;


}
