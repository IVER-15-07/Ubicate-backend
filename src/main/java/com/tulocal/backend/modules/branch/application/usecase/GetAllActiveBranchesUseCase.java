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

    private final JdbcBranchRepository jdbcBranchRepository;

    public List<BranchMapPointResponse> execute() {
        List<BranchEntity> entities = jdbcBranchRepository.findAllActiveBranchesRaw();

        return entities.stream()
                .map(e -> new BranchMapPointResponse(
                        e.getId(),
                        e.getNombre(),
                        e.getCategoryId(),
                        e.getCategoryName(),
                    e.getDireccion(),
                    e.getLogoUrl(),
                    e.getBannerUrl(),
                        e.getLat(),
                        e.getLng()))
                .toList();
    }
}