package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.api.request.CreateMenuRequest;
import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.model.Menu;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateMenuUseCase {

    private final BusinessRepository businessRepository;

    @Transactional
    public Menu execute(UUID businessId, CreateMenuRequest request) {
        Business business = businessRepository.findById(businessId);
        if (business == null) {
            throw new IllegalArgumentException("No existe el negocio indicado");
        }

        Set<UUID> validBranchIds = new LinkedHashSet<>();
        if (business.getBranches() != null) {
            business.getBranches().forEach(branch -> validBranchIds.add(branch.getId()));
        }

        Set<UUID> requestedBranchIds = new LinkedHashSet<>(request.getBranchIds());
        for (UUID branchId : requestedBranchIds) {
            if (!validBranchIds.contains(branchId)) {
                throw new IllegalArgumentException("Una o mas sucursales no pertenecen al negocio");
            }
        }

        Menu menu = new Menu();
        menu.setBusinessId(businessId);
        menu.setNombre(request.getNombre().trim());
        menu.setIsActive(request.getIsActive());
        menu.setBranchIds(new ArrayList<>(requestedBranchIds));

        return businessRepository.saveMenu(menu);
    }
}
