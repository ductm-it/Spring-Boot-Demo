package com.example.demo.Interface.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
 * IEntity
 */
public interface IEntity {
    
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    Integer getId();

    void setId(Integer id);

    void updateDisplay();

}