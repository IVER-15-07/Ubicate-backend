package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.api.request.CreateBranchRequest;
import com.tulocal.backend.modules.branch.domain.model.Branch;
import com.tulocal.backend.modules.branch.domain.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateBranchUseCase {

    private final BranchRepository branchRepository;

    @Transactional
    public Branch execute(CreateBranchRequest request, UUID ownerUserId) {

        Branch branch = new Branch();
        branch.setOwnerUserId(ownerUserId);
        branch.setCategoryId(request.getCategoryId());
        branch.setNombre(request.getNombre().trim());
        branch.setDescripcion(request.getDescripcion());
        branch.setLogoUrl(request.getLogoUrl());
        branch.setBannerUrl(request.getBannerUrl());
        branch.setLat(request.getLat());
        branch.setLng(request.getLng());
        branch.setDireccion(request.getDireccion());
        branch.setTelefono(request.getTelefono());
        branch.setIsActive(false);       
        branch.setCreadoEn(java.time.LocalDateTime.now());

        return branchRepository.save(branch);
    }



    
}
