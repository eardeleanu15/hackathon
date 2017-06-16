package com.db.hackhaton.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MedicalCase.
 */
@Entity
@Table(name = "medical_case")
public class MedicalCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "approval_status")
    private Boolean approvalStatus;

    @Column(name = "createdon")
    private LocalDate createdon;

    @Column(name = "archived")
    private Boolean archived;

    @Column(name = "version")
    private Integer version;

    @ManyToOne
    @JsonBackReference
    private Registry registry;

    @OneToMany(mappedBy = "medicalCase", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<CaseFieldValue> caseFields = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isApprovalStatus() {
        return approvalStatus;
    }

    public MedicalCase approvalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
        return this;
    }

    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public LocalDate getCreatedon() {
        return createdon;
    }

    public MedicalCase createdon(LocalDate createdon) {
        this.createdon = createdon;
        return this;
    }

    public void setCreatedon(LocalDate createdon) {
        this.createdon = createdon;
    }

    public Boolean isArchived() {
        return archived;
    }

    public MedicalCase archived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Integer getVersion() {
        return version;
    }

    public MedicalCase version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Registry getRegistry() {
        return registry;
    }

    public MedicalCase registry(Registry registry) {
        this.registry = registry;
        return this;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Set<CaseFieldValue> getCaseFields() {
        return caseFields;
    }

    public MedicalCase caseFields(Set<CaseFieldValue> caseFieldValues) {
        this.caseFields = caseFieldValues;
        return this;
    }

    public MedicalCase addCaseField(CaseFieldValue caseFieldValue) {
        this.caseFields.add(caseFieldValue);
        caseFieldValue.setMedicalCase(this);
        return this;
    }

    public MedicalCase removeCaseField(CaseFieldValue caseFieldValue) {
        this.caseFields.remove(caseFieldValue);
        caseFieldValue.setMedicalCase(null);
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
        MedicalCase medicalCase = (MedicalCase) o;
        if (medicalCase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicalCase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MedicalCase{" +
            "id=" + getId() +
            ", approvalStatus='" + isApprovalStatus() + "'" +
            ", createdon='" + getCreatedon() + "'" +
            ", archived='" + isArchived() + "'" +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
