package com.db.hackhaton.web.rest.dto;

import com.db.hackhaton.domain.RegistryField;

public class MedicalCaseValue {
    private Long id;
    private String value;
    private RegistryField registryField;

    public MedicalCaseValue() {}

    public MedicalCaseValue(Long id, String value, RegistryField registryField) {
        this.id = id;
        this.value = value;
        this.registryField = registryField;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RegistryField getRegistryField() {
        return registryField;
    }

    public void setRegistryField(RegistryField registryField) {
        this.registryField = registryField;
    }
}
