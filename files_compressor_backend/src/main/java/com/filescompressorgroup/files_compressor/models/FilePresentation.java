package com.filescompressorgroup.files_compressor.models;

public class FilePresentation {
    private String fileName;
    private Double size;

    public FilePresentation(String fileName, Double size) {
        this.fileName = fileName;
        this.size = size;
    }

    public FilePresentation() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }  
}
