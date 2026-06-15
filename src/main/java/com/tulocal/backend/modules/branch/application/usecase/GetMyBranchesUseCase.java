package com.tulocal.backend.modules.branch.application.usecase;

import com.tulocal.backend.modules.branch.domain.model.Branch;
import com.tulocal.backend.modules.branch.domain.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetMyBranchesUseCase {

    private final BranchRepository branchRepository;

    public List<Branch> execute(UUID ownerUserId) {
        return branchRepository.findByOwnerUserId(ownerUserId);
    }
}