package com.tulocal.backend.modules.menu.infrastructure.persistence;

import com.tulocal.backend.modules.menu.domain.model.BranchMenu;
import com.tulocal.backend.modules.menu.domain.repository.BranchMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BranchMenuRepositoryImpl implements BranchMenuRepository {

    private final SpringDataBranchMenuRepository springDataBranchMenuRepository;

    private BranchMenu toModel(BranchMenuEntity entity) {
        BranchMenu bm = new BranchMenu();
        bm.setId(entity.getId());
        bm.setBranchId(entity.getBranchId());
        bm.setMenuId(entity.getMenuId());
        bm.setIsActive(entity.getIsActive());
        bm.setCreadoEn(entity.getCreadoEn());
        return bm;
    }

    @Override
    public BranchMenu save(BranchMenu branchMenu) {
        BranchMenuEntity entity = new BranchMenuEntity();
        entity.setBranchId(branchMenu.getBranchId());
        entity.setMenuId(branchMenu.getMenuId());
        entity.setIsActive(branchMenu.getIsActive());
        entity.setCreadoEn(branchMenu.getCreadoEn());

        BranchMenuEntity saved = springDataBranchMenuRepository.save(entity);
        branchMenu.setId(saved.getId());
        return branchMenu;
    }

    @Override
    public BranchMenu findById(UUID id) {
        return springDataBranchMenuRepository.findById(id)
                .map(this::toModel)
                .orElse(null);
    }

    @Override
    public BranchMenu findByBranchIdAndMenuId(UUID branchId, UUID menuId) {
        return springDataBranchMenuRepository.findByBranchIdAndMenuId(branchId, menuId)
                .map(this::toModel)
                .orElse(null);
    }

    @Override
    public List<BranchMenu> findByBranchId(UUID branchId) {
        return springDataBranchMenuRepository.findByBranchId(branchId)
                .stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public List<BranchMenu> findByMenuId(UUID menuId) {
        return springDataBranchMenuRepository.findByMenuId(menuId)
                .stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public BranchMenu updateActive(UUID id, boolean isActive) {
        BranchMenuEntity entity = springDataBranchMenuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Relación menú-sucursal no encontrada"));
        entity.setIsActive(isActive);
        BranchMenuEntity saved = springDataBranchMenuRepository.save(entity);
        return toModel(saved);
    }

    @Override
    public void delete(UUID id) {
        springDataBranchMenuRepository.deleteById(id);
    }

    @Override
    public void deleteAllByMenuId(UUID menuId) {
        springDataBranchMenuRepository.deleteAllByMenuId(menuId);
    }
}