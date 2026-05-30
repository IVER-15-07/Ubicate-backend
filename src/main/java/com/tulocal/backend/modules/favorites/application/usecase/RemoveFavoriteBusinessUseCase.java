package com.tulocal.backend.modules.favorites.application.usecase;

import com.tulocal.backend.modules.favorites.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RemoveFavoriteBusinessUseCase {

    private final FavoriteRepository favoriteRepository;

    public void execute(UUID userId, UUID businessId) {
        favoriteRepository.removeBusinessFavorite(userId, businessId);
    }
}