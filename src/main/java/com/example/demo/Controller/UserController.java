package com.example.demo.Controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.example.demo.Core.AuthUser;
import com.example.demo.Core.JwtToken;
import com.example.demo.Core.LoginData;
import com.example.demo.Core.ResponseData;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Enum.RoleEnum;
import com.example.demo.Provider.JwtTokenProvider;
import com.example.demo.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1.0/user")
public class UserController extends BaseController<UserEntity> {

    private static UserEntity masterUser = null;

    public UserEntity getMasterUser() {
        if (UserController.masterUser != null) {
            return UserController.masterUser;
        } else {
            ResponseData<UserEntity> responseData = ((UserRepository) this.baseRepository).getMasterUser();
            if (responseData.getStatus()) {
                UserController.masterUser = responseData.getData();
                return UserController.masterUser;
            }
        }
        return null;
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public ResponseEntity<ResponseData<JwtToken>> signin(@Valid @RequestBody LoginData loginData) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(loginData.getUsername());
        userEntity.setPassword(loginData.getPassword());
        
        ResponseData<UserEntity> responseData = ((UserRepository) this.baseRepository).signin(userEntity);
        if (responseData.getStatus()) {
            JwtToken jwtToken = new JwtToken(this.jwtTokenProvider.createToken(responseData.getData()));
            return ResponseDataEntity(ResponseData.success(jwtToken));
        }
        return ResponseDataEntity(ResponseData.from(responseData));
    }

    @Transactional
    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public ResponseEntity<ResponseData<UserEntity>> signup(@Valid @RequestBody AuthUser authUser) {
        UserEntity masterUserEntity = this.getMasterUser();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(authUser.getUsername());
        userEntity.setPassword(authUser.getPassword());

        ResponseData<UserEntity> responseData = null;
        if (masterUserEntity == null) {
            userEntity.setRoleEnum(RoleEnum.ROLE_ADMIN);
            responseData = ((UserRepository) this.baseRepository).createMasterUser(userEntity);
            if (responseData.getStatus()) {
                UserController.masterUser = responseData.getData();
            }
        } else {
            userEntity.setRoleEnum(RoleEnum.ROLE_USER);
            responseData = ((UserRepository) this.baseRepository).signup(userEntity, masterUserEntity);
        }
        return ResponseDataEntity(responseData);
    }

}