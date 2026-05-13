package com.tulocal.backend.modules.Business.domain.repository;

import java.util.List;
import java.util.UUID;

import com.tulocal.backend.modules.Business.domain.model.Business;

public interface BusinessRepository {
    List<Business> findAll();
    Business findById(UUID id);
    List<Business> findByNombreContaining(String nombre);
    List<Business> searchByNombreOrDescripcion(String searchTerm);
    List<Business> findByCategoryId(Integer categoryId);
    
}
