package com.example.demo.Controller;

import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.Core.ResponseData;
import com.example.demo.Core.Request.RequestFilter;
import com.example.demo.Core.Request.RequestPage;
import com.example.demo.Core.Response.LabelValue;
import com.example.demo.Core.Response.ResponsePage;
import com.example.demo.Entity.BaseEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Interface.Controller.IController;
import com.example.demo.Interface.Service.IUserService;
import com.example.demo.Repository.BaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public abstract class BaseController<T extends BaseEntity> extends ErrorController implements IController<T> {

    @Autowired
    protected IUserService userService;

    @Autowired
    protected BaseRepository<T> baseRepository;

    @Transactional
    @Override
    public ResponseEntity<ResponseData<T>> create(T entity) throws Exception {
        UserEntity userEntity = this.userService.getLoggedInUser();
        return ResponseDataEntity(this.baseRepository.create(entity, userEntity));
    }

    @Override
    public ResponseEntity<ResponseData<T>> read(Integer id) throws Exception {
        UserEntity userEntity = this.userService.getLoggedInUser();
        return ResponseDataEntity(this.baseRepository.read(id, userEntity));
    }

    @Override
    public ResponseEntity<ResponseData<ResponsePage<T>>> getPage(RequestPage requestPage) throws Exception {
        UserEntity userEntity = this.userService.getLoggedInUser();
        return ResponseDataEntity(this.baseRepository.getPage(requestPage, userEntity));
    }

    @Override
    public ResponseEntity<ResponseData<List<LabelValue>>> filter(RequestFilter requestFilter) throws Exception {
        UserEntity userEntity = this.userService.getLoggedInUser();
        return ResponseDataEntity(this.baseRepository.filter(requestFilter, userEntity));
    }

    @Override
    public ResponseEntity<ResponseData<Long>> count(RequestFilter requestFilter) throws Exception {
        UserEntity userEntity = this.userService.getLoggedInUser();
        return ResponseDataEntity(this.baseRepository.count(requestFilter, userEntity));
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseData<T>> update(Integer id, T entity) throws Exception {
        UserEntity userEntity = this.userService.getLoggedInUser();
        return ResponseDataEntity(this.baseRepository.update(id, entity, userEntity));
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseData<Boolean>> delete(Integer id) throws Exception {
        UserEntity userEntity = this.userService.getLoggedInUser();
        return ResponseDataEntity(this.baseRepository.delete(id, userEntity));
    }

}