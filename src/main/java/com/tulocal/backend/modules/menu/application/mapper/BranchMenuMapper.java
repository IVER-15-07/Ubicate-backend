package com.tulocal.backend.modules.menu.application.mapper;

import com.tulocal.backend.modules.menu.domain.model.BranchMenu;
import com.tulocal.backend.modules.menu.api.response.BranchMenuResponse;
import org.springframework.stereotype.Component;

@Component
public class BranchMenuMapper {
    public BranchMenuResponse toResponse(BranchMenu branchMenu) {
        BranchMenuResponse response = new BranchMenuResponse();
        response.setId(branchMenu.getId());
        response.setBranchId(branchMenu.getBranchId());
        response.setMenuId(branchMenu.getMenuId());
        response.setIsActive(branchMenu.getIsActive());
        response.setCreadoEn(branchMenu.getCreadoEn());
        return response;
    }
    
}
