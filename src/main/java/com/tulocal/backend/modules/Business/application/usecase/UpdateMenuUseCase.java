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
public class UpdateMenuUseCase {

    private final BusinessRepository businessRepository;

    @Transactional
    public Menu execute(UUID businessId, UUID menuId, CreateMenuRequest request) {
        Business business = businessRepository.findById(businessId);
        if (business == null) {
            throw new IllegalArgumentException("No existe el negocio indicado");
        }

        Menu current = businessRepository.findMenuById(menuId);
        if (current == null) {
            throw new IllegalArgumentException("Menu no encontrado");
        }
        if (!current.getBusinessId().equals(businessId)) {
            throw new IllegalArgumentException("El menu no pertenece al negocio");
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
        menu.setId(menuId);
        menu.setBusinessId(businessId);
        menu.setNombre(request.getNombre().trim());
        menu.setIsActive(request.getIsActive() != null ? request.getIsActive() : current.getIsActive());
        menu.setBranchIds(new ArrayList<>(requestedBranchIds));

        return businessRepository.updateMenu(menu);
    }
}
