package com.topview.file.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * @Description:文件的元数据
 * 此处设计表的原则是灵活运用索引,尽量非null
 * @Author: zbj
 * @Date: 2024/8/27
 */
@Data
public class FileMetadata {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //文件于minio的键
    private String filePath;

    //文件原名称
    private String fileName;

    private String fileDescription;

    private String fileType;

    @TableLogic(value = "0", delval = "1")
    private Byte deleted;
}
