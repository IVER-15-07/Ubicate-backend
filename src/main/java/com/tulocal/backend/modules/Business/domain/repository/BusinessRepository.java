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
    Business update(Business business);
    void deleteBusiness(UUID id);
    Branch saveBranch(Branch branch);
    Branch findBranchById(UUID id);
    Branch updateBranch(Branch branch);
    void deleteBranch(UUID branchId);
    Menu saveMenu(Menu menu);
    Menu updateMenu(Menu menu);
    void deleteMenu(UUID menuId);
    com.tulocal.backend.modules.Business.domain.model.Menu findMenuById(java.util.UUID id);
    com.tulocal.backend.modules.Business.domain.model.MenuItem saveMenuItem(com.tulocal.backend.modules.Business.domain.model.MenuItem item);
    com.tulocal.backend.modules.Business.domain.model.MenuItem findMenuItemById(java.util.UUID itemId);
    com.tulocal.backend.modules.Business.domain.model.MenuItem updateMenuItem(com.tulocal.backend.modules.Business.domain.model.MenuItem item);
    void deleteMenuItem(UUID itemId);
    
}
