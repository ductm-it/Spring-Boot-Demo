package com.example.demo.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ResponseData
 */
@Getter
@Setter
@ToString
public class ResponseData<T> {

    public static <U> ResponseData<U> success(U data) {
        ResponseData<U> responseData = new ResponseData<>();
        responseData.setData(data);
        responseData.setMessages(new ArrayList<>());
        responseData.setStatusCode(HttpStatus.OK);
        return responseData;
    }

    public static <U> ResponseData<U> error(String message) {
        return error(Arrays.asList(message));
    }

    public static <U> ResponseData<U> error(List<String> messages) {
        ResponseData<U> responseData = new ResponseData<>();
        responseData.setData(null);
        responseData.setMessages(messages);
        responseData.setStatusCode(HttpStatus.BAD_REQUEST);
        return responseData;
    }

    public static <U> ResponseData<U> forbidden() {
        return forbidden("You don't have enough permission to call this function");
    }
    public static <U> ResponseData<U> forbidden(String message) {
        ResponseData<U> responseData = new ResponseData<>();
        responseData.setData(null);
        responseData.setMessages(Arrays.asList(message));
        responseData.setStatusCode(HttpStatus.FORBIDDEN);
        return responseData;
    }

    public static <U> ResponseData<U> unauthorized() {
        return forbidden("Your credential is not valid");
    }
    public static <U> ResponseData<U> unauthorized(String message) {
        ResponseData<U> responseData = new ResponseData<>();
        responseData.setData(null);
        responseData.setMessages(Arrays.asList(message));
        responseData.setStatusCode(HttpStatus.UNAUTHORIZED);
        return responseData;
    }

    public static <U> ResponseData<U> notFound() {
        return forbidden("We can not find the data math with your request");
    }
    public static <U> ResponseData<U> notFound(String message) {
        ResponseData<U> responseData = new ResponseData<>();
        responseData.setData(null);
        responseData.setMessages(Arrays.asList(message));
        responseData.setStatusCode(HttpStatus.NOT_FOUND);
        return responseData;
    }

    public static <U> ResponseData<U> from(ResponseData<?> responseData) {
        ResponseData<U> resData = new ResponseData<>();
        resData.setData(null);
        resData.setMessages(responseData.getMessages());
        resData.setStatusCode(responseData.getStatusCode());
        return resData;
    }

    private T data;
    private List<String> messages;
    @ApiModelProperty(hidden = true)
    private HttpStatus statusCode;
    
    public String getMessage() {
        return String.join("\n", this.messages);
    }

    public Boolean getStatus() {
        return this.statusCode != null ? this.statusCode.is2xxSuccessful() : false;
    }

    public ResponseData() {

    }

    public ResponseData(T data, List<String> messages, HttpStatus statusCode) {
        this.data = data;
        this.messages = messages;
        this.statusCode = statusCode;
    }

}