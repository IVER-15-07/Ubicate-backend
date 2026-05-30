package com.tulocal.backend.modules.favorites.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.favorites.application.mapper.FavoriteMapper;
import com.tulocal.backend.modules.favorites.application.usecase.AddFavoriteBusinessUseCase;
import com.tulocal.backend.modules.favorites.application.usecase.CreateZoneUseCase;
import com.tulocal.backend.modules.favorites.application.usecase.DeleteZoneUseCase;
import com.tulocal.backend.modules.favorites.application.usecase.GetBusinessesByZoneUseCase;
import com.tulocal.backend.modules.favorites.application.usecase.GetFavoriteBusinessesUseCase;
import com.tulocal.backend.modules.favorites.application.usecase.GetZonesByUserUseCase;
import com.tulocal.backend.modules.favorites.application.usecase.RemoveFavoriteBusinessUseCase;
import com.tulocal.backend.modules.favorites.api.request.AddFavoriteBusinessRequest;
import com.tulocal.backend.modules.favorites.api.request.CreateZonePolygonRequest;
import com.tulocal.backend.modules.favorites.api.response.FavoriteBusinessResponse;
import com.tulocal.backend.modules.favorites.api.response.ZonePolygonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final AddFavoriteBusinessUseCase addFavoriteBusinessUseCase;
    private final RemoveFavoriteBusinessUseCase removeFavoriteBusinessUseCase;
    private final GetFavoriteBusinessesUseCase getFavoriteBusinessesUseCase;
    private final CreateZoneUseCase createZoneUseCase;
    private final GetZonesByUserUseCase getZonesByUserUseCase;
    private final GetBusinessesByZoneUseCase getBusinessesByZoneUseCase;
    private final DeleteZoneUseCase deleteZoneUseCase;
    private final FavoriteMapper favoriteMapper;

    @PostMapping("/businesses")
    public ResponseEntity<ApiResponse<Void>> addFavoriteBusiness(@Valid @RequestBody AddFavoriteBusinessRequest request) {
        addFavoriteBusinessUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Negocio agregado a favoritos", null));
    }

    @GetMapping("/businesses")
    public ResponseEntity<ApiResponse<List<FavoriteBusinessResponse>>> getFavoriteBusinesses(@RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok("Favoritos obtenidos correctamente", favoriteMapper.mapBusinesses(getFavoriteBusinessesUseCase.execute(userId))));
    }

    @DeleteMapping("/businesses/{businessId}")
    public ResponseEntity<ApiResponse<Void>> removeFavoriteBusiness(@RequestParam UUID userId,
                                                                    @PathVariable UUID businessId) {
        removeFavoriteBusinessUseCase.execute(userId, businessId);
        return ResponseEntity.ok(ApiResponse.ok("Negocio eliminado de favoritos", null));
    }

    @PostMapping("/zones")
    public ResponseEntity<ApiResponse<ZonePolygonResponse>> createZone(@Valid @RequestBody CreateZonePolygonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Zona favorita creada correctamente", favoriteMapper.toZoneResponse(createZoneUseCase.execute(request))));
    }

    @GetMapping("/zones")
    public ResponseEntity<ApiResponse<List<ZonePolygonResponse>>> getZonesByUser(@RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok("Zonas favoritas obtenidas correctamente", favoriteMapper.mapZones(getZonesByUserUseCase.execute(userId))));
    }

    @GetMapping("/zones/{zoneId}/businesses")
    public ResponseEntity<ApiResponse<List<FavoriteBusinessResponse>>> getBusinessesByZone(@PathVariable UUID zoneId) {
        return ResponseEntity.ok(ApiResponse.ok("Negocios de la zona obtenidos correctamente", favoriteMapper.mapBusinesses(getBusinessesByZoneUseCase.execute(zoneId))));
    }

    @DeleteMapping("/zones/{zoneId}")
    public ResponseEntity<ApiResponse<Void>> deleteZone(@PathVariable UUID zoneId) {
        deleteZoneUseCase.execute(zoneId);
        return ResponseEntity.ok(ApiResponse.ok("Zona dada de baja correctamente", null));
    }
}
