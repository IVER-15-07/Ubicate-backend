package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddFavoriteUseCase {
    private final FavoriteRepository favoriteRepository;

    public void execute(UUID userId, UUID branchId) {
        favoriteRepository.add(userId, branchId);
    }
}