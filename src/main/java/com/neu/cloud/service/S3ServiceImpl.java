package com.neu.cloud.service;

import com.neu.cloud.models.FileMetaData;
import com.neu.cloud.repo.FileRepository;
import com.neu.cloud.response.FileDataResponse;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class S3ServiceImpl implements S3Service {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private S3UploadService s3UploadService;


    @Override
    public ResponseEntity<FileDataResponse> uploadFilePartOne(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty");
        }

        String key = file.getOriginalFilename();
        String folderName = UUID.randomUUID().toString();
        File tempFile = new File("/tmp/" + key);

        try {
            // Transfer multipart file to local temp file
            file.transferTo(tempFile);

            // Upload to S3
            s3UploadService.uploadFile(folderName, key, tempFile);
            log.info("File uploaded successfully");

            // Save metadata to RDS
            FileMetaData metadata = new FileMetaData();
            metadata.setId(folderName);
            metadata.setUplodDate(new Date());
            metadata.setFileName(key);
            metadata.setUrl(getFilePath(folderName,key));
            fileRepository.save(metadata);
            log.info("File saved to database successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(new FileDataResponse(metadata.getId(), metadata.getUrl(), metadata.getUplodDate(),metadata.getFileName()));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Failed to upload file", e);
        } finally {
            // Clean up temp file
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }


    public String getFilePath(String folderName,String fileName) {
        return Dotenv.load().get("S3_BUCKET") + "/" + folderName+ "/" + fileName;
    }

    @Override
    public ResponseEntity<Void> deleteFileFirst(String id) throws NoResourceFoundException {
        Optional<FileMetaData> metadataOptional = fileRepository.findById(id);
        if (metadataOptional.isPresent()) {
            FileMetaData metadata = metadataOptional.get();
            // Delete from S3
            s3UploadService.deleteFile(metadata);
            log.info("File deleted successfully from s3 bucket");

            // Delete from RDS
            fileRepository.delete(metadata);
            log.info("File deleted successfully from database");

            return ResponseEntity.noContent().build();
        }
        log.warn("File not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> fetchData(@PathVariable String id) {
        Optional<FileMetaData> metadataOptional = fileRepository.findById(id);
        if (metadataOptional.isPresent()) {
            log.info("Fetching file successfully");
            FileMetaData metadata = metadataOptional.get();
            return ResponseEntity.ok(new FileDataResponse(metadata.getId(), metadata.getUrl(), metadata.getUplodDate(), metadata.getFileName()));
        }
        log.warn("No file found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
