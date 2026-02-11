package com.davendra.buzzer.controller;

import com.davendra.buzzer.services.CloudinaryUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/upload")
public class CloudinaryUploadController {
    @Autowired
    private CloudinaryUploadService cloudinaryUploadService;

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().body("No files were provided.");
        }
        return ResponseEntity.status(200).body(cloudinaryUploadService.uploadFile(multipartFile));
    }

    @PostMapping("/multiple/files")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("No files were provided.");
        }

        List<String> uploadedUrls = cloudinaryUploadService.uploadMultipleFiles(files); // Use the service method
        return ResponseEntity.ok(uploadedUrls); // Return the list of uploaded URLs
    }
}
