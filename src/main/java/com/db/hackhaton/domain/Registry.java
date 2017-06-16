package com.db.hackhaton.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Registry.
 */
@Entity
@Table(name = "registry")
public class Registry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "createdon")
    private LocalDate createdon;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "archived")
    private Boolean archived;

    @OneToMany(mappedBy = "registry", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    @JsonManagedReference
    private Set<RegistryField> registryFields;

    @OneToMany(mappedBy = "registry", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<MedicalCase> medicalCases = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Registry name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Registry description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedon() {
        return createdon;
    }

    public Registry createdon(LocalDate createdon) {
        this.createdon = createdon;
        return this;
    }

    public void setCreatedon(LocalDate createdon) {
        this.createdon = createdon;
    }

    public Boolean isActive() {
        return active;
    }

    public Registry active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isArchived() {
        return archived;
    }

    public Registry archived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Set<RegistryField> getRegistryFields() {
        return registryFields;
    }

    public Registry registryFields(Set<RegistryField> registryFields) {
        this.registryFields = registryFields;
        return this;
    }

    public Registry addRegistryField(RegistryField registryField) {
        this.registryFields.add(registryField);
        registryField.setRegistry(this);
        return this;
    }

    public Registry removeRegistryField(RegistryField registryField) {
        this.registryFields.remove(registryField);
        registryField.setRegistry(null);
        return this;
    }

    public void setRegistryFields(Set<RegistryField> registryFields) {
        this.registryFields = registryFields;
    }

    public Set<MedicalCase> getMedicalCases() {
        return medicalCases;
    }

    public Registry medicalCases(Set<MedicalCase> medicalCases) {
        this.medicalCases = medicalCases;
        return this;
    }

    public Registry addMedicalCase(MedicalCase medicalCase) {
        this.medicalCases.add(medicalCase);
        medicalCase.setRegistry(this);
        return this;
    }

    public Registry removeMedicalCase(MedicalCase medicalCase) {
        this.medicalCases.remove(medicalCase);
        medicalCase.setRegistry(null);
        return this;
    }

    public void setMedicalCases(Set<MedicalCase> medicalCases) {
        this.medicalCases = medicalCases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Registry registry = (Registry) o;
        if (registry.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registry.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Registry{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdon='" + getCreatedon() + "'" +
            ", active='" + isActive() + "'" +
            ", archived='" + isArchived() + "'" +
            "}";
    }
}
