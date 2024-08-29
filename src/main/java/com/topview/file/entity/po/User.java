package com.topview.file.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/28
 */
@Data
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableLogic(value = "0", delval = "1")
    private Byte deleted;

    private String username;

    private String password;

    //泥工学号,唯一绑定,登录凭证,需要索引
    private String studentId;
}
