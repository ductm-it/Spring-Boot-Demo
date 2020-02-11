package com.example.demo.Entity;

import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.demo.Converter.MapConverter;

import org.hibernate.annotations.Where;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "User_Role")
@Where(clause = "is_delete=false")
public class RoleEntity extends BaseEntity {

    @Id
    @ApiModelProperty(hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer roleId;

    String description;
    
    @Convert(converter = MapConverter.class)
    Map<String, Long> role;

    @Override
    public Integer getId() {
        return this.roleId;
    }

    @Override
    public void setId(Integer id) {
        this.roleId = id;
    }

}