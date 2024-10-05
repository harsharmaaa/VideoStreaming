package com.testreact.reacttest.service;

import com.testreact.reacttest.model.Video;
import com.testreact.reacttest.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;

@Service
public class VideoService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final Path fileStorageLocation;
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.videoRepository = videoRepository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory!");
        }
    }

    public Video storeVideo(MultipartFile file, String title) {
        String fileName = file.getOriginalFilename();
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Video video = new Video();
            video.setTitle(title);
            video.setFileName(fileName);
            return videoRepository.save(video);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName);
        }
    }

    public Resource loadVideoAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    public List<Video> getAllVideos() {
        // TODO Auto-generated method stub
        return videoRepository.findAll();
    }
}
