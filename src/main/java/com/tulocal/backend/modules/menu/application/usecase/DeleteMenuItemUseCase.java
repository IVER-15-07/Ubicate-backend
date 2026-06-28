package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import com.tulocal.backend.common.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public void execute(UUID itemId, UUID ownerUserId) {

        MenuItem item = menuItemRepository.findById(itemId);
        if (item == null)
            throw new IllegalArgumentException("El platillo no existe");

        var menu = menuRepository.findById(item.getMenuId());
        if (menu == null || !menu.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No tienes permiso sobre este platillo");

        if (item.getPhotoUrl() != null)
            cloudinaryService.delete(item.getPhotoUrl());

        menuItemRepository.delete(itemId);
    }
}