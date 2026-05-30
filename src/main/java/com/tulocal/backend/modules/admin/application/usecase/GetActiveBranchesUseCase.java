package com.tulocal.backend.modules.admin.application.usecase;

import com.tulocal.backend.modules.admin.domain.model.AdminBranch;
import com.tulocal.backend.modules.admin.domain.repository.AdminBranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetActiveBranchesUseCase {

    private final AdminBranchRepository adminBranchRepository;

    public List<AdminBranch> execute() {
        return adminBranchRepository.findByIsActiveTrue();
    }
}
