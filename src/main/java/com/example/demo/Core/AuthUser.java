package com.example.demo.Core;

import javax.persistence.Transient;

import com.example.demo.Validator.IPattern;
import com.example.demo.Validator.IRequired;
import com.example.demo.Validator.IUsername;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthUser {

    @IUsername
    @IRequired
    @IPattern(param = "^([a-z\\._0-9]{5,})$")
    private String username;

    
    @Transient
    @Setter(value = AccessLevel.NONE)
    private String password;

}