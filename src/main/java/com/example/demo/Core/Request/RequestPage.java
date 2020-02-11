package com.example.demo.Core.Request;

import com.example.demo.Validator.IMin;
import com.example.demo.Validator.IRequired;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * RequestPage
 */
@Getter
@Setter
@ToString
public class RequestPage extends RequestFilter {

    @IRequired
    @IMin(param = 1)
    @ApiModelProperty(example = "1")
    private Integer pageNumber;

    @IRequired
    @IMin(param = 1)
    @ApiModelProperty(example = "10")
    @Getter(value = AccessLevel.NONE)
    private Integer pageSize;

    public Integer getPageSize() {
        if (this.pageSize > 100) {
            return 100;
        }
        return this.pageSize;
    }

}