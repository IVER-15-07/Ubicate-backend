package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RemoveFavoriteUseCase {
    private final FavoriteRepository favoriteRepository;
    @Transactional
    public void execute(UUID userId, UUID branchId) {
        favoriteRepository.remove(userId, branchId);
    }
}