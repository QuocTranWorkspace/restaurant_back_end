package com.example.restaurant.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service interface for handling file storage operations
 */
public interface StorageService {

    /**
     * Store a file and return the filename
     *
     * @param file The file to store
     * @return The filename of the stored file
     * @throws IOException If an I/O error occurs
     */
    String store(MultipartFile file) throws IOException;

    /**
     * Delete a file with the given filename
     *
     * @param filename The filename of the file to delete
     * @throws IOException If an I/O error occurs
     */
    void delete(String filename) throws IOException;

    /**
     * Get a URL for accessing the file
     *
     * @param filename The filename of the file
     * @return The URL to access the file
     */
    String getFileUrl(String filename);

    /**
     * Check if a file exists
     *
     * @param filename The filename to check
     * @return true if the file exists, false otherwise
     */
    boolean exists(String filename);
}