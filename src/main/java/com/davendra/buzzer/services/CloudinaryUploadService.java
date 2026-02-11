package com.davendra.buzzer.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CloudinaryUploadService {
    public String uploadFile(MultipartFile file) throws IOException;
    public List<String> uploadMultipleFiles(List<MultipartFile> files) throws IOException;
}
