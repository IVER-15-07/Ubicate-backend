package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.api.request.UpdateBranchRequest;
import com.tulocal.backend.modules.Business.domain.model.Branch;
import com.tulocal.backend.modules.Business.domain.model.Location;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateBranchUseCase {

    private final BusinessRepository businessRepository;

    @Transactional
    public Branch execute(UUID businessId, UUID branchId, UpdateBranchRequest request) {
        Branch current = businessRepository.findBranchById(branchId);
        if (current == null) {
            throw new IllegalArgumentException("No existe la sucursal indicada");
        }
        if (!businessId.equals(current.getBusinessId())) {
            throw new IllegalArgumentException("La sucursal no pertenece al negocio indicado");
        }

        Branch branch = new Branch();
        branch.setId(branchId);
        branch.setBusinessId(businessId);
        branch.setNombre(request.getNombre().trim());
        branch.setIsActive(current.getIsActive());

        Location location = new Location();
        location.setLat(request.getLat());
        location.setLng(request.getLng());
        location.setDireccion(request.getDireccion());
        branch.setLocations(new ArrayList<>());
        branch.getLocations().add(location);

        return businessRepository.updateBranch(branch);
    }
}
