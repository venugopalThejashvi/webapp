package com.neu.cloud.service;

import com.neu.cloud.models.FileMetaData;
import com.neu.cloud.repo.FileRepository;
import com.neu.cloud.utils.TimedMetric;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
@Slf4j
public class S3UploadServiceImpl implements S3UploadService {

    private final S3Client s3Client;
    private final FileRepository fileRepository;

    public S3UploadServiceImpl(FileRepository fileRepository) {
        InstanceProfileCredentialsProvider provider = InstanceProfileCredentialsProvider.create();
        this.s3Client = S3Client
                .builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(provider)
                .build();
        this.fileRepository = fileRepository;
    }

    @Override
    @TimedMetric("aws.s3.add")
    public void uploadFile(String folderName, String key, File file) {
        log.info("Uploading file {}", key);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(Dotenv.load().get("S3_BUCKET"))
                .key(folderName+ "/" + key)
                .build();
        s3Client.putObject(request, file.toPath());
    }

    @Override
    @TimedMetric("aws.s3.delete")
    public void deleteFile(FileMetaData fileMetaData) throws NoResourceFoundException {
        log.info("Request to delete file in aws");
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(Dotenv.load().get("S3_BUCKET"))
                .key(fileMetaData.getId()+"/"+fileMetaData.getFileName())
                .build();
        s3Client.deleteObject(request);
    }
}
