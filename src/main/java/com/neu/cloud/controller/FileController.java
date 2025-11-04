package com.neu.cloud.controller;

import com.neu.cloud.response.FileDataResponse;
import com.neu.cloud.service.S3Service;
import com.neu.cloud.utils.TimedMetric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestController
@RequestMapping("/v1/file")
@Slf4j
public class FileController {

    @Autowired
    private S3Service s3Service;

    @TimedMetric("api.file.post")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDataResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Received POST request to upload file: {}", file.getOriginalFilename());
        return s3Service.uploadFilePartOne(file);
    }

    @TimedMetric("api.file.get")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getFile(@PathVariable String id) {
        log.info("Received GET request for file");
        return s3Service.fetchData(id);
    }

    @TimedMetric("api.file.delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) throws NoResourceFoundException {
        log.info("Received DELETE request for file ID: {}", id);
        return s3Service.deleteFileFirst(id);
    }
}