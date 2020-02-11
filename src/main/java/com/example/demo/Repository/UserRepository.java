package com.example.demo.Repository;

import java.util.Optional;

import javax.persistence.TypedQuery;

import com.example.demo.Core.Clause;
import com.example.demo.Core.ResponseData;
import com.example.demo.Core.ServerQuery;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Enum.RoleEnum;

import org.springframework.stereotype.Repository;

/**
 * UserRepository
 */
@Repository
public class UserRepository extends BaseRepository<UserEntity> {

    public ResponseData<UserEntity> signup(UserEntity userEntity, UserEntity masterUserEntity) {
        userEntity.setPassword(null, false);
        ResponseData<UserEntity> responseData = this.create(userEntity, masterUserEntity);
        return responseData;
    }

    public ResponseData<UserEntity> createMasterUser(UserEntity userEntity) {
        userEntity.setPassword(null, false);
        userEntity.setCreatedBy(1);
        userEntity.setChangedBy(1);
        this.entityManager.persist(userEntity);
        userEntity.setCreatedBy(userEntity.getId());
        userEntity.setChangedBy(userEntity.getId());
        return ResponseData.success(userEntity);
    }

    public ResponseData<UserEntity> signin(UserEntity userEntity) {
        TypedQuery<UserEntity> typedQuery = this.entityManager.createQuery("SELECT c FROM " + this.getTable() + " c WHERE c.username=:username", UserEntity.class);
        typedQuery.setParameter("username", userEntity.getUsername());
        Optional<UserEntity> optional = typedQuery.getResultStream().findFirst();
        if (optional.isPresent()) {
            UserEntity uEntity = optional.get();
            if (uEntity.verifyHash(userEntity.getPassword())) {
                return ResponseData.success(uEntity);
            } else {
                return ResponseData.unauthorized("Your password is wrong");
            }
        }
        return ResponseData.notFound("Your credential is not math with any record");
    }

    @SuppressWarnings("unchecked")
    public ResponseData<Long> countByUsername(String username) {
        ServerQuery serverQuery = new ServerQuery();
        serverQuery.setSelect("COUNT(c)");
        serverQuery.setTable(this.getTable());
        serverQuery.getWhereClauses().add(new Clause("username", "=", username));
        
        TypedQuery<Long> typedQuery = (TypedQuery<Long>) getTypedQuery(serverQuery, Long.class);
        return ResponseData.success(typedQuery.getSingleResult());
    }

    public ResponseData<UserEntity> getMasterUser() {
        TypedQuery<UserEntity> typedQuery = this.entityManager.createQuery("SELECT c FROM " + this.getTable() + " c WHERE c.roleEnum=:roleEnum", UserEntity.class);
        typedQuery.setParameter("roleEnum", RoleEnum.ROLE_ADMIN);
        Optional<UserEntity> optional = typedQuery.getResultStream().findFirst();
        if (optional.isPresent()) {
            return ResponseData.success(optional.get());
        }
        return ResponseData.notFound();
    }

    @Override
    void preUpdate(UserEntity newEntity, UserEntity oldEntity) {
        super.preUpdate(newEntity, oldEntity);
        newEntity.setHash(oldEntity.getHash());
        newEntity.setUsername(oldEntity.getUsername());
    }

}