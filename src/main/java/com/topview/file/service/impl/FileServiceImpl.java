package com.topview.file.service.impl;


import com.topview.file.entity.dto.ExportFileDto;
import com.topview.file.entity.dto.UploadFileDto;
import com.topview.file.entity.po.Activity;
import com.topview.file.entity.po.FileActivity;
import com.topview.file.entity.po.FileMetadata;
import com.topview.file.mapper.ActivityMapper;
import com.topview.file.mapper.FileActivityMapper;
import com.topview.file.mapper.FileMetadataMapper;
import com.topview.file.service.FileService;
import com.topview.file.utils.AwsUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/25
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private AwsUtil awsUtil;

    @Resource
    private ActivityMapper activityMapper;


    @Resource
    private FileActivityMapper fileActivityMapper;

    @Resource
    private FileMetadataMapper  fileMetadataMapper;

    /**
     * todo:检测文件上传,文件上传限制机制
     * 用户针对活动发起文件上交
     */
    @Transactional
    @Override
    public void upload(MultipartFile file, UploadFileDto uploadFileDto) {

        Activity activity=activityMapper.selectById(uploadFileDto.getActivityId());
        //learn:一点优势是防止并发问题
        Assert.notNull(activity,"活动不存在");
        Assert.isTrue(activity.getEndTime()<System.currentTimeMillis(),"活动已结束");
        Assert.isTrue(activity.getFileType().equals(file.getContentType()),"文件类型不匹配");

        //todo 将文件上传到minio桶,这里好像不能回滚,只能删除
        String filePath=awsUtil.uploadFile(file,uploadFileDto);

        //将文件信息存入数据库
        FileMetadata fileMetadata=new FileMetadata();
        fileMetadata.setFilePath(filePath);
        fileMetadata.setFileName(uploadFileDto.getFileName());
        fileMetadata.setFileType(file.getContentType());
        addFileMetadata(fileMetadata);

        //将文件和活动绑定
        FileActivity fileActivity=new FileActivity();
        fileActivity.setActivityId(uploadFileDto.getActivityId());
        fileActivity.setFileId(fileMetadata.getId());
        fileActivityMapper.insert(fileActivity);
    }


    public void addFileMetadata(FileMetadata fileMetadata){
        fileMetadataMapper.insert(fileMetadata);
    }

    /**
     * 将批量文件转成zip导出
     * @param exportFileDto
     * @return
     */
    @Override
    public InputStreamResource exportFile(ExportFileDto exportFileDto) {
        //todo:防止越权,需要知道activityId来判断活动发起人,需要鉴权系统来获取当前用户

        //获得文件路径
        List<Integer> ids=exportFileDto.getIds();
        List<FileMetadata> fileMetadataList=fileMetadataMapper.selectBatchIds(ids);
        List<String> filePaths=fileMetadataList.stream().map(FileMetadata::getFilePath).collect(Collectors.toList());

        //s3协议批量导出文件
        List<InputStream> inputStreams = filePaths.stream().map(filePath->awsUtil.exportFile(filePath)).collect(Collectors.toList());

        //将文件压缩
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        try {
            for (InputStream inputStream : inputStreams) {
                //好写法 todo:为null会报错
                zipOutputStream.putNextEntry(new ZipEntry(fileMetadataList.get(inputStreams.indexOf(inputStream)).getFileName()));
                zipOutputStream.write(inputStream.readAllBytes());
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
        }catch (IOException e){
            e.printStackTrace();
        }
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        return resource;
    }
}
