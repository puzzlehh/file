package com.topview.file;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/26
 */
@SpringBootTest
public class AwsTest {

    String accessKey = "6y6fVNpkWHU02abADfiE";
    String secretKey = "en7A4RUfggNONrJ6Ho8F9jsiWNrxBqk9i2tp8e4x";
    String endpoint="http://10.21.32.237:9000";

    String region="us-east-2";

    @Test
    public void testAi() {
        // MinIO 的访问密钥和秘密密钥

        String minioEndpoint = "http://10.21.32.237:9000"; // MinIO 的地址

        // 创建 AWS S3 客户端
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(minioEndpoint, "us-east-1")) // 区域可以设置为任意值
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .withPathStyleAccessEnabled(true) // 启用路径样式访问
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

    @Test
    public void testAws() {

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration baseOpts = new ClientConfiguration();
        //
        baseOpts.setSignerOverride("S3SignerType");
        baseOpts.setProtocol(Protocol.HTTPS);
        //
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(hostName, region))  // 如果有endpoint，可以用这个，这个和withRegion(Region)不能一起使用
//                .withPathStyleAccessEnabled(true)  // 如果配置了S3域名，就需要加这个进行路径访问，要不然会报AccessKey不存在的问题
                .withClientConfiguration(baseOpts)
                .build();

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

    @Test
    public void test(){

        // 创建连接
        ClientConfiguration config = new ClientConfiguration();

        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(endpoint, Regions.CN_NORTH_1.getName());

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        AmazonS3 s3Client = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(config)
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(true)
                .build();

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

    @Test
    public void testMinio() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();

        File file=new File("README.md");
        String objectName = UUID.randomUUID().toString();

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        minioClient.putObject(PutObjectArgs.builder().bucket("test").object(objectName).stream(inputStream, -1, 1*1024*1024*1024).build());

        System.out.println("File uploaded: " + file.getName());



        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket("test")
                .object("testhh")
                //这里的context Type需要image/jpeg的类似形式而不是单纯jpeg
                .contentType("md")
                .stream(inputStream, inputStream.available(), -1)
                .build();
    }


}
