package com.example.restaurant.service;

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

        if (accessKey == null || accessKey.trim().isEmpty()) {
            throw new IllegalArgumentException("B2 access key cannot be null or empty");
        }

        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("B2 secret key cannot be null or empty");
        }

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        // Configure B2 client with S3-compatible API
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1) // Use US_EAST_1 for us-east-005 endpoint
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // Important for B2
                        .build())
                .build();

        // Initialize the S3Presigner with the same configuration
        this.s3Presigner = S3Presigner.builder()
                .region(Region.US_EAST_1) // Use US_EAST_1 for us-east-005 endpoint
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // Important for B2
                        .build())
                .build();
    }

    @PostConstruct
    public void verifyConnection() {
        try {
            // Test if we can list objects in the bucket
            log.info("Testing connection to B2 bucket: {}", bucketName);
            s3Client.listObjects(ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .maxKeys(1)
                    .build());
            log.info("Successfully connected to B2 bucket: {}", bucketName);
        } catch (S3Exception e) {
            log.error("Failed to connect to B2 bucket: {}. Error: {} ({})",
                    bucketName, e.getMessage(), e.statusCode());

            if (e.statusCode() == 403) {
                log.error("Received 403 Forbidden error. Please check your access key, secret key, and bucket permissions.");
                log.error("Make sure your application key has read and write permissions for the bucket.");
            } else if (e.statusCode() == 404) {
                log.error("Bucket not found. Check if the bucket name '{}' is correct.", bucketName);
            }
        }
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            originalFilename = "unnamed-file";
        }

        String fileName = getUniqueUploadFileName(originalFilename);
        String key = productPrefix + fileName;

        log.info("Storing file '{}' with key: {}", originalFilename, key);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("Successfully stored file: {}", fileName);
            return fileName;
        } catch (S3Exception e) {
            log.error("Failed to store file: {}. Error: {} ({})",
                    fileName, e.getMessage(), e.statusCode());
            throw new IOException("Failed to store file in B2: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            log.warn("Attempted to delete null or empty filename");
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
            log.error("Failed to delete file: {}. Error: {} ({})",
                    filename, e.getMessage(), e.statusCode());
            throw new IOException("Failed to delete file from B2: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String filename) {
        if (filename == null || filename.isEmpty()) {
            log.warn("Attempted to get URL for null or empty filename");
            return "";
        }

        String key = productPrefix + filename;
        log.info("Generating URL for file with key: {}", key);

        try {
            // Create a pre-signed URL that expires in 24 hours
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
            log.error("Failed to generate URL for file: {}. Error: {} ({})",
                    filename, e.getMessage(), e.statusCode());
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
            if (e.statusCode() == 404) {
                log.info("File does not exist: {}", filename);
            } else {
                log.error("Error checking if file exists: {}. Error: {} ({})",
                        filename, e.getMessage(), e.statusCode());
            }
            return false;
        }
    }

    private String getUniqueUploadFileName(String fileName) {
        String fileExtension = "";
        if (fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }

        // Create a unique filename to avoid collisions
        return fileName + "_" + UUID.randomUUID().toString() + fileExtension;
    }
}