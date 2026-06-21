package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.domain.model.BranchMenu;
import com.tulocal.backend.modules.menu.domain.repository.BranchMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor

public class ToggleBranchMenuUseCase {


    private final BranchMenuRepository branchMenuRepository;

    public BranchMenu execute(UUID branchMenuId, boolean isActive) {
        return branchMenuRepository.updateActive(branchMenuId, isActive);
    }
    
}
