package com.tulocal.backend.modules.favorites.application.usecase;

import com.tulocal.backend.modules.favorites.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteZoneUseCase {

    private final FavoriteRepository favoriteRepository;

    public void execute(UUID zoneId) {
        favoriteRepository.deleteZone(zoneId);
    }
}