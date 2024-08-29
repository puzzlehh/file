package com.topview.file.entity.dto;

import lombok.Data;

/**
 * @Description:这里设计更新password,我觉得不能直接放一个user上来,因为更新电话号码...密码的流程是不一样的
 * @Author: zbj
 * @Date: 2024/8/28
 */
@Data
public class UpdatePasswordDto {
    private String studentId;

    private String originPassword;

    private String newPassword;

    private String newPasswordCheck;

}
