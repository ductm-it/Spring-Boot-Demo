package com.example.demo.Core;

import com.example.demo.Validator.IPattern;
import com.example.demo.Validator.IRequired;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginData {

    @IRequired
    @IPattern(param = "^([a-z\\._0-9]{5,})$")
    private String username;

    @IRequired
    private String password;

}