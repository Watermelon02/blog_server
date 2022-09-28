package com.blog.domain.response;

import lombok.Data;

@Data
public class Result<T>{
    private int status;
    private Long total;
    private T data;

    public  Result(int status, Long total, T data){
        this.status = status;
        this.total = total;
        this.data = data;
    }
}
