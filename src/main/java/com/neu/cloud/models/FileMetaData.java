package com.neu.cloud.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class FileMetaData {
    @Id
    private String id;
    private String url;
    private Date uplodDate;
    private String fileName;
    }
