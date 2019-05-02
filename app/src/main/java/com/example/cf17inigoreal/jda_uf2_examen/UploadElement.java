package com.example.cf17inigoreal.jda_uf2_examen;

public class UploadElement {
    private String urlUpload;
    private String descIncUpload;
    private String aulaIncUpload;
    private String urlImageUpload;
    private boolean checkImageUpload;

    public UploadElement() {
    }

    public UploadElement(String urlUpload, String descIncUpload, String aulaIncUpload, String urlImageUpload, boolean checkImageUpload) {
        this.urlUpload = urlUpload;
        this.descIncUpload = descIncUpload;
        this.aulaIncUpload = aulaIncUpload;
        this.urlImageUpload = urlImageUpload;
        this.checkImageUpload = checkImageUpload;
    }

    public String getUrlUpload() {
        return urlUpload;
    }

    public void setUrlUpload(String urlUpload) {
        this.urlUpload = urlUpload;
    }

    public String getDescIncUpload() {
        return descIncUpload;
    }

    public void setDescIncUpload(String descIncUpload) {
        this.descIncUpload = descIncUpload;
    }

    public String getAulaIncUpload() {
        return aulaIncUpload;
    }

    public void setAulaIncUpload(String aulaIncUpload) {
        this.aulaIncUpload = aulaIncUpload;
    }

    public String getUrlImageUpload() {
        return urlImageUpload;
    }

    public void setUrlImageUpload(String urlImageUpload) {
        this.urlImageUpload = urlImageUpload;
    }

    public boolean isCheckImageUpload() {
        return checkImageUpload;
    }

    public void setCheckImageUpload(boolean checkImageUpload) {
        this.checkImageUpload = checkImageUpload;
    }
}
