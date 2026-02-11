package com.davendra.buzzer.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.EagerTransformation;
import com.cloudinary.utils.ObjectUtils;
import com.davendra.buzzer.services.CloudinaryUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class CloudinaryUploadServiceImpl implements CloudinaryUploadService {

    @Autowired
    private Cloudinary cloudinary;

    // Allowed Image Types
    private final List<String> allowedImageTypes = List.of(
            "image/jpeg",   // JPEG
            "image/png",    // PNG
            "image/gif",    // GIF
            "image/bmp",    // BMP
            "image/webp",   // WEBP
            "image/tiff",   // TIFF
            "image/svg+xml" // SVG
    );

    // Allowed Video Types
    private final List<String> allowedVideoTypes = List.of(
            "video/mp4",    // MP4
            "video/avi",    // AVI
            "video/mpeg",   // MPEG
            "video/quicktime", // MOV
            "video/x-msvideo", // AVI (alternative)
            "video/x-flv",  // FLV
            "video/3gpp",   // 3GP
            "video/ogg",    // OGG
            "video/webm",   // WEBM
            "video/x-matroska" // MKV
    );

    private static final List<String> allowedAudioTypes = Arrays.asList(
            "audio/mpeg",  // mp3
            "audio/mp3",
            "audio/wav",
            "audio/webm",  // <— from MediaRecorder API
            "audio/ogg",
            "audio/x-wav"
    );


    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.getSize() > 15 * 1024 * 1024) { // 15MB
            throw new IllegalArgumentException("File size exceeds 15MB limit");
        }

        String contentType = file.getContentType();
        Map uploadResult = new HashMap();
        if (allowedImageTypes.contains(contentType)) {
            System.out.println("This is an image");
            uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "buzzerUploads"));
        } else if (allowedVideoTypes.contains(contentType)) {
            System.out.println("This is a video");
            uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "buzzerUploads",
                            "resource_type", "video",
                            "public_id", "buzzer_video",
                            "eager", Arrays.asList(
                                    new EagerTransformation().width(300).height(300).crop("pad").audioCodec("none"),
                                    new EagerTransformation().width(160).height(100).crop("crop").gravity("south").audioCodec("none")),
                            "eager_async", true,
                            "eager_notification_url", "https://mysite.example.com/notify_endpoint"));
        } else if (allowedAudioTypes.contains(contentType) || "application/octet-stream".equals(contentType)) {
            System.out.println("This is a audio");
            uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "buzzerUploads",
                            "resource_type", "video"));
        } else {
            throw new IllegalArgumentException("Invalid file type. Only images and videos are allowed.");
        }
        return uploadResult.get("url").toString();
    }

    @Override
    public List<String> uploadMultipleFiles(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFile(file)); // Reuse single upload method
        }
        return urls; // Return list of uploaded URLs
    }
}
