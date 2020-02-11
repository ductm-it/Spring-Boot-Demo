package com.example.demo.Repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TypedQuery;

import com.example.demo.Core.Clause;
import com.example.demo.Core.ResponseData;
import com.example.demo.Core.ServerQuery;
import com.example.demo.Core.Request.RequestFilter;
import com.example.demo.Core.Request.RequestPage;
import com.example.demo.Core.Response.LabelValue;
import com.example.demo.Core.Response.ResponsePage;
import com.example.demo.Entity.BaseEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Enum.RoleEnum;
import com.example.demo.Enum.ServerAction;
import com.example.demo.Enum.ServerActionResponse;
import com.example.demo.Interface.Repository.IRepository;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * BaseRepository
 */
@MappedSuperclass
@Getter
@SuppressWarnings("unchecked")
public abstract class BaseRepository<T extends BaseEntity> implements IRepository<T> {

    private String table;
    private String primary;
    private Class<T> dataType;

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    protected EntityManager entityManager;

    public BaseRepository() {
        Class<?> clazz = this.getClass();
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Type type = parameterizedType.getActualTypeArguments()[0];
        this.dataType = (Class<T>) type;
        this.table = ((Class<T>) type).getAnnotation(Entity.class).name();
        this.primary = Arrays.asList(((Class<T>) type).getDeclaredFields()).stream().filter(t -> t.getAnnotation(Id.class) != null).findFirst().get().getName();
    }

    void preUpdate(T newEntity, T oldEntity) {
        newEntity.setChangedBy(0);
        newEntity.setIsDelete(oldEntity.getIsDelete());
        newEntity.setVersion(oldEntity.getVersion());
        newEntity.setCreatedBy(oldEntity.getCreatedBy());
        newEntity.setCreatedDate(oldEntity.getCreatedDate());
    }

    void preSave(T entity) {
        entity.setChangedBy(0);
    }

    TypedQuery<?> getTypedQuery(ServerQuery serverQuery, Class<?> clazz) {
        String hql = serverQuery.toHql();
        Map<String, Object> param = serverQuery.getParam();

        TypedQuery<?> typedQuery = this.entityManager.createQuery(hql, clazz);
        if (param.size() > 0) {
            for (Map.Entry<String, Object> p : param.entrySet()) {
                typedQuery.setParameter(p.getKey(), p.getValue());
            }
        }

        return typedQuery;
    }

    protected final ServerActionResponse getServerActionResponse(UserEntity userEntity, ServerAction serverAction) {
        if (RoleEnum.ROLE_ADMIN.equals(userEntity.getRoleEnum())) return ServerActionResponse.BYPASS;
        if (userEntity.getRoleEntity() == null || userEntity.getRoleEntity().getRole() == null) return ServerActionResponse.REJECT;
        Map<String, Long> map = userEntity.getRoleEntity().getRole();

        if (map.containsKey(this.getTable())) {
            Long val = map.get(this.getTable());
            Long perm = serverAction.getData();
            if ((val & perm) == perm) {
                if (ServerAction.CREATE.equals(serverAction)) {
                    return ServerActionResponse.ACCEPT;
                }

                Long fullPerm = perm << 1;
                if ((val & fullPerm) == fullPerm) {
                    return ServerActionResponse.BYPASS;
                }

                return ServerActionResponse.ACCEPT;
            }
        }

        return ServerActionResponse.REJECT;
    }


    @Override
    public ResponseData<T> create(T entity, UserEntity userEntity) {
        if (ServerActionResponse.REJECT.equals(this.getServerActionResponse(userEntity, ServerAction.CREATE))) {
            return ResponseData.forbidden();
        }
        entity.setCreatedBy(userEntity.getId());
        entity.setChangedBy(userEntity.getId());
        this.entityManager.persist(entity);
        return ResponseData.success(entity);
    }

    @Override
    public ResponseData<T> read(Integer id, UserEntity userEntity) {
        ServerActionResponse serverActionResponse = this.getServerActionResponse(userEntity, ServerAction.READ);
        if (ServerActionResponse.REJECT.equals(serverActionResponse)) {
            return ResponseData.forbidden();
        }
        ServerQuery serverQuery = new ServerQuery();
        serverQuery.setTable(this.getTable());
        serverQuery.getWhereClauses().add(new Clause(this.getPrimary(), "=", id));
        if (!ServerActionResponse.BYPASS.equals(serverActionResponse)) {
            serverQuery.getWhereClauses().add(new Clause("createdBy", "=", userEntity.getId()));
        }

        TypedQuery<T> typedQuery = (TypedQuery<T>) this.getTypedQuery(serverQuery, this.getDataType());
        Optional<T> optional = typedQuery.getResultStream().findFirst();
        
        if (optional.isPresent())
            return ResponseData.success(optional.get());
        return ResponseData.notFound();
    }

