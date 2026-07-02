package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.api.response.BranchMapPointResponse;
import com.tulocal.backend.modules.branch.infrastructure.persistence.JdbcBranchRepository;
import com.tulocal.backend.modules.branch.infrastructure.persistence.BranchEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllActiveBranchesUseCase {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final JdbcBranchRepository jdbcBranchRepository;

    public List<BranchMapPointResponse> execute() {
        return mapBranches(jdbcBranchRepository.findAllActiveBranchesRaw(), null, null, null);
    }

    public List<BranchMapPointResponse> execute(Double userLat, Double userLng, Double radiusKm) {
        return mapBranches(jdbcBranchRepository.findAllActiveBranchesRaw(), userLat, userLng, radiusKm);
    }

    private List<BranchMapPointResponse> mapBranches(
            List<BranchEntity> entities,
            Double userLat,
            Double userLng,
            Double radiusKm) {

        return entities.stream()
                .filter(entity -> entity.getLat() != null && entity.getLng() != null)
                .map(entity -> {
                    Double distanceKm = null;
                    if (userLat != null && userLng != null) {
                        distanceKm = haversineKm(userLat, userLng, entity.getLat(), entity.getLng());
                    }

                    return new BranchMapPointResponse(
                            entity.getId(),
                            entity.getNombre(),
                            entity.getCategoryId(),
                            entity.getCategoryName(),
                            entity.getDireccion(),
                            entity.getLogoUrl(),
                            entity.getBannerUrl(),
                            distanceKm,
                            entity.getLat(),
                            entity.getLng());
                })
                .filter(response -> {
                    if (radiusKm == null || response.getDistanceKm() == null) {
                        return true;
                    }
                    return response.getDistanceKm() <= radiusKm;
                })
                .sorted((left, right) -> {
                    if (left.getDistanceKm() == null || right.getDistanceKm() == null) {
                        return 0;
                    }
                    return Double.compare(left.getDistanceKm(), right.getDistanceKm());
                })
                .toList();
    }

    private double haversineKm(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLng / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}