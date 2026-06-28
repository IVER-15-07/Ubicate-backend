package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.domain.model.Branch;
import com.tulocal.backend.modules.branch.domain.repository.BranchRepository;
import com.tulocal.backend.common.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteBranchUseCase {

    private final BranchRepository branchRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public void execute(UUID branchId, UUID ownerUserId) {

        Branch branch = branchRepository.findById(branchId);
        if (branch == null)
            throw new IllegalArgumentException("La sucursal no existe");
        if (!branch.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No tienes permiso sobre esta sucursal");

        if (branch.getLogoUrl() != null)
            cloudinaryService.delete(branch.getLogoUrl());
        if (branch.getBannerUrl() != null)
            cloudinaryService.delete(branch.getBannerUrl());

        branchRepository.delete(branchId);
    }
}