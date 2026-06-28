package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.api.request.UpdateMenuItemRequest;
import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;
import com.tulocal.backend.common.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public MenuItem execute(UUID itemId, UpdateMenuItemRequest request,
                            MultipartFile photo, UUID ownerUserId) throws Exception {

        MenuItem item = menuItemRepository.findById(itemId);
        if (item == null)
            throw new IllegalArgumentException("El platillo no existe");

        Menu menu = menuRepository.findById(item.getMenuId());
        if (menu == null || !menu.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No tienes permiso sobre este platillo");

        // solo actualiza lo que venga, ignora lo que sea null
        if (request.getNombre() != null && !request.getNombre().isBlank())
            item.setNombre(request.getNombre().trim());

        if (request.getDescripcion() != null)
            item.setDescripcion(request.getDescripcion());

        if (request.getPrecio() != null)
            item.setPrecio(request.getPrecio());


        // foto: solo si viene una nueva
        if (photo != null && !photo.isEmpty()) {
            if (item.getPhotoUrl() != null)
                cloudinaryService.delete(item.getPhotoUrl());
            item.setPhotoUrl(cloudinaryService.upload(photo, "tulocal/menu-items"));
        }

        return menuItemRepository.update(item);
    }
}