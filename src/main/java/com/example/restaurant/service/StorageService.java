package com.example.restaurant.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface StorageService {
    String store(MultipartFile file) throws IOException;
    void delete(String filename) throws IOException;
    String getFileUrl(String filename);
    boolean exists(String filename);
}