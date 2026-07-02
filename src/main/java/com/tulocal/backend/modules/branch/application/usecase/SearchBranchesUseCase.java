package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.api.response.BranchMapPointResponse;
import com.tulocal.backend.modules.branch.infrastructure.persistence.JdbcBranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchBranchesUseCase {

    private final JdbcBranchRepository jdbcBranchRepository;

    public List<BranchMapPointResponse> execute(String query) {
        if (query == null || query.isBlank())
            return List.of();

        return jdbcBranchRepository.searchBranches(query.trim())
                .stream()
                .map(e -> new BranchMapPointResponse(
                        e.getId(),
                        e.getNombre(),
                        e.getCategoryId(),
                        e.getCategoryName(),
                        e.getDireccion(),
                        e.getLogoUrl(),
                        e.getBannerUrl(),
                        null,
                        e.getLat(),
                        e.getLng()))
                .toList();
    }
}