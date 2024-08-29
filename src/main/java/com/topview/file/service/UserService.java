package com.topview.file.service;

import com.topview.file.entity.dto.UpdatePasswordDto;
import com.topview.file.entity.dto.UserLoginDto;
import com.topview.file.entity.vo.UserLoginVo;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/28
 */
public interface UserService {
    public UserLoginVo login(UserLoginDto userLoginDto);

    public void updatePassword(UpdatePasswordDto updatePasswordDto);
}
