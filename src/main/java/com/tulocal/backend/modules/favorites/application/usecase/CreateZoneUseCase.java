package com.tulocal.backend.modules.favorites.application.usecase;

import com.tulocal.backend.modules.User.domain.repository.UserRepository;
import com.tulocal.backend.modules.favorites.api.request.CreateZonePolygonRequest;
import com.tulocal.backend.modules.favorites.domain.model.ZonePolygon;
import com.tulocal.backend.modules.favorites.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateZoneUseCase {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public ZonePolygon execute(CreateZonePolygonRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("No existe el usuario indicado");
        }
        return favoriteRepository.createZone(request.getUserId(), request.getNombre(), request.getCoordinates());
    }
}