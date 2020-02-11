package com.example.demo.Enum;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public enum RoleEnum implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}