package com.example.demo.Config;


import com.example.demo.Entity.UserEntity;
import com.example.demo.Enum.RoleEnum;
import com.example.demo.Filter.JwtTokenFilter;
import com.example.demo.Provider.JwtTokenProvider;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilterConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        System.out.println("################ " + this.jwtTokenProvider.createToken(new UserEntity() {
            {
                setId(1);
                setRoleEnum(RoleEnum.ROLE_ADMIN);
        }}));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
