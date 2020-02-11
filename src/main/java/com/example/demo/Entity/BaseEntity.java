package com.example.demo.Entity;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import com.example.demo.Interface.Entity.IEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class BaseEntity implements IEntity {

    @Version
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    Long version;

    @ApiModelProperty(hidden = true)
    Integer createdBy;

    @ApiModelProperty(hidden = true)
    Date createdDate;

    @ApiModelProperty(hidden = true)
    Integer changedBy;

    @ApiModelProperty(hidden = true)
    Date changedDate;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    Boolean isDelete;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    String display;

    @Override
    public void updateDisplay() {
        this.display = "RECORD_" + String.format("%09d", this.getId());
    }

    @PrePersist
    protected void prePersist() {
        this.updateDisplay();
        this.setVersion(0l);
        this.setIsDelete(false);
        this.setCreatedDate(new Date());
        this.setChangedDate(new Date());
    }

    @PreUpdate
    protected void preUpdate() {
        this.updateDisplay();
        this.setChangedDate(new Date());
    }

}