package com.db.hackhaton.repository;

import com.db.hackhaton.domain.CaseFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


/**
 * Spring Data JPA repository for the CaseFieldValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaseFieldValueRepository extends JpaRepository<CaseFieldValue,Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CaseFieldValue c set c.value =:value where c.id =:id")
    int updateValueById(@Param("value")String value, @Param("id")Long id);

}
