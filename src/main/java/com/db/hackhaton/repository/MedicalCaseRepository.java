package com.db.hackhaton.repository;

import com.db.hackhaton.domain.MedicalCase;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the MedicalCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalCaseRepository extends JpaRepository<MedicalCase,Long> {
    @Query("SELECT mc FROM MedicalCase mc JOIN CaseFieldValue cfv ON (cfv.medicalCase.id = mc.id) JOIN RegistryField rf ON (rf.id = cfv.registryField.id) JOIN FETCH mc.registry WHERE rf.name = 'CNP' AND cfv.value = ?1")
    List<MedicalCase> getMedicalCasesByUserCnp(@Param("cnp") String cnp);
}
