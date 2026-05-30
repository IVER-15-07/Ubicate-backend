package com.tulocal.backend.modules.admin.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tulocal.backend.modules.admin.domain.model.AdminBranch;
import com.tulocal.backend.modules.admin.domain.repository.AdminBranchRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApproveBranchUseCase {

    private final AdminBranchRepository adminBranchRepository;

    @Transactional
    public AdminBranch execute(UUID branchId) {
        AdminBranch branch = adminBranchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la sucursal indicada"));

        branch.setIsActive(true);
        return adminBranchRepository.save(branch);
    }
}