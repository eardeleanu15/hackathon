package com.db.hackhaton.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CaseFieldValue.
 */
@Entity
@Table(name = "case_field_value")
public class CaseFieldValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_value")
    private String value;

    @ManyToOne
    private RegistryField registryField;

    @ManyToOne
    @JsonBackReference
    private MedicalCase medicalCase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public CaseFieldValue value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RegistryField getRegistryField() {
        return registryField;
    }

    public CaseFieldValue registryField(RegistryField registryField) {
        this.registryField = registryField;
        return this;
    }

    public void setRegistryField(RegistryField registryField) {
        this.registryField = registryField;
    }

    public MedicalCase getMedicalCase() {
        return medicalCase;
    }

    public CaseFieldValue medicalCase(MedicalCase medicalCase) {
        this.medicalCase = medicalCase;
        return this;
    }

    public void setMedicalCase(MedicalCase medicalCase) {
        this.medicalCase = medicalCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CaseFieldValue caseFieldValue = (CaseFieldValue) o;
        if (caseFieldValue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), caseFieldValue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CaseFieldValue{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
