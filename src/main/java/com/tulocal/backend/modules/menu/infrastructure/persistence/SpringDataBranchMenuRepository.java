package com.tulocal.backend.modules.menu.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface SpringDataBranchMenuRepository extends JpaRepository<BranchMenuEntity, UUID> {
    List<BranchMenuEntity> findByBranchId(UUID branchId);
    List<BranchMenuEntity> findByMenuId(UUID menuId);
    Optional<BranchMenuEntity> findByBranchIdAndMenuId(UUID branchId, UUID menuId);
}