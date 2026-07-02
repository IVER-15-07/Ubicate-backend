package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.api.response.BranchDetailResponse;
import com.tulocal.backend.modules.branch.infrastructure.persistence.JdbcBranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetBranchDetailUseCase {

    private final JdbcBranchRepository jdbcBranchRepository;

    public BranchDetailResponse execute(UUID branchId) {
        return jdbcBranchRepository.getBranchDetail(branchId);
    }
}