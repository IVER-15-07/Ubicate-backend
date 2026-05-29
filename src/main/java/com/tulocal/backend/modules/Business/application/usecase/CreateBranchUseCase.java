package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.api.request.CreateBranchRequest;
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
public class CreateBranchUseCase {

    private final BusinessRepository businessRepository;

    @Transactional
    public Branch execute(UUID businessId, CreateBranchRequest request) {
        if (businessRepository.findById(businessId) == null) {
            throw new IllegalArgumentException("No existe el negocio indicado");
        }

        Branch branch = new Branch();
        branch.setBusinessId(businessId);
        branch.setNombre(request.getNombre().trim());

        Location location = new Location();
        location.setLat(request.getLat());
        location.setLng(request.getLng());
        if (request.getDireccion() != null) {
            location.setDireccion(request.getDireccion().trim());
        }

        branch.setLocations(new ArrayList<>());
        branch.getLocations().add(location);

        return businessRepository.saveBranch(branch);
    }
}