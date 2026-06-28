package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.api.request.UpdateBranchRequest;
import com.tulocal.backend.modules.branch.domain.model.Branch;
import com.tulocal.backend.modules.branch.domain.repository.BranchRepository;
import com.tulocal.backend.common.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateBranchUseCase {

    private final BranchRepository branchRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public Branch execute(UUID branchId, UpdateBranchRequest request, UUID ownerUserId,
                          MultipartFile logo, MultipartFile banner) throws Exception {

        Branch branch = branchRepository.findById(branchId);
        if (branch == null)
            throw new IllegalArgumentException("La sucursal no existe");
        if (!branch.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No tienes permiso sobre esta sucursal");

        // si manda logo nuevo → borra el viejo y sube el nuevo
        if (logo != null && !logo.isEmpty()) {
            if (branch.getLogoUrl() != null)
                cloudinaryService.delete(branch.getLogoUrl());
            branch.setLogoUrl(cloudinaryService.upload(logo, "tulocal/branches/logos"));
        }

        // si manda banner nuevo → borra el viejo y sube el nuevo
        if (banner != null && !banner.isEmpty()) {
            if (branch.getBannerUrl() != null)
                cloudinaryService.delete(branch.getBannerUrl());
            branch.setBannerUrl(cloudinaryService.upload(banner, "tulocal/branches/banners"));
        }

        branch.setCategoryId(request.getCategoryId());
        branch.setNombre(request.getNombre().trim());
        branch.setDescripcion(request.getDescripcion());
        branch.setLat(request.getLat());
        branch.setLng(request.getLng());
        branch.setDireccion(request.getDireccion());
        branch.setTelefono(request.getTelefono());
        if (request.getIsActive() != null)
            branch.setIsActive(request.getIsActive());

        return branchRepository.update(branch);
    }
}