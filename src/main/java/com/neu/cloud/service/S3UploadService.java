package com.neu.cloud.service;

import com.neu.cloud.models.FileMetaData;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.File;

public interface S3UploadService {
    void uploadFile(String folderName, String key, File file);

    void deleteFile(FileMetaData fileMetaData) throws NoResourceFoundException;
}
