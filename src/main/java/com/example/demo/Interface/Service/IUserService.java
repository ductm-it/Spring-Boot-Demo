package com.example.demo.Interface.Service;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.Entity.UserEntity;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * UserService
 */
public interface IUserService {

    UserEntity getLoggedInUser() throws Exception;
    Integer getLoggedInId();

    public default String getJwtToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }
    
}