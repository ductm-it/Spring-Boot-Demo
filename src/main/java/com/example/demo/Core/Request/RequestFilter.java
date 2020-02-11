package com.example.demo.Core.Request;

import com.example.demo.Validator.IMaxLength;

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
public class RequestFilter {
    
    @IMaxLength(param = 10)
    @ApiModelProperty(example = " ")
    @Setter(value = AccessLevel.NONE)
    protected String searchString;


    public void setSearchString(String searchString) {
        if (searchString != null) {
            searchString = searchString.trim();
            if (searchString.length() > 0) {
                this.searchString = searchString;
            }
        }
    }

}