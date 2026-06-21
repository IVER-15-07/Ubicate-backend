package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.domain.model.BranchMenu;
import com.tulocal.backend.modules.menu.domain.repository.BranchMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetMenusByBranchUseCase {

    private final BranchMenuRepository branchMenuRepository;

    public List<BranchMenu> execute(UUID branchId) {
        return branchMenuRepository.findByBranchId(branchId);
    }
    
}
