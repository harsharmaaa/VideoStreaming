package com.testreact.reacttest.Controller;

import com.testreact.reacttest.model.Video;
import com.testreact.reacttest.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public Video uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("title") String title) {
        return videoService.storeVideo(file, title);
    }

    @GetMapping("/all")
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/stream/{fileName}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String fileName) {
        Resource resource = videoService.loadVideoAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
