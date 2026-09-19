package com.example.taskmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GenericResponse<T> {
    private Integer code;
    private String status;
    private String message;
    private T data;

    public GenericResponse(T data){
        this.code = 200;
        this.status = "Success";
        this.message = "OK";
        this.data = data;
    }
}
