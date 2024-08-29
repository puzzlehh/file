package com.topview.file.service.impl;

import com.topview.file.entity.dto.UpdatePasswordDto;
import com.topview.file.entity.dto.UserLoginDto;
import com.topview.file.entity.po.User;
import com.topview.file.entity.vo.UserLoginVo;
import com.topview.file.mapper.UserMapper;
import com.topview.file.service.UserService;
import com.topview.file.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @Description:todo:暂不开启注册功能,我手动向数据库表里插入数据
 * @Author: zbj
 * @Date: 2024/8/28
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public UserLoginVo login(UserLoginDto userLoginDto){
        User user=userMapper.selectByStudentId(userLoginDto.getStudentId());
        Assert.notNull(user,"用户不存在,请联系管理员");
        String md5Password= MD5Util.getMD5String(userLoginDto.getPassword());
        Assert.isTrue(MD5Util.checkPassword(userLoginDto.getPassword(),user.getPassword()),"密码错误");

        UserLoginVo userLoginVo=new UserLoginVo();
        userLoginVo.setId(user.getId());
        return userLoginVo;
        //todo:这里要引入rbac吗?
    }

    @Override
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {
        if(!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getNewPasswordCheck())){
            throw new IllegalArgumentException("两次密码不一致");
        }

        User user=userMapper.selectByStudentId(updatePasswordDto.getStudentId());
        Assert.isTrue(MD5Util.checkPassword(updatePasswordDto.getOriginPassword(),user.getPassword()),"原密码错误");
        user.setPassword(MD5Util.getMD5String(updatePasswordDto.getNewPassword()));
        //todo:这个的实现方式?
        userMapper.updateById(user);
    }
}
