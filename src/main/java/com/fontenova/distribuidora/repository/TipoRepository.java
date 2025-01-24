package com.fontenova.distribuidora.repository;

import com.fontenova.distribuidora.domain.Tipo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tipo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoRepository extends JpaRepository<Tipo, Integer> {}