    @Override
    public ResponseData<ResponsePage<T>> getPage(RequestPage requestPage, UserEntity userEntity) {
        ServerActionResponse serverActionResponse = this.getServerActionResponse(userEntity, ServerAction.READ);
        if (ServerActionResponse.REJECT.equals(serverActionResponse)) {
            return ResponseData.forbidden();
        }

        ServerQuery serverQuery = new ServerQuery();
        serverQuery.setTable(this.getTable());
        if (!ServerActionResponse.BYPASS.equals(serverActionResponse)) {
            serverQuery.getWhereClauses().add(new Clause("createdBy", "=", userEntity.getId()));
        }
        if (requestPage.getSearchString() != null) {
            serverQuery.getWhereClauses().add(new Clause("display", "LIKE", "%" + requestPage.getSearchString() + "%"));
        }

        ResponsePage<T> responsePage = new ResponsePage<>();
        TypedQuery<T> typedQuery = (TypedQuery<T>) this.getTypedQuery(serverQuery, this.getDataType());

        responsePage.setTotalRecord(Long.valueOf(typedQuery.getResultStream().count()).intValue());

        typedQuery.setMaxResults(requestPage.getPageSize());
        typedQuery.setFirstResult(requestPage.getPageSize() * (requestPage.getPageNumber() - 1));
        
        responsePage.setRecords(typedQuery.getResultList());
        responsePage.setPageSize(requestPage.getPageSize());
        responsePage.setCurrentPage(requestPage.getPageNumber());
        responsePage.update();
        
        return ResponseData.success(responsePage);
    }

    

    @Override
    public ResponseData<List<LabelValue>> filter(RequestFilter requestFilter, UserEntity userEntity) {
        ServerQuery serverQuery = new ServerQuery();
        serverQuery.setTable(this.getTable());
        serverQuery.setSelect("new " + LabelValue.class.getCanonicalName() + "(c." + this.getPrimary() + ", c.display)");
        if (requestFilter.getSearchString() != null) {
            serverQuery.getWhereClauses().add(new Clause("display", "LIKE", "%" + requestFilter.getSearchString() + "%"));
        }

        TypedQuery<LabelValue> typedQuery = (TypedQuery<LabelValue>) this.getTypedQuery(serverQuery, LabelValue.class);
        typedQuery.setMaxResults(10);
        typedQuery.setFirstResult(0);
        
        return ResponseData.success(typedQuery.getResultList());
    }

    @Override
    public ResponseData<Long> count(RequestFilter requestFilter, UserEntity userEntity) {
        ServerActionResponse serverActionResponse = this.getServerActionResponse(userEntity, ServerAction.READ);
        if (ServerActionResponse.REJECT.equals(serverActionResponse)) {
            return ResponseData.forbidden();
        }

        ServerQuery serverQuery = new ServerQuery();
        serverQuery.setTable(this.getTable());
        serverQuery.setSelect("COUNT(c)");
        if (!ServerActionResponse.BYPASS.equals(serverActionResponse)) {
            serverQuery.getWhereClauses().add(new Clause("createdBy", "=", userEntity.getId()));
        }
        if (requestFilter.getSearchString() != null) {
            serverQuery.getWhereClauses().add(new Clause("display", "LIKE", "%" + requestFilter.getSearchString() + "%"));
        }

        TypedQuery<Long> typedQuery = (TypedQuery<Long>) this.getTypedQuery(serverQuery, Long.class);
        
        return ResponseData.success(typedQuery.getSingleResult());
    }

    @Override
    public ResponseData<T> update(Integer id, T newEntity, UserEntity userEntity) {
        ServerActionResponse serverActionResponse = this.getServerActionResponse(userEntity, ServerAction.UPDATE);
        if (ServerActionResponse.REJECT.equals(serverActionResponse)) {
            return ResponseData.forbidden();
        }
        ResponseData<T> responseData = this.read(id, userEntity);
        if (responseData.getStatus()) {
            T oldEntity = responseData.getData();
            if (!ServerActionResponse.BYPASS.equals(serverActionResponse)) {
                if (!oldEntity.getCreatedBy().equals(userEntity.getId())) {
                    return ResponseData.forbidden("You can not update the record which created by another user with approval");
                }
            }
            this.preUpdate(newEntity, oldEntity);
            newEntity.setChangedBy(userEntity.getId());
            this.entityManager.merge(newEntity);
            return ResponseData.success(newEntity);
        }
        return ResponseData.notFound();
    }

    @Override
    public ResponseData<Boolean> delete(Integer id, UserEntity userEntity) {
        ServerActionResponse serverActionResponse = this.getServerActionResponse(userEntity, ServerAction.DELETE);
        if (ServerActionResponse.REJECT.equals(serverActionResponse)) {
            return ResponseData.forbidden();
        }
        ResponseData<T> responseData = this.read(id, userEntity);
        if (responseData.getStatus()) {
            T oldEntity = responseData.getData();
            if (!ServerActionResponse.BYPASS.equals(serverActionResponse)) {
                if (!oldEntity.getCreatedBy().equals(userEntity.getId())) {
                    return ResponseData.forbidden("You can not delete the record which created by another user with approval");
                }
            }
            oldEntity.setIsDelete(true);
            oldEntity.setChangedBy(userEntity.getId());
            this.entityManager.merge(oldEntity);
            return ResponseData.success(true);
        }
        return ResponseData.notFound();
    }

}