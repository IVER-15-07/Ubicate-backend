package com.tulocal.backend.modules.branch.infrastructure.persistence;

import com.tulocal.backend.modules.branch.domain.model.Branch;
import com.tulocal.backend.modules.branch.domain.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryImpl implements BranchRepository {

    private final SpringDataBranchRepository springDataBranchRepository;

    private Branch toModel(BranchEntity entity) {

        Branch branch = new Branch();

        branch.setId(entity.getId());
        branch.setOwnerUserId(entity.getOwnerUserId());
        branch.setCategoryId(entity.getCategoryId());

        branch.setNombre(entity.getNombre());
        branch.setDescripcion(entity.getDescripcion());
        branch.setLogoUrl(entity.getLogoUrl());
        branch.setBannerUrl(entity.getBannerUrl());

        branch.setLat(entity.getLat());
        branch.setLng(entity.getLng());

        branch.setDireccion(entity.getDireccion());
        branch.setTelefono(entity.getTelefono());

        branch.setIsActive(entity.getIsActive());
        branch.setCreadoEn(entity.getCreadoEn());

        return branch;
    }

    @Override
    public Branch save(Branch branch) {
        // convertir Branch -> BranchEntity
        BranchEntity entity = new BranchEntity();
        entity.setOwnerUserId(branch.getOwnerUserId());
        entity.setCategoryId(branch.getCategoryId());

        entity.setNombre(branch.getNombre());
        entity.setDescripcion(branch.getDescripcion());
        entity.setLogoUrl(branch.getLogoUrl());
        entity.setBannerUrl(branch.getBannerUrl());
        entity.setLat(branch.getLat());
        entity.setLng(branch.getLng());
        entity.setDireccion(branch.getDireccion());
        entity.setTelefono(branch.getTelefono());
        entity.setIsActive(branch.getIsActive());
        entity.setCreadoEn(branch.getCreadoEn());

        BranchEntity saved = springDataBranchRepository.save(entity);

        // convertir BranchEntity -> Branch
        branch.setId(saved.getId());

        return branch;
    }

    @Override
    public List<Branch> findByOwnerUserId(UUID ownerUserId) {
        return springDataBranchRepository.findByOwnerUserId(ownerUserId)
                .stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public Branch findById(UUID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Branch update(Branch branch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(UUID id) {
        throw new UnsupportedOperationException();
    }

}