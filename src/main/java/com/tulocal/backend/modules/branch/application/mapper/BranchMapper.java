package com.tulocal.backend.modules.branch.application.mapper;

import com.tulocal.backend.modules.branch.domain.model.Branch;
import com.tulocal.backend.modules.branch.api.response.BranchResponse;

import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

public BranchResponse toResponse(Branch branch) {
    BranchResponse response = new BranchResponse();

    response.setId(branch.getId());
    response.setOwnerUserId(branch.getOwnerUserId());

    response.setCategoryId(branch.getCategoryId());
    response.setNombre(branch.getNombre());
    response.setDescripcion(branch.getDescripcion());
    response.setLogoUrl(branch.getLogoUrl());
    response.setBannerUrl(branch.getBannerUrl());
    response.setLat(branch.getLat());
    response.setLng(branch.getLng());
    response.setDireccion(branch.getDireccion());
    response.setTelefono(branch.getTelefono());
    response.setIsActive(branch.getIsActive());
    response.setCreadoEn(branch.getCreadoEn());

    return response;
}
    
}
