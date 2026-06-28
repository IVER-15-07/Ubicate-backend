package com.tulocal.backend.modules.menu.domain.repository;

import java.util.UUID;
import java.util.List;
import com.tulocal.backend.modules.menu.domain.model.BranchMenu;

public interface BranchMenuRepository {

    BranchMenu save(BranchMenu branchMenu);
    BranchMenu findById(UUID id);
    BranchMenu findByBranchIdAndMenuId(UUID branchId, UUID menuId);
    List<BranchMenu> findByBranchId(UUID branchId);
    List<BranchMenu> findByMenuId(UUID menuId);
    BranchMenu updateActive(UUID id, boolean isActive);
    void delete(UUID id);
    void deleteAllByMenuId(UUID menuId);

}
