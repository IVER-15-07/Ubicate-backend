package com.tulocal.backend.modules.menu.domain.repository;

import java.util.UUID;
import java.util.List;
import com.tulocal.backend.modules.menu.domain.model.Menu;



public interface MenuRepository {

    Menu save(Menu menu);
    Menu findById(UUID id);
    List<Menu> findByOwnerUserId(UUID ownerUserId);
    Menu update(Menu menu);
    void delete(UUID id); 

}
