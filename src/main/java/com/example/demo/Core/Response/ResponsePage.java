package com.example.demo.Core.Response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ResponsePage
 */
@Setter
@Getter
@ToString
public class ResponsePage<T> {

    private List<T> records;
    private Integer totalRecord;
    private Integer totalPage;
    private Integer currentPage;
    private Integer pageSize;

    public void update() {
        this.setTotalPage(Long.valueOf(Math.round(Math.ceil(this.getTotalRecord() * 1.0 / this.getPageSize()))).intValue());
    }

}