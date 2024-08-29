package com.topview.file.entity.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/27
 */
@Data
public class UploadFileDto {
    private String fileName;

    private String fileDescription;

    //活动id
    private Integer activityId;


}
