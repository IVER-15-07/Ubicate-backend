package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.api.request.CreateMenuRequest;
import com.tulocal.backend.modules.menu.api.request.CreateMenuItemRequest;
import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;      

@Component
@RequiredArgsConstructor
public class CreateMenuUseCase {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    public record MenuWithItems(Menu menu, List<MenuItem> items) {}

    @Transactional
    public MenuWithItems execute(CreateMenuRequest request, UUID ownerUserId) {

        Menu menu = new Menu();
        menu.setOwnerUserId(ownerUserId);
        menu.setNombre(request.getNombre().trim());
        menu.setIsActive(false);
        menu.setCreadoEn(LocalDateTime.now());

        Menu savedMenu = menuRepository.save(menu);

        List<MenuItem> savedItems = new ArrayList<>();

        if (request.getItems() != null) {
            for (CreateMenuItemRequest itemRequest : request.getItems()) {
                MenuItem item = new MenuItem();
                item.setMenuId(savedMenu.getId());
                item.setNombre(itemRequest.getNombre().trim());
                item.setDescripcion(itemRequest.getDescripcion());
                item.setPrecio(itemRequest.getPrecio());
                item.setPhotoUrl(itemRequest.getPhotoUrl());
                item.setIsActive(true);
                item.setCreadoEn(LocalDateTime.now());

                savedItems.add(menuItemRepository.save(item));
            }
        }

        return new MenuWithItems(savedMenu, savedItems);
    }
}
