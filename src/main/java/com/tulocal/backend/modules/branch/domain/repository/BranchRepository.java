package com.tulocal.backend.modules.branch.domain.repository;

import java.util.UUID;
import java.util.List;
import com.tulocal.backend.modules.branch.domain.model.Branch;


public interface BranchRepository {

    Branch save(Branch branch);
    Branch findById(UUID id);
    List<Branch> findByOwnerUserId(UUID ownerUserId);
    Branch update(Branch branch);
    void delete(UUID id);

    
}
