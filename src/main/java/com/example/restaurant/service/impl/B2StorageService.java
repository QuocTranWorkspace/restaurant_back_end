package com.example.restaurant.service.impl;

import com.example.restaurant.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "b2")
public class B2StorageService implements StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${cloud.b2.bucket}")
    private String bucketName;

    @Value("${cloud.b2.product-prefix:product/avatar/}")
    private String productPrefix;

    @Value("${cloud.b2.public-url}")
    private String publicBaseUrl;

    public B2StorageService(
            @Value("${cloud.b2.access-key}") String accessKey,
            @Value("${cloud.b2.secret-key}") String secretKey,
            @Value("${cloud.b2.endpoint}") String endpoint) {

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        // Configure B2 client with S3-compatible API
        this.s3Client = S3Client.builder()
                .region(Region.US_WEST_1)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();

        // Initialize the S3Presigner with the same configuration
        this.s3Presigner = S3Presigner.builder()
                .region(Region.US_WEST_1)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        String fileName = getUniqueUploadFileName(Objects.requireNonNull(file.getOriginalFilename()));
        String key = productPrefix + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return fileName;
    }

    @Override
    public void delete(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return;
        }

        String key = productPrefix + filename;

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    @Override
    public String getFileUrl(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        String key = productPrefix + filename;

        // Create a pre-signed URL that expires in 1 hour
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // URL expires in 60 minutes
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }

    @Override
    public boolean exists(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        try {
            String key = productPrefix + filename;

            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception e) {
            return false;
        }
    }

    private String getUniqueUploadFileName(String fileName) {
        String[] splitFileName = fileName.split("\\.");
        if (splitFileName.length < 2) {
            return fileName + System.currentTimeMillis();
        }
        return splitFileName[0] + System.currentTimeMillis() + "." + splitFileName[1];
    }
}