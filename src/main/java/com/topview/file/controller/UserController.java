package com.topview.file.controller;

import com.topview.file.common.result.CommonResult;
import com.topview.file.entity.dto.UpdatePasswordDto;
import com.topview.file.entity.dto.UserLoginDto;
import com.topview.file.entity.vo.UserLoginVo;
import com.topview.file.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/28
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public CommonResult<UserLoginVo> login(@RequestBody UserLoginDto userLoginDto){
        return CommonResult.autoResult(true,userService.login(userLoginDto));
    }

    @PatchMapping
    public CommonResult updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto){
        userService.updatePassword(updatePasswordDto);
        return CommonResult.operateSuccess();
    }

}
