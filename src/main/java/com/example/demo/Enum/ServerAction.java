package com.example.demo.Enum;

import lombok.Getter;

@Getter
public enum ServerAction {

    CREATE(1l), READ(2l), READ_ALL(4l), UPDATE(10l), UPDATE_ALL(20l), DELETE(34l), DELETE_ALL(68l); 

    private Long data;

    private ServerAction(Long data) {
        this.data = data;
    }

}