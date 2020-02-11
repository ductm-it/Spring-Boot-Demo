package com.example.demo.Config;

import com.example.demo.Provider.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${eureka.client.enabled:false}")
    private Boolean eurekaEnabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (!this.eurekaEnabled) {
            http.authorizeRequests()
                    .antMatchers("/api/v1.0/user/signin").permitAll()
                    .antMatchers("/api/v1.0/user/signup").permitAll()
                    .antMatchers("/api/v1.0/user/send-reset-password-token/**").permitAll()
                    .antMatchers("/api/v1.0/user/update-password-by-token").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1.0/*/excel-template").permitAll()
                    
                    .antMatchers("/v2/api-docs").permitAll()
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/swagger-ui.html").permitAll()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .anyRequest().authenticated();
            http.exceptionHandling().accessDeniedPage("/auth/login");
        }
        http.apply(new JwtTokenFilterConfig(jwtTokenProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}