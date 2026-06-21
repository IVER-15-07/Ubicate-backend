package com.tulocal.backend.modules.menu.application.usecase;
import com.tulocal.backend.modules.menu.api.request.AssignMenuToBranchRequest;
import com.tulocal.backend.modules.menu.domain.model.BranchMenu;
import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.repository.BranchMenuRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import com.tulocal.backend.modules.branch.domain.model.Branch;
import com.tulocal.backend.modules.branch.domain.repository.BranchRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AssignMenuToBranchUseCase {
     private final BranchMenuRepository branchMenuRepository;
    private final BranchRepository branchRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public BranchMenu execute(AssignMenuToBranchRequest request, UUID ownerUserId) {

        Branch branch = branchRepository.findById(request.getBranchId());
        if (branch == null) {
            throw new IllegalArgumentException("La sucursal indicada no existe");
        }
        if (!branch.getOwnerUserId().equals(ownerUserId)) {
            throw new IllegalArgumentException("No tienes permiso sobre esta sucursal");
        }

        Menu menu = menuRepository.findById(request.getMenuId());
        if (menu == null) {
            throw new IllegalArgumentException("El menú indicado no existe");
        }
        if (!menu.getOwnerUserId().equals(ownerUserId)) {
            throw new IllegalArgumentException("No tienes permiso sobre este menú");
        }

        BranchMenu existing = branchMenuRepository.findByBranchIdAndMenuId(
                request.getBranchId(), request.getMenuId());
        if (existing != null) {
            throw new IllegalArgumentException("Este menú ya está asignado a esta sucursal");
        }

        BranchMenu branchMenu = new BranchMenu();
        branchMenu.setBranchId(request.getBranchId());
        branchMenu.setMenuId(request.getMenuId());
        branchMenu.setIsActive(true);
        branchMenu.setCreadoEn(LocalDateTime.now());

        return branchMenuRepository.save(branchMenu);
    }
    
}
