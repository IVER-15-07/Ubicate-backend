package com.tulocal.backend.modules.menu.infrastructure.persistence;

import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final SpringDataMenuItemRepository springDataMenuItemRepository;

    private MenuItem toModel(MenuItemEntity entity) {
        MenuItem item = new MenuItem();
        item.setId(entity.getId());
        item.setMenuId(entity.getMenuId());
        item.setNombre(entity.getNombre());
        item.setDescripcion(entity.getDescripcion());
        item.setPrecio(entity.getPrecio());
        item.setPhotoUrl(entity.getPhotoUrl());
        item.setIsActive(entity.getIsActive());
        item.setCreadoEn(entity.getCreadoEn());
        return item;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemEntity entity = new MenuItemEntity();
        entity.setMenuId(menuItem.getMenuId());
        entity.setNombre(menuItem.getNombre());
        entity.setDescripcion(menuItem.getDescripcion());
        entity.setPrecio(menuItem.getPrecio());
        entity.setPhotoUrl(menuItem.getPhotoUrl());
        entity.setIsActive(menuItem.getIsActive());
        entity.setCreadoEn(menuItem.getCreadoEn());

        MenuItemEntity saved = springDataMenuItemRepository.save(entity);
        menuItem.setId(saved.getId());
        return menuItem;
    }

    @Override
    public MenuItem findById(UUID id) {
        return springDataMenuItemRepository.findById(id)
                .map(this::toModel)
                .orElse(null);
    }

    @Override
    public List<MenuItem> findByMenuId(UUID menuId) {
        return springDataMenuItemRepository.findByMenuId(menuId)
                .stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public MenuItem update(MenuItem menuItem) {
        MenuItemEntity entity = springDataMenuItemRepository.findById(menuItem.getId())
                .orElseThrow(() -> new IllegalArgumentException("El platillo no existe"));

        entity.setNombre(menuItem.getNombre());
        entity.setDescripcion(menuItem.getDescripcion());
        entity.setPrecio(menuItem.getPrecio());
        entity.setPhotoUrl(menuItem.getPhotoUrl());
        entity.setIsActive(menuItem.getIsActive());

        return toModel(springDataMenuItemRepository.save(entity));
    }

    @Override
    public void deleteAllByMenuId(UUID id) {
        springDataMenuItemRepository.deleteAllByMenuId(id);
    }

    @Override
    public void delete(UUID id) {
        springDataMenuItemRepository.deleteById(id);
    }
}