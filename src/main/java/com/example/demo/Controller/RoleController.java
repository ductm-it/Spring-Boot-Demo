package com.example.demo.Controller;

import com.example.demo.Entity.RoleEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1.0/role")
public class RoleController extends BaseController<RoleEntity> {

}