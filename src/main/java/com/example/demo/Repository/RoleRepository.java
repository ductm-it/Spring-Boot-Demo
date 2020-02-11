package com.example.demo.Repository;

import java.util.Optional;

import javax.persistence.TypedQuery;

import com.example.demo.Entity.RoleEntity;

import org.springframework.stereotype.Repository;

/**
 * RoleRepository
 */
@Repository
public class RoleRepository extends BaseRepository<RoleEntity> {

    public RoleEntity getByUserId(Integer userId) {
        TypedQuery<RoleEntity> typedQuery = this.entityManager.createQuery("SELECT c FROM " + this.getTable() + " c, User_Master d WHERE c.roleId=d.roleId AND d.userId=:userId", RoleEntity.class);
        typedQuery.setParameter("userId", userId);
        Optional<RoleEntity> optional = typedQuery.getResultStream().findFirst();
        if (optional.isPresent()) return optional.get();
        return null;
    }

}
