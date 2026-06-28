package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;
import com.tulocal.backend.modules.menu.domain.repository.BranchMenuRepository;
import com.tulocal.backend.common.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteMenuUseCase {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final BranchMenuRepository branchMenuRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public void execute(UUID menuId, UUID ownerUserId) {
        Menu menu = menuRepository.findById(menuId);
        if (menu == null)
            throw new IllegalArgumentException("El menú no existe");
        if (!menu.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No tienes permiso sobre este menú");

        // 1. borrar fotos de los items de Cloudinary
        menuItemRepository.findByMenuId(menuId).forEach(item -> {
            if (item.getPhotoUrl() != null)
                cloudinaryService.delete(item.getPhotoUrl());
        });

        // 2. borrar items
        menuItemRepository.deleteAllByMenuId(menuId);

        // 3. borrar relaciones branch_menus
        branchMenuRepository.deleteAllByMenuId(menuId);

        // 4. borrar el menú
        menuRepository.delete(menuId);
    }

}
