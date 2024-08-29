package com.topview.file.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.topview.file.entity.po.FileMetadata;

import java.util.Collection;
import java.util.List;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/27
 */
public interface FileMetadataMapper extends BaseMapper<FileMetadata> {

    /**
     * 小细节,用啥查啥
     * @param id
     * @return
     */
    default String getFilePathById(Integer id){
        LambdaQueryWrapper<FileMetadata> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileMetadata::getId,id).select(FileMetadata::getFilePath);
        return selectOne(wrapper).getFilePath();
    }

    /**
     * 返回路径集合,小细节使用流
     * @param ids
     * @return
     */
    default List<String> getFilePathByIds(List<Integer> ids){
        LambdaQueryWrapper<FileMetadata> wrapper=new LambdaQueryWrapper<>();
        //todo:学习一下,这里最好进行空参校验,不然会报server error
        wrapper.in(FileMetadata::getId,ids).select(FileMetadata::getFilePath);
        return selectList(wrapper).stream().map(FileMetadata::getFilePath).toList();
    }




}
