package com.tulocal.backend.modules.branch.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface SpringDataFavoriteRepository extends JpaRepository<FavoriteEntity, UUID> {
    Optional<FavoriteEntity> findByUserIdAndBranchId(UUID userId, UUID branchId);
    boolean existsByUserIdAndBranchId(UUID userId, UUID branchId);
    void deleteByUserIdAndBranchId(UUID userId, UUID branchId);
}