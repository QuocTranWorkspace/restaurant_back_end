//package com.example.restaurant.controller;
//
//import com.example.restaurant.service.impl.B2StorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class ImageController {
//    @Autowired
//    private B2StorageService storageService;
//
//    @GetMapping("/api/images/{filename}")
//    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//        // Backend fetches from B2 using credentials
//        Resource imageResource = storageService.getImageAsResource(filename);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG) // Set appropriate content type
//                .body(imageResource);
//    }
//}