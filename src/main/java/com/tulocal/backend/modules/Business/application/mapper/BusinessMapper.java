package com.tulocal.backend.modules.Business.application.mapper;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.model.Branch;
import com.tulocal.backend.modules.Business.api.response.BusinessResponse;
import com.tulocal.backend.modules.Business.api.response.BranchResponse;
import com.tulocal.backend.modules.Business.api.response.LocationResponse;
import com.tulocal.backend.modules.Business.api.response.CategoryResponse;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class BusinessMapper {

    public BusinessResponse toResponse(Business business) {

        BusinessResponse response = new BusinessResponse();
        response.setId(business.getId());
        response.setOwnerUserId(business.getOwnerUserId());
        response.setNombre(business.getNombre());
        response.setDescripcion(business.getDescripcion());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(business.getCategoryId());
        categoryResponse.setNombre(business.getCategoryNombre());

        response.setCategory(categoryResponse);

        response.setLogoUrl(business.getLogoUrl());
        response.setBannerUrl(business.getBannerUrl());
        response.setIsActive(business.getIsActive());
        response.setCreadoEn(business.getCreadoEn());

        // Mapear branches
        if (business.getBranches() != null) {
            response.setBranches(business.getBranches().stream()
                    .map(this::branchToResponse)
                    .collect(Collectors.toList()));
        }

        return response;

    }
    
    private BranchResponse branchToResponse(Branch branch) {
        BranchResponse response = new BranchResponse();
        response.setId(branch.getId());
        response.setBusinessId(branch.getBusinessId());
        response.setNombre(branch.getNombre());
        response.setCreadoEn(branch.getCreadoEn());
        // Mapear locations si existen
        if (branch.getLocations() != null) {
            response.setLocations(branch.getLocations().stream()
                    .map(loc -> {
                        LocationResponse lr = new LocationResponse();
                        lr.setLat(loc.getLat());
                        lr.setLng(loc.getLng());
                        lr.setDireccion(loc.getDireccion());
                        return lr;
                    }).collect(Collectors.toList()));
        }
        return response;
    }

}
