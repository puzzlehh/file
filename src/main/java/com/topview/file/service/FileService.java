package com.topview.file.service;

import com.topview.file.entity.dto.ExportFileDto;
import com.topview.file.entity.dto.UploadFileDto;
import com.topview.file.entity.po.FileMetadata;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/25
 */
public interface FileService {

    public void upload(MultipartFile file, UploadFileDto fileMetadata);

    public void addFileMetadata(FileMetadata fileMetadata);

    public InputStreamResource exportFile(ExportFileDto exportFileDto);

}
