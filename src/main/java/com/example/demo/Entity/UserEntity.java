package com.example.demo.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.example.demo.Enum.RoleEnum;
import com.example.demo.Util.BcryptUtil;
import com.example.demo.Validator.IMin;
import com.example.demo.Validator.IPattern;
import com.example.demo.Validator.IRequired;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "User_Master")
@Where(clause = "is_delete=false")
public class UserEntity extends BaseEntity {

    private static BcryptUtil bcryptUtil = new BcryptUtil(11);

    @Id
    @ApiModelProperty(hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String fullname = "";

    @ApiModelProperty(hidden = true)
    private RoleEnum roleEnum;

    @IRequired
    @IPattern(param = "^([a-z0-9\\._]{5,})$")
    private String username;
    
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String hash;

    @IRequired
    @IMin(param = 1)
    private Integer roleId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "roleId", name = "roleId", nullable = true, updatable = false, insertable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    RoleEntity roleEntity;

    @Transient
    @Setter(value = AccessLevel.NONE)
    private String password;

    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
            this.hash = UserEntity.bcryptUtil.hash(password);
        }
    }

    public void setPassword(String password, Boolean updateHash) {
        this.password = password;
        if (updateHash) {
            this.hash = UserEntity.bcryptUtil.hash(password);
        }
    }

    public Boolean verifyHash(String password) {
        return UserEntity.bcryptUtil.verifyHash(password, this.hash);
    }

    public static String hashPassword(String password) {
        return UserEntity.bcryptUtil.hash(password);
    }


    @Override
    public Integer getId() {
        return this.userId;
    }

    @Override
    public void setId(Integer id) {
        this.userId = id;
    }

}