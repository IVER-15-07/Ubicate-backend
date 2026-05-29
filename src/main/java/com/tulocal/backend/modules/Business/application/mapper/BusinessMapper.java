package com.tulocal.backend.modules.Business.application.mapper;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.model.Branch;
import com.tulocal.backend.modules.Business.domain.model.Menu;
import com.tulocal.backend.modules.Business.api.response.BusinessResponse;
import com.tulocal.backend.modules.Business.api.response.BranchResponse;
import com.tulocal.backend.modules.Business.api.response.LocationResponse;
import com.tulocal.backend.modules.Business.api.response.CategoryResponse;
import com.tulocal.backend.modules.Business.api.response.MenuResponse;
import com.tulocal.backend.modules.Business.api.response.MenuItemResponse;
import com.tulocal.backend.modules.Business.api.response.MenuImageResponse;
import com.tulocal.backend.modules.Business.domain.model.MenuItem;
import com.tulocal.backend.modules.Business.domain.model.MenuImage;
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
                .map(this::toBranchResponse)
                .collect(Collectors.toList()));
        }

        return response;

    }
    
    public BranchResponse toBranchResponse(Branch branch) {
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

    public MenuResponse toMenuResponse(Menu menu) {
        MenuResponse response = new MenuResponse();
        response.setId(menu.getId());
        response.setBusinessId(menu.getBusinessId());
        response.setNombre(menu.getNombre());
        response.setIsActive(menu.getIsActive());
        response.setCreadoEn(menu.getCreadoEn());
        response.setBranchIds(menu.getBranchIds());
        return response;
    }

    public MenuItemResponse toMenuItemResponse(MenuItem item) {
        MenuItemResponse r = new MenuItemResponse();
        r.setId(item.getId());
        r.setMenuId(item.getMenuId());
        r.setNombre(item.getNombre());
        r.setDescripcion(item.getDescripcion());
        r.setPrecio(item.getPrecio());
        r.setPhotoUrl(item.getPhotoUrl());
        r.setIsActive(item.getIsActive());
        r.setCreadoEn(item.getCreadoEn());
        if (item.getImages() != null) {
            r.setImages(item.getImages().stream().map(this::toMenuImageResponse).collect(Collectors.toList()));
        }
        return r;
    }

    private MenuImageResponse toMenuImageResponse(MenuImage image) {
        MenuImageResponse response = new MenuImageResponse();
        response.setId(image.getId());
        response.setUrl(image.getUrl());
        response.setOrden(image.getOrden());
        response.setCreadoEn(image.getCreadoEn());
        return response;
    }

}
