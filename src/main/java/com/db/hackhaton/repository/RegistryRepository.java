package com.db.hackhaton.repository;

import com.db.hackhaton.domain.Registry;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Registry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistryRepository extends JpaRepository<Registry,Long> {

    List<Registry> findAllByActive(Boolean active);
}
