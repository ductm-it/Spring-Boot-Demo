package com.example.demo.Interface.Controller;

import java.util.List;

import javax.validation.Valid;

import com.example.demo.Core.ResponseData;
import com.example.demo.Core.Request.RequestFilter;
import com.example.demo.Core.Request.RequestPage;
import com.example.demo.Core.Response.LabelValue;
import com.example.demo.Core.Response.ResponsePage;
import com.example.demo.Entity.BaseEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

/**
 * IController
 */
public interface IController<T extends BaseEntity> {

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Create new record", notes = "Based on your api, our system will define the data type and put it into our Database")
    public ResponseEntity<ResponseData<T>> create(@RequestBody T entity) throws Exception;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get the record by id")
    public ResponseEntity<ResponseData<T>> read(@PathVariable Integer id) throws Exception;

    @RequestMapping(value = "/get-page", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Get records by page number")
    public ResponseEntity<ResponseData<ResponsePage<T>>> getPage(@Valid @RequestBody RequestPage requestPage) throws Exception;

    @RequestMapping(value = "/filter", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Filter by string")
    public ResponseEntity<ResponseData<List<LabelValue>>> filter(@Valid @RequestBody RequestFilter requestPage) throws Exception;

    @RequestMapping(value = "/count", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Count by string")
    public ResponseEntity<ResponseData<Long>> count(@Valid @RequestBody RequestFilter requestPage) throws Exception;

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Update the record by id")
    public ResponseEntity<ResponseData<T>> update(@PathVariable Integer id, @RequestBody T entity) throws Exception;

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ApiOperation(value = "Delete the record by id")
    public ResponseEntity<ResponseData<Boolean>> delete(@PathVariable Integer id) throws Exception;

}