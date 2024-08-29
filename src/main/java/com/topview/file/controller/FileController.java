package com.topview.file.controller;

import com.alibaba.fastjson.JSON;
import com.topview.file.common.result.CommonResult;
import com.topview.file.entity.dto.ExportFileDto;
import com.topview.file.entity.dto.UploadFileDto;
import com.topview.file.entity.po.FileMetadata;
import com.topview.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/25
 */
@RestController
@RequestMapping("/api/file")
@Api(tags = "文件上传")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public CommonResult<Integer> upload(@ApiParam(value = "需要上传的文件")
                                        @RequestPart("multipartFile") MultipartFile multipartFile,
                                        @ApiParam(value = "需要传入文件描述fileDescribe和文件名fileName,并转成json字符串发送")
                                        @RequestPart("fileMetadata") UploadFileDto fileMetadata) {
        fileService.upload(multipartFile, fileMetadata);
        return CommonResult.operateSuccess();
    }

    /**
     * 导出文件,每个用户应该有自己的?文件
     * 的路径,文件的显示应该是数据库的活,文件的导出就用数据库的数据来找到Minio的数据来实现导出
     * @return
     */
    @PostMapping("/export")
    public CommonResult<InputStreamResource> exportFiles(@RequestBody ExportFileDto exportFileDto, HttpServletResponse response){
        response.setHeader("Content-Disposition", "attachment;filename=file.zip");
        response.setCharacterEncoding("UTF-8");
        try {
            StreamUtils.copy(fileService.exportFile(exportFileDto).getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return CommonResult.operateSuccess();
    }

}
