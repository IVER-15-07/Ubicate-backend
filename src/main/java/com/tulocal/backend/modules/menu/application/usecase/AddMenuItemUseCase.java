package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.api.request.AddMenuItemRequest;
import com.tulocal.backend.modules.menu.api.request.CreateMenuItemRequest;
import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;
import com.tulocal.backend.common.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddMenuItemUseCase {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public List<MenuItem> execute(AddMenuItemRequest request, UUID ownerUserId,
            List<MultipartFile> photos) throws Exception {

        Menu menu = menuRepository.findById(request.getMenuId());
        if (menu == null)
            throw new IllegalArgumentException("El menú indicado no existe");
        if (!menu.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No tienes permiso sobre este menú");

        List<MenuItem> savedItems = new ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            CreateMenuItemRequest itemRequest = request.getItems().get(i);

            String photoUrl = null;
            if (photos != null && i < photos.size()) {
                MultipartFile photo = photos.get(i);
                if (photo != null && !photo.isEmpty()) {
                    photoUrl = cloudinaryService.upload(photo, "tulocal/menu-items");
                }
            }

            MenuItem item = new MenuItem();
            item.setMenuId(menu.getId());
            item.setNombre(itemRequest.getNombre().trim());
            item.setDescripcion(itemRequest.getDescripcion());
            item.setPrecio(itemRequest.getPrecio());
            item.setPhotoUrl(photoUrl);
            item.setIsActive(true);
            item.setCreadoEn(LocalDateTime.now());

            savedItems.add(menuItemRepository.save(item));
        }

        return savedItems;
    }
}