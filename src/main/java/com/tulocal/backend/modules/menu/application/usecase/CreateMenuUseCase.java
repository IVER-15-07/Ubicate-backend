package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.api.request.CreateMenuRequest;
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
public class CreateMenuUseCase {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final CloudinaryService cloudinaryService;

    public record MenuWithItems(Menu menu, List<MenuItem> items) {
    }

    @Transactional
    public MenuWithItems execute(CreateMenuRequest request, UUID ownerUserId,
            List<MultipartFile> photos) throws Exception {

        Menu menu = new Menu();
        menu.setOwnerUserId(ownerUserId);
        menu.setNombre(request.getNombre().trim());
        menu.setIsActive(true);
        menu.setCreadoEn(LocalDateTime.now());

        Menu savedMenu = menuRepository.save(menu);

        List<MenuItem> savedItems = new ArrayList<>();

        if (request.getItems() != null) {
            for (int i = 0; i < request.getItems().size(); i++) {
                CreateMenuItemRequest itemRequest = request.getItems().get(i);

                // si viene foto para este índice, subirla a Cloudinary
                String photoUrl = null;
                if (photos != null && i < photos.size()) {
                    MultipartFile photo = photos.get(i);
                    if (photo != null && !photo.isEmpty()) {
                        photoUrl = cloudinaryService.upload(photo, "tulocal/menu-items");
                    }
                }

                MenuItem item = new MenuItem();
                item.setMenuId(savedMenu.getId());
                item.setNombre(itemRequest.getNombre().trim());
                item.setDescripcion(itemRequest.getDescripcion());
                item.setPrecio(itemRequest.getPrecio());
                item.setPhotoUrl(photoUrl);
                item.setIsActive(true);
                item.setCreadoEn(LocalDateTime.now());

                savedItems.add(menuItemRepository.save(item));
            }
        }

        return new MenuWithItems(savedMenu, savedItems);
    }
}