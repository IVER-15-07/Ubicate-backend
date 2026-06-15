package com.tulocal.backend.modules.branch.infrastructure.persistence;

import com.tulocal.backend.modules.branch.domain.model.Branch;
import com.tulocal.backend.modules.branch.domain.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryImpl implements BranchRepository {

    private final SpringDataBranchRepository springDataBranchRepository;

    @Override
    public Branch save(Branch branch) {
        // convertir Branch -> BranchEntity
        BranchEntity entity = new BranchEntity();

        entity.setNombre(branch.getNombre());
        entity.setDescripcion(branch.getDescripcion());
        entity.setLat(branch.getLat());
        entity.setLng(branch.getLng());
        entity.setDireccion(branch.getDireccion());
        entity.setTelefono(branch.getTelefono());

        BranchEntity saved =
                springDataBranchRepository.save(entity);

        // convertir BranchEntity -> Branch
        branch.setId(saved.getId());

        return branch;
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