package com.example.demo.Core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clause
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Clause {

    private String key;
    private String operator;
    private Object value;

    public String toClauseString() {
        return this.key + " " + this.operator + " :" + this.key;
    }

}