package com.example.demo.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.Core.Request.RequestPage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ServerQuery
 */
@Getter
@Setter
@ToString
public class ServerQuery extends RequestPage {

    private List<Clause> whereClauses = new ArrayList<>();
    private String table;
    private String select = "c";

    public String toHql() {
        String hql = "SELECT " + this.getSelect() + " FROM " + this.getTable() + " c";
        if (this.getWhereClauses() != null && this.getWhereClauses().size() > 0) {
            hql += " WHERE " + this.getWhereClauses().stream().map(e -> e.toClauseString()).reduce((a, b) -> {
                return a + " AND " + b;
            }).get();
        }
        return hql;
    }

    public Map<String, Object> getParam() {
        Map<String, Object> map = new HashMap<>();
        this.getWhereClauses().forEach(t -> {
            map.put(t.getKey(), t.getValue());
        });
        return map;
    }

    public static void main(String[] args) {
        ServerQuery serverQuery = new ServerQuery();
        serverQuery.setTable("Product");
        serverQuery.getWhereClauses()
                .addAll(Arrays.asList(new Clause("id", "=", 4), new Clause("display", "LIKE", "%null%")));

        System.out.println(serverQuery.toHql());
    }
}