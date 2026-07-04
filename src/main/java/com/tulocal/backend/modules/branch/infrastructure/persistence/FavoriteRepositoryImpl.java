package com.tulocal.backend.modules.branch.infrastructure.persistence;

import com.tulocal.backend.modules.branch.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final SpringDataFavoriteRepository springDataFavoriteRepository;

    @Override
    public void add(UUID userId, UUID branchId) {
        if (springDataFavoriteRepository.existsByUserIdAndBranchId(userId, branchId)) return;
        FavoriteEntity entity = new FavoriteEntity();
        entity.setUserId(userId);
        entity.setBranchId(branchId);
        springDataFavoriteRepository.save(entity);
    }

    @Override
    public void remove(UUID userId, UUID branchId) {
        springDataFavoriteRepository.deleteByUserIdAndBranchId(userId, branchId);
    }

    @Override
    public boolean isFavorite(UUID userId, UUID branchId) {
        return springDataFavoriteRepository.existsByUserIdAndBranchId(userId, branchId);
    }
}