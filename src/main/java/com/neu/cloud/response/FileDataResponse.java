package com.neu.cloud.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class FileDataResponse {
    private String id;
    private String url;
    @JsonProperty(value = "upload_date")
    private Date uplodDate;
    @JsonProperty(value = "file_name")
    private String fileName;

    public FileDataResponse(){}

    public FileDataResponse(String id, String url, Date uplodDate, String fileName) {
        this.id = id;
        this.url = url;
        this.uplodDate = uplodDate;
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUplodDate() {
        return uplodDate;
    }

    public void setUplodDate(Date uplodDate) {
        this.uplodDate = uplodDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
