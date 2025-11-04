package com.neu.cloud.service;

import com.neu.cloud.response.FileDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;

public interface S3Service {
    ResponseEntity<FileDataResponse> uploadFilePartOne(MultipartFile file);

    ResponseEntity<Void> deleteFileFirst(String id) throws NoResourceFoundException;

    ResponseEntity<Object> fetchData(String id);
}
