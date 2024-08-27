package com.topview.file.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.topview.file.entity.po.FileMetadata;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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


    public void uploadFile() {
        // MinIO 的访问密钥和秘密密钥
        String minioEndpoint = "http://10.21.32.237:9000"; // MinIO 的地址

        // 创建 AWS S3 客户端
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(minioEndpoint, "us-east-1")) // 区域可以设置为任意值
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withPathStyleAccessEnabled(true) // 启用路径样式访问
                .build();

        // 创建桶
        String bucketName = "test";

        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
            System.out.println("Bucket created: " + bucketName);
        } else {
            System.out.println("Bucket already exists: " + bucketName);
        }

        // 上传文件
        File file = new File("README.md"); // 本地文件路径
        s3Client.putObject(new PutObjectRequest(bucketName, "README.md", file));


        System.out.println("File uploaded: " + file.getName());
    }

    /**
     * 返回文件路径
     *
     * @param file
     * @return
     * @throws Exception
     */
    public String uploadFile(MultipartFile file, FileMetadata fileMetadata) {
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
}
