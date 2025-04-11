package com.example.restaurant.service.impl;

import com.example.restaurant.service.StorageService;
import lombok.extern.slf4j.Slf4j;
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
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "b2")
public class B2StorageService implements StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${cloud.b2.bucket}")
    private String bucketName;

    @Value("${cloud.b2.product-prefix:product/avatar/}")
    private String productPrefix;

    public B2StorageService(
            @Value("${cloud.b2.access-key}") String accessKey,
            @Value("${cloud.b2.secret-key}") String secretKey,
            @Value("${cloud.b2.endpoint}") String endpoint) {

        log.info("Initializing B2StorageService with endpoint: {}", endpoint);

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        // Configure B2 client with S3-compatible API
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1) // Changed to US_EAST_1 to match your bucket region
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();

        // Initialize the S3Presigner with the same configuration
        this.s3Presigner = S3Presigner.builder()
                .region(Region.US_EAST_1) // Changed to US_EAST_1 to match your bucket region
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @PostConstruct
    public void verifyConnection() {
        try {
            // Test if we can list objects in the bucket
            s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).maxKeys(1).build());
            log.info("Successfully connected to B2 bucket: {}", bucketName);
        } catch (S3Exception e) {
            log.error("Failed to connect to B2 bucket: {}. Error: {}", bucketName, e.getMessage());
            if (e.statusCode() == 403) {
                log.error("Received 403 Forbidden error. Please check your access key, secret key, and bucket permissions.");
            }
        }
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String fileName = getUniqueUploadFileName(Objects.requireNonNull(file.getOriginalFilename()));
        String key = productPrefix + fileName;

        log.info("Storing file with key: {}", key);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("Successfully stored file: {}", fileName);
            return fileName;
        } catch (S3Exception e) {
            log.error("Failed to store file: {}. Error: {}", fileName, e.getMessage());
            throw new IOException("Failed to store file in B2: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return;
        }

        String key = productPrefix + filename;
        log.info("Deleting file with key: {}", key);

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Successfully deleted file: {}", filename);
        } catch (S3Exception e) {
            log.error("Failed to delete file: {}. Error: {}", filename, e.getMessage());
            throw new IOException("Failed to delete file from B2: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        String key = productPrefix + filename;
        log.info("Generating URL for file with key: {}", key);

        try {
            // Create a pre-signed URL that expires in 1 hour
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(24)) // URL expires in 24 hours
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();
            log.info("Generated URL for file: {}", filename);
            return url;
        } catch (S3Exception e) {
            log.error("Failed to generate URL for file: {}. Error: {}", filename, e.getMessage());
            return "";
        }
    }

    @Override
    public boolean exists(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        String key = productPrefix + filename;
        log.info("Checking if file exists with key: {}", key);

        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);
            log.info("File exists: {}", filename);
            return true;
        } catch (S3Exception e) {
            log.info("File does not exist: {}. Error: {}", filename, e.getMessage());
            return false;
        }
    }

    private String getUniqueUploadFileName(String fileName) {
        String fileExtension = "";
        if (fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }

        return fileName + "_" + UUID.randomUUID().toString() + fileExtension;
    }
}