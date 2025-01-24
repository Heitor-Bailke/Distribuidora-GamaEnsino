package com.fontenova.distribuidora.repository;

import com.fontenova.distribuidora.domain.Distribuidora;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Distribuidora entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistribuidoraRepository extends JpaRepository<Distribuidora, Integer> {}
