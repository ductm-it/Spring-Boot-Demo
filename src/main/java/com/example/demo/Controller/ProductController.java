package com.example.demo.Controller;

import com.example.demo.Entity.ProductEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1.0/product")
public class ProductController extends BaseController<ProductEntity> {

}