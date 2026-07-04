package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.api.response.BranchResponse;
import com.tulocal.backend.modules.branch.infrastructure.persistence.JdbcFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMyFavoritesUseCase {
    private final JdbcFavoriteRepository jdbcFavoriteRepository;

    public List<BranchResponse> execute(UUID userId) {
        return jdbcFavoriteRepository.findFavoriteBranches(userId);
    }
}