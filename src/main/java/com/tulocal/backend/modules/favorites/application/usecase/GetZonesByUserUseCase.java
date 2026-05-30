package com.tulocal.backend.modules.favorites.application.usecase;

import com.tulocal.backend.modules.favorites.domain.model.ZonePolygon;
import com.tulocal.backend.modules.favorites.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetZonesByUserUseCase {

    private final FavoriteRepository favoriteRepository;

    public List<ZonePolygon> execute(UUID userId) {
        return favoriteRepository.findZonesByUser(userId);
    }
}