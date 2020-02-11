package com.example.demo.Interface.Repository;

import java.util.List;

import com.example.demo.Core.ResponseData;
import com.example.demo.Core.Request.RequestFilter;
import com.example.demo.Core.Request.RequestPage;
import com.example.demo.Core.Response.LabelValue;
import com.example.demo.Core.Response.ResponsePage;
import com.example.demo.Entity.BaseEntity;
import com.example.demo.Entity.UserEntity;

/**
 * IRepository
 */
public interface IRepository<T extends BaseEntity> {

    ResponseData<T> create(T entity, UserEntity userEntity);

    ResponseData<T> read(Integer id, UserEntity userEntity);

    ResponseData<ResponsePage<T>> getPage(RequestPage requestPage, UserEntity userEntity);

    ResponseData<List<LabelValue>> filter(RequestFilter requestPage, UserEntity userEntity);

    ResponseData<Long> count(RequestFilter requestPage, UserEntity userEntity);

    ResponseData<T> update(Integer id, T entity, UserEntity userEntity);

    ResponseData<Boolean> delete(Integer id, UserEntity userEntity);

    String getPrimary();

    String getTable();

    Class<T> getDataType();

}