package com.topview.file.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.topview.file.entity.po.User;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/28
 */
public interface UserMapper extends BaseMapper<User> {

    default User selectByStudentId(String studentId){
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getStudentId,studentId);
        return selectOne(wrapper);
    }
}
