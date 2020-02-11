package com.example.demo.Entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "Product")
@Where(clause = "is_delete=false")
public class ProductEntity extends BaseEntity {

    @Id
    @ApiModelProperty(hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer productId;

    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "userId", name = "createdBy", nullable = true, updatable = false, insertable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    UserEntity createdUser;

    @Override
    public Integer getId() {
        return this.productId;
    }

    @Override
    public void setId(Integer id) {
        this.productId = id;
    }

}