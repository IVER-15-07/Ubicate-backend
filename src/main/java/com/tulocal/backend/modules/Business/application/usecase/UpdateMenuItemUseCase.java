package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.api.request.CreateMenuItemRequest;
import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.model.Menu;
import com.tulocal.backend.modules.Business.domain.model.MenuImage;
import com.tulocal.backend.modules.Business.domain.model.MenuItem;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateMenuItemUseCase {

    private final BusinessRepository businessRepository;

    @Transactional
    public MenuItem execute(UUID businessId, UUID menuId, UUID itemId, CreateMenuItemRequest request) {
        Business business = businessRepository.findById(businessId);
        if (business == null) throw new IllegalArgumentException("No existe el negocio indicado");

        Menu menu = businessRepository.findMenuById(menuId);
        if (menu == null) throw new IllegalArgumentException("Menu no encontrado");
        if (!menu.getBusinessId().equals(businessId)) throw new IllegalArgumentException("El menu no pertenece al negocio");

        MenuItem current = businessRepository.findMenuItemById(itemId);
        if (current == null) throw new IllegalArgumentException("Menu item no encontrado");
        if (!current.getMenuId().equals(menuId)) throw new IllegalArgumentException("El menu item no pertenece al menu indicado");

        MenuItem item = new MenuItem();
        item.setId(itemId);
        item.setMenuId(menuId);
        item.setNombre(request.getNombre().trim());
        item.setDescripcion(request.getDescripcion());
        item.setPrecio(request.getPrecio());
        item.setPhotoUrl(request.getPhotoUrl());
        item.setIsActive(request.getIsActive() != null ? request.getIsActive() : current.getIsActive());

        ArrayList<String> imageUrls = new ArrayList<>();
        if (request.getImageUrls() != null) {
            for (String imageUrl : request.getImageUrls()) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    imageUrls.add(imageUrl.trim());
                }
            }
        }
        if (imageUrls.isEmpty() && request.getPhotoUrl() != null && !request.getPhotoUrl().trim().isEmpty()) {
            imageUrls.add(request.getPhotoUrl().trim());
        }

        if (!imageUrls.isEmpty()) {
            ArrayList<MenuImage> images = new ArrayList<>();
            for (int i = 0; i < imageUrls.size(); i++) {
                MenuImage image = new MenuImage();
                image.setUrl(imageUrls.get(i));
                image.setOrden(i + 1);
                images.add(image);
            }
            item.setImages(images);
        } else {
            item.setImages(current.getImages());
        }

        return businessRepository.updateMenuItem(item);
    }
}
