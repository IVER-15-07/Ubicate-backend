package com.tulocal.backend.modules.branch.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
public interface SpringDataBranchRepository extends JpaRepository<BranchEntity, UUID> {

    List<BranchEntity> findByOwnerUserId(UUID ownerUserId);
    // Spring genera dinámicamente métodos como .save(), .deleteById(), .findById() aquí.
}