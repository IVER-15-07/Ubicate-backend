package com.tulocal.backend.modules.menu.infrastructure.persistence;

import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {

    private final SpringDataMenuRepository springDataMenuRepository;

    private Menu toModel(MenuEntity entity) {
        Menu menu = new Menu();
        menu.setId(entity.getId());
        menu.setOwnerUserId(entity.getOwnerUserId());
        menu.setNombre(entity.getNombre());
        menu.setIsActive(entity.getIsActive());
        menu.setCreadoEn(entity.getCreadoEn());
        return menu;
    }

    @Override
    public Menu save(Menu menu) {
        MenuEntity entity = new MenuEntity();
        entity.setOwnerUserId(menu.getOwnerUserId());
        entity.setNombre(menu.getNombre());
        entity.setIsActive(menu.getIsActive());
        entity.setCreadoEn(menu.getCreadoEn());

        MenuEntity saved = springDataMenuRepository.save(entity);
        menu.setId(saved.getId());
        return menu;
    }

    @Override
    public Menu findById(UUID id) {
        return springDataMenuRepository.findById(id)
                .map(this::toModel)
                .orElse(null);
    }

    @Override
    public List<Menu> findByOwnerUserId(UUID ownerUserId) {
        return springDataMenuRepository.findByOwnerUserId(ownerUserId)
                .stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public Menu update(Menu menu) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(UUID id) {
        springDataMenuRepository.deleteById(id);
    }
}