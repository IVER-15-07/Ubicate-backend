package com.tulocal.backend.modules.superAdmin.domain.repository;

import com.tulocal.backend.modules.superAdmin.domain.model.MapCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MapCategoryRepository extends JpaRepository<MapCategory, Integer> {
    List<MapCategory> findAllByOrderByNombreAsc();

    Optional<MapCategory> findByNombreIgnoreCase(String nombre);
}
