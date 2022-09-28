package com.blog.domain.response;

public class UploadImageFailureResponse extends UploadImageResponse {
    public int error = 1;
    public String message;
    public UploadImageFailureResponse(String message){
        this.message = message;
    }
}
