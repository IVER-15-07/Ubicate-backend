package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor

public class GetMyMenusUseCase {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    public record MenuWithItems(Menu menu, java.util.List<MenuItem> items) {
    }

    public java.util.List<MenuWithItems> execute(UUID ownerUserId) {
        return menuRepository.findByOwnerUserId(ownerUserId)
                .stream()
                .map(menu -> new MenuWithItems(menu, menuItemRepository.findByMenuId(menu.getId())))
                .toList();
    }

}
