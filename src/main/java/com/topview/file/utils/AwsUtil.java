package com.topview.file.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.topview.file.entity.dto.UploadFileDto;
import com.topview.file.entity.po.FileMetadata;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/26
 */
@Component
@Slf4j
public class AwsUtil {

    @Value("${aws.accessKey}")
    String accessKey;
    @Value("${aws.secretKey}")
    String secretKey;
    @Value("${aws.endpoint}")
    String endpoint;
    @Value("${aws.region}")
    String region;
    @Value("${aws.bucketName}")
    String bucketName;

    @Resource
    AmazonS3 s3Client;

    /**
     * 返回文件路径
     *
     * @param file
     * @return
     * @throws Exception
     */
    public String uploadFile(MultipartFile file, UploadFileDto fileMetadata) {
        String filename = fileMetadata.getFileName();
        String contentType = file.getContentType();

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String newFilename;
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
            //这里日志待升级,感觉info太多了?
            log.info("创建桶" + bucketName);
        } else {
            log.info("Bucket already exists: " + bucketName);
        }

        //构造文件请求
        String date = new SimpleDateFormat("yyyy-MM").format(new Date());
        //虽然是扁平化,加/能形成类似层级的文件结构视图
        newFilename = date + '/' + contentType + '/' + filename;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, newFilename, inputStream, null);
        s3Client.putObject(putObjectRequest);
        return newFilename;
    }

    public InputStream exportFile(String filePath){
        return s3Client.getObject(bucketName, filePath).getObjectContent();
    }
}
