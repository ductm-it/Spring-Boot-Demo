package com.example.demo.Service;

import java.util.Optional;

import com.example.demo.Entity.UserEntity;
import com.example.demo.Enum.RoleEnum;
import com.example.demo.Interface.Service.IUserService;
import com.example.demo.Repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserService
 */
@Service
public class UserService implements IUserService {

    @Autowired
    protected RoleRepository roleRepository;

    @Override
    public UserEntity getLoggedInUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = ((UserDetails) authentication.getPrincipal());
            Integer userId = Integer.parseInt(userDetails.getUsername());
            Optional<RoleEnum> optional = authentication.getAuthorities().stream()
                    .map(e -> RoleEnum.valueOf(e.getAuthority())).findFirst();

            UserEntity userEntity = new UserEntity();
            userEntity.setId(userId);
            userEntity.setRoleEnum(optional.isPresent() ? optional.get() : RoleEnum.ROLE_USER);
            userEntity.setRoleEntity(this.roleRepository.getByUserId(userId));
            return userEntity;
        } else {
            throw new UsernameNotFoundException("User is not authenticated; Found " + authentication.getPrincipal()
                    + " of type " + authentication.getPrincipal().getClass() + "; Expected type User");
        }
    }

    @Override
    public Integer getLoggedInId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            return Integer.parseInt(((UserDetails) authentication.getPrincipal()).getUsername());
        } else {
            throw new UsernameNotFoundException("User is not authenticated; Found " + authentication.getPrincipal()
                    + " of type " + authentication.getPrincipal().getClass() + "; Expected type User");
        }
    }
    
}