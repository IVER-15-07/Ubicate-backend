package com.tulocal.backend.modules.menu.domain.repository;
import java.util.UUID;
import java.util.List;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;

public interface MenuItemRepository {

    MenuItem save(MenuItem menuItem);
    MenuItem findById(UUID id);
    List<MenuItem> findByMenuId(UUID menuId);
    MenuItem update(MenuItem menuItem);
    void delete(UUID id);

}
