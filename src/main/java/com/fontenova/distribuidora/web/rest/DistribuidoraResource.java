package com.fontenova.distribuidora.web.rest;

import com.fontenova.distribuidora.domain.Distribuidora;
import com.fontenova.distribuidora.repository.DistribuidoraRepository;
import com.fontenova.distribuidora.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fontenova.distribuidora.domain.Distribuidora}.
 */
@RestController
@RequestMapping("/api/distribuidoras")
@Transactional
public class DistribuidoraResource {

    private static final Logger LOG = LoggerFactory.getLogger(DistribuidoraResource.class);

    private static final String ENTITY_NAME = "distribuidora";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DistribuidoraRepository distribuidoraRepository;

    public DistribuidoraResource(DistribuidoraRepository distribuidoraRepository) {
        this.distribuidoraRepository = distribuidoraRepository;
    }

    /**
     * {@code POST  /distribuidoras} : Create a new distribuidora.
     *
     * @param distribuidora the distribuidora to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new distribuidora, or with status {@code 400 (Bad Request)} if the distribuidora has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Distribuidora> createDistribuidora(@Valid @RequestBody Distribuidora distribuidora) throws URISyntaxException {
        LOG.debug("REST request to save Distribuidora : {}", distribuidora);
        if (distribuidora.getId() != null) {
            throw new BadRequestAlertException("A new distribuidora cannot already have an ID", ENTITY_NAME, "idexists");
        }
        distribuidora = distribuidoraRepository.save(distribuidora);
        return ResponseEntity.created(new URI("/api/distribuidoras/" + distribuidora.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, distribuidora.getId().toString()))
            .body(distribuidora);
    }

    /**
     * {@code PUT  /distribuidoras/:id} : Updates an existing distribuidora.
     *
     * @param id the id of the distribuidora to save.
     * @param distribuidora the distribuidora to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distribuidora,
     * or with status {@code 400 (Bad Request)} if the distribuidora is not valid,
     * or with status {@code 500 (Internal Server Error)} if the distribuidora couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Distribuidora> updateDistribuidora(
        @PathVariable(value = "id", required = false) final Integer id,
        @Valid @RequestBody Distribuidora distribuidora
    ) throws URISyntaxException {
        LOG.debug("REST request to update Distribuidora : {}, {}", id, distribuidora);
        if (distribuidora.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, distribuidora.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!distribuidoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        distribuidora = distribuidoraRepository.save(distribuidora);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, distribuidora.getId().toString()))
            .body(distribuidora);
    }

    /**
     * {@code PATCH  /distribuidoras/:id} : Partial updates given fields of an existing distribuidora, field will ignore if it is null
     *
     * @param id the id of the distribuidora to save.
     * @param distribuidora the distribuidora to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distribuidora,
     * or with status {@code 400 (Bad Request)} if the distribuidora is not valid,
     * or with status {@code 404 (Not Found)} if the distribuidora is not found,
     * or with status {@code 500 (Internal Server Error)} if the distribuidora couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Distribuidora> partialUpdateDistribuidora(
        @PathVariable(value = "id", required = false) final Integer id,
        @NotNull @RequestBody Distribuidora distribuidora
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Distribuidora partially : {}, {}", id, distribuidora);
        if (distribuidora.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, distribuidora.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!distribuidoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Distribuidora> result = distribuidoraRepository
            .findById(distribuidora.getId())
            .map(existingDistribuidora -> {
                if (distribuidora.getNome() != null) {
                    existingDistribuidora.setNome(distribuidora.getNome());
                }
                if (distribuidora.getCep() != null) {
                    existingDistribuidora.setCep(distribuidora.getCep());
                }
                if (distribuidora.getCidade() != null) {
                    existingDistribuidora.setCidade(distribuidora.getCidade());
                }
                if (distribuidora.getBairro() != null) {
                    existingDistribuidora.setBairro(distribuidora.getBairro());
                }
                if (distribuidora.getRua() != null) {
                    existingDistribuidora.setRua(distribuidora.getRua());
                }
                if (distribuidora.getNumero() != null) {
                    existingDistribuidora.setNumero(distribuidora.getNumero());
                }
                if (distribuidora.getReferencia() != null) {
                    existingDistribuidora.setReferencia(distribuidora.getReferencia());
                }
                if (distribuidora.getEstado() != null) {
                    existingDistribuidora.setEstado(distribuidora.getEstado());
                }
                if (distribuidora.getDetalhes() != null) {
                    existingDistribuidora.setDetalhes(distribuidora.getDetalhes());
                }

                return existingDistribuidora;
            })
            .map(distribuidoraRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, distribuidora.getId().toString())
        );
    }

    /**
     * {@code GET  /distribuidoras} : get all the distribuidoras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of distribuidoras in body.
     */
    @GetMapping("")
    public List<Distribuidora> getAllDistribuidoras() {
        LOG.debug("REST request to get all Distribuidoras");
        return distribuidoraRepository.findAll();
    }

    /**
     * {@code GET  /distribuidoras/:id} : get the "id" distribuidora.
     *
     * @param id the id of the distribuidora to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the distribuidora, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Distribuidora> getDistribuidora(@PathVariable("id") Integer id) {
        LOG.debug("REST request to get Distribuidora : {}", id);
        Optional<Distribuidora> distribuidora = distribuidoraRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(distribuidora);
    }

    /**
     * {@code DELETE  /distribuidoras/:id} : delete the "id" distribuidora.
     *
     * @param id the id of the distribuidora to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribuidora(@PathVariable("id") Integer id) {
        LOG.debug("REST request to delete Distribuidora : {}", id);
        distribuidoraRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
