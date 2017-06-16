package com.db.hackhaton.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.db.hackhaton.domain.enumeration.FieldType;

import com.db.hackhaton.domain.enumeration.FieldGroups;

/**
 * A RegistryField.
 */
@Entity
@Table(name = "registry_field")
public class RegistryField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "mandatory")
    private Boolean mandatory;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private FieldType type;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "jhi_values")
    private String values;

    @Column(name = "default_value")
    private String defaultValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_group")
    private FieldGroups group;

    @ManyToOne
    @JsonBackReference
    private Registry registry;

    @OneToMany(mappedBy = "registryField")
    @JsonIgnore
    private Set<CaseFieldValue> caseFields = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RegistryField name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public RegistryField active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isMandatory() {
        return mandatory;
    }

    public RegistryField mandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public FieldType getType() {
        return type;
    }

    public RegistryField type(FieldType type) {
        this.type = type;
        return this;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public RegistryField maxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getValues() {
        return values;
    }

    public RegistryField values(String values) {
        this.values = values;
        return this;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public RegistryField defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public FieldGroups getGroup() {
        return group;
    }

    public RegistryField group(FieldGroups group) {
        this.group = group;
        return this;
    }

    public void setGroup(FieldGroups group) {
        this.group = group;
    }

    public Registry getRegistry() {
        return registry;
    }

    public RegistryField registry(Registry registry) {
        this.registry = registry;
        return this;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Set<CaseFieldValue> getCaseFields() {
        return caseFields;
    }

    public RegistryField caseFields(Set<CaseFieldValue> caseFieldValues) {
        this.caseFields = caseFieldValues;
        return this;
    }

    public RegistryField addCaseField(CaseFieldValue caseFieldValue) {
        this.caseFields.add(caseFieldValue);
        caseFieldValue.setRegistryField(this);
        return this;
    }

    public RegistryField removeCaseField(CaseFieldValue caseFieldValue) {
        this.caseFields.remove(caseFieldValue);
        caseFieldValue.setRegistryField(null);
        return this;
    }

    public void setCaseFields(Set<CaseFieldValue> caseFieldValues) {
        this.caseFields = caseFieldValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistryField registryField = (RegistryField) o;
        if (registryField.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registryField.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegistryField{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + isActive() + "'" +
            ", mandatory='" + isMandatory() + "'" +
            ", type='" + getType() + "'" +
            ", maxLength='" + getMaxLength() + "'" +
            ", values='" + getValues() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", group='" + getGroup() + "'" +
            "}";
    }
}
