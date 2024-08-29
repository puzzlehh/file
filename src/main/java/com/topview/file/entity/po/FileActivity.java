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
public class FileActivity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fileId;

    private Integer activityId;

    @TableLogic(value = "0", delval = "1")
    private Byte deleted;
}

