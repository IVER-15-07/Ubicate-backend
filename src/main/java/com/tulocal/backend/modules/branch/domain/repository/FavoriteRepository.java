package com.tulocal.backend.modules.branch.domain.repository;

import java.util.UUID;

public interface FavoriteRepository {
    void add(UUID userId, UUID branchId);
    void remove(UUID userId, UUID branchId);
    boolean isFavorite(UUID userId, UUID branchId);
}