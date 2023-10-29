package com.carspot.app.payload.response;

public class FileUploadResponse {

    private String fileId;
    private String name;
    private byte[] data;
    private String type;
    private long size;
    private String fileUploader;

    public FileUploadResponse(String fileId, String name, byte[] data, String type, long size, String fileUploader) {
        this.fileId = fileId;
        this.name = name;
        this.data = data;
        this.type = type;
        this.size = size;
        this.fileUploader = fileUploader;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileUploader() {
        return fileUploader;
    }

    public void setFileUploader(String fileUploader) {
        this.fileUploader = fileUploader;
    }

}
