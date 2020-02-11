package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.MappedSuperclass;

import com.example.demo.Core.ResponseData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * StackInfo
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
class StackInfo {

    private String methodName;
    private String className;
    private String fileName;
    private Integer lineNumber;

}

/**
 * ErrorInfo
 */
@Getter
@Setter
@ToString
class ErrorInfo {
    String message = "";
    List<StackInfo> stackInfos = new ArrayList<>();
}

/**
 * ErrorController
 */
@MappedSuperclass
public class ErrorController {

    protected <U> ResponseEntity<ResponseData<U>> ResponseDataEntity(ResponseData<U> responseData) {
        return new ResponseEntity<>(responseData, responseData.getStatusCode());
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ResponseData<Object>> exceptionHandler(final Exception ex) {

        List<ErrorInfo> errorInfos = new ArrayList<>();
        Throwable t = ex;
        while (t != null) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setMessage(t.getMessage());
            Arrays.asList(t.getStackTrace()).stream()
                    .filter(e -> e.getClassName().startsWith(this.getClass().getName())).forEach(e -> {
                        errorInfo.stackInfos.add(new StackInfo(e.getMethodName(), getClass().getCanonicalName(),
                                e.getFileName(), e.getLineNumber()));
                    });
            errorInfos.add(errorInfo);
            t = t.getCause();
        }
        System.out.println(errorInfos);
        return ResponseDataEntity(ResponseData.error(errorInfos.stream().map(e -> {
            return e.getMessage();
        }).collect(Collectors.toList())));
    }

}