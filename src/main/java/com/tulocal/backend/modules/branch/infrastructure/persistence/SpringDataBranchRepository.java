package com.tulocal.backend.modules.branch.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataBranchRepository extends JpaRepository<BranchEntity, UUID> {
    // Spring genera dinámicamente métodos como .save(), .deleteById(), .findById() aquí.
}