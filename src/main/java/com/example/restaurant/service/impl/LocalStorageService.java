package com.example.restaurant.service.impl;

import com.example.restaurant.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "local", matchIfMissing = true)
public class LocalStorageService implements StorageService {

    @Value("${file.path}")
    private String filePath;

    @Override
    public String store(MultipartFile file) throws IOException {
        String fileName = getUniqueUploadFileName(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(filePath + fileName);
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());
        return fileName;
    }

    @Override
    public void delete(String filename) throws IOException {
        Path path = Paths.get(filePath + filename);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Override
    public String getFileUrl(String filename) {
        return "/upload/product/avatar/" + filename;
    }

    @Override
    public boolean exists(String filename) {
        return Files.exists(Paths.get(filePath + filename));
    }

    private String getUniqueUploadFileName(String fileName) {
        String[] splitFileName = fileName.split("\\.");
        return splitFileName[0] + System.currentTimeMillis() + "." + splitFileName[1];
    }
}