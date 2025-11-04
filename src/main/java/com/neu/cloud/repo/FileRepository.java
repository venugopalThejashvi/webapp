package com.neu.cloud.repo;

import com.neu.cloud.models.FileMetaData;
import com.neu.cloud.utils.TimedMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileMetaData, String> {
    @TimedMetric("database.file.add")
    @Override
    <S extends FileMetaData> S save(S entity);

    @TimedMetric("database.file.delete")
    @Override
    void delete(FileMetaData entity);

    @TimedMetric("database.file.find")
    @Override
    Optional<FileMetaData> findById(String s);
}
