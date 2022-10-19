package com.blog.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private int status;
    private Long total;
    private T data;

    public  Result(int status, Long total, T data){
        this.status = status;
        this.total = total;
        this.data = data;
    }
}
