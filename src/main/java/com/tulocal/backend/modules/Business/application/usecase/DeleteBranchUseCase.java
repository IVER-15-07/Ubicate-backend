package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteBranchUseCase {

    private final BusinessRepository businessRepository;

    public void execute(UUID branchId) {
        businessRepository.deleteBranch(branchId);
    }
}
