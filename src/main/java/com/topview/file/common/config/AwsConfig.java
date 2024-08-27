package com.topview.file.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/27
 */
@Configuration
public class AwsConfig {
    @Value("${aws.accessKey}")
    String accessKey;
    @Value("${aws.secretKey}")
    String secretKey;
    @Value("${aws.endpoint}")
    String endpoint;
    @Value("${aws.region}")
    String region;
    @Value("${aws.bucketName}")
    String defaultBucketName;

    @Bean(name = "s3Client")
    public AmazonS3 amazonS3(){
        // 创建 AWS S3 客户端
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1")) // 区域可以设置为任意值
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withPathStyleAccessEnabled(true) // 启用路径样式访问
                .build();
        return s3Client;
    }
}
