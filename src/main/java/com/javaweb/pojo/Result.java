package com.javaweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private boolean success;
    private String message;
    private Object resultData;

    public static Result success(){
        return new Result(true,"success",null);
    }
    public static Result success(Object resultData){
        return new Result(true,"success",resultData);
    }
    public static Result error(String message){
        return new Result(false,message,null);
    }
    public static Result error(String message,Object resultData){
        return new Result(false,message,resultData);
    }
}
