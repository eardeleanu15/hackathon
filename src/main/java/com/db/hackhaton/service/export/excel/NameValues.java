package com.db.hackhaton.service.export.excel;

import java.util.List;

/**
 * Created by echo on 6/15/17.
 */
public class NameValues {
    public String fieldName;
    public List<String> values;

    public void add(String value) {
        this.values.add(value);
    }
}
