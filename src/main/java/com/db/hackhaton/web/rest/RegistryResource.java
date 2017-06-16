package com.db.hackhaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackhaton.domain.Registry;

import com.db.hackhaton.repository.RegistryRepository;
import com.db.hackhaton.web.rest.dto.RegistryRetrieveDTO;
import com.db.hackhaton.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Registry.
 */
@RestController
@RequestMapping("/api")
public class RegistryResource {

    private final Logger log = LoggerFactory.getLogger(RegistryResource.class);

    private static final String ENTITY_NAME = "registry";

    private final RegistryRepository registryRepository;

    public RegistryResource(RegistryRepository registryRepository) {
        this.registryRepository = registryRepository;
    }

    /**
     * POST  /registries : Create a new registry.
     *
     * @param registry the registry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new registry, or with status 400 (Bad Request) if the registry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/registries")
    @Timed
    public ResponseEntity<Registry> createRegistry(@RequestBody Registry registry) throws URISyntaxException {
        log.debug("REST request to save Registry : {}", registry);
        if (registry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new registry cannot already have an ID")).body(null);
        }
        // Populate CreatedOn Field with current date
        registry.setCreatedon(LocalDate.now());

        Registry result = registryRepository.save(registry);
        return ResponseEntity.created(new URI("/api/registries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /registries : Updates an existing registry.
     *
     * @param registry the registry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registry,
     * or with status 400 (Bad Request) if the registry is not valid,
     * or with status 500 (Internal Server Error) if the registry couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/registries")
    @Timed
    public ResponseEntity<Registry> updateRegistry(@RequestBody Registry registry) throws URISyntaxException {
        log.debug("REST request to update Registry : {}", registry);
        if (registry.getId() == null) {
            return createRegistry(registry);
        }
        Registry result = registryRepository.save(registry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, registry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /registries : get all the registries.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of registries in body
     */
    @GetMapping("/registries")
    @Timed
    public List<RegistryRetrieveDTO> getAllRegistries(@RequestParam(value = "active", required = false) Boolean active) {
        log.debug("REST request to get all Registries");
        if (active != null) {
            List<Registry> allByActive = registryRepository.findAllByActive(active);
            return convertToDTO(allByActive);
        }

        List<Registry> all = registryRepository.findAll();
        return convertToDTO(all);
    }

    private List<RegistryRetrieveDTO> convertToDTO(List<Registry> allByActive) {
        List<RegistryRetrieveDTO> dtos = new ArrayList<>();

        for(Registry r : allByActive) {
            RegistryRetrieveDTO dto = new RegistryRetrieveDTO();
            dto.setId(r.getId());
            dto.setName(r.getName());
            dto.setActive(r.isActive());
            dto.setArchived(r.isArchived());
            dto.setCreatedon(r.getCreatedon());
            dto.setDescription(r.getDescription());
            dto.setRegistryFields(r.getRegistryFields());
            dto.setMedicalCases(r.getMedicalCases());
            dto.setNumberOfCases(r.getMedicalCases().size());

            dtos.add(dto);
        }

        return dtos;
    }

    /**
     * GET  /registries/:id : get the "id" registry.
     *
     * @param id the id of the registry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the registry, or with status 404 (Not Found)
     */
    @GetMapping("/registries/{id}")
    @Timed
    public ResponseEntity<Registry> getRegistry(@PathVariable Long id) {
        log.debug("REST request to get Registry : {}", id);
        Registry registry = registryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(registry));
    }

    /**
     * DELETE  /registries/:id : delete the "id" registry.
     *
     * @param id the id of the registry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/registries/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegistry(@PathVariable Long id) {
        log.debug("REST request to delete Registry : {}", id);
        registryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
