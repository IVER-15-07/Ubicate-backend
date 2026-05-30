package com.tulocal.backend.modules.favorites.application.usecase;

import com.tulocal.backend.modules.favorites.domain.model.FavoriteBusiness;
import com.tulocal.backend.modules.favorites.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetBusinessesByZoneUseCase {

    private final FavoriteRepository favoriteRepository;

    public List<FavoriteBusiness> execute(UUID zoneId) {
        return favoriteRepository.findBusinessesByZoneId(zoneId);
    }
}