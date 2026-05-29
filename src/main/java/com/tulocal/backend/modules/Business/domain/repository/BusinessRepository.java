package com.tulocal.backend.modules.Business.domain.repository;

import java.util.List;
import java.util.UUID;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.model.Branch;
import com.tulocal.backend.modules.Business.domain.model.Menu;

public interface BusinessRepository {
    List<Business> findAll();
    Business findById(UUID id);
    List<Business> findByNombreContaining(String nombre);
    List<Business> searchByNombreOrDescripcion(String searchTerm);
    List<Business> findByCategoryId(Integer categoryId);
    Business save(Business business);
    Branch saveBranch(Branch branch);
    Menu saveMenu(Menu menu);
    com.tulocal.backend.modules.Business.domain.model.Menu findMenuById(java.util.UUID id);
    com.tulocal.backend.modules.Business.domain.model.MenuItem saveMenuItem(com.tulocal.backend.modules.Business.domain.model.MenuItem item);
    
}
