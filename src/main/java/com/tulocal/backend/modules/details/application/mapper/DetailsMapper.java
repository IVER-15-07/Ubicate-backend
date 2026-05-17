package com.tulocal.backend.modules.details.application.mapper;

import com.tulocal.backend.modules.details.domain.model.*;
import com.tulocal.backend.modules.details.api.response.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class DetailsMapper {

    public BusinessDetailResponse toResponse(BusinessDetail detail) {
        BusinessDetailResponse response = new BusinessDetailResponse();
        response.setId(detail.getId());
        response.setNombre(detail.getNombre());
        response.setDescripcion(detail.getDescripcion());
        response.setLogoUrl(detail.getLogoUrl());
        response.setBannerUrl(detail.getBannerUrl());
        response.setIsActive(detail.getIsActive());
        response.setCreadoEn(detail.getCreadoEn());

        CategoryDetailResponse category = new CategoryDetailResponse();
        category.setId(detail.getCategoryId());
        category.setNombre(detail.getCategoryNombre());
        response.setCategory(category);

        if (detail.getBranches() != null) {
            response.setBranches(detail.getBranches().stream()
                    .map(this::branchToResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private BranchDetailResponse branchToResponse(BranchDetail branch) {
        BranchDetailResponse response = new BranchDetailResponse();
        response.setId(branch.getId());
        response.setNombre(branch.getNombre());
        response.setCreadoEn(branch.getCreadoEn());

      
        if (branch.getLocation() != null) {
            LocationDetailResponse loc = new LocationDetailResponse();
            loc.setLat(branch.getLocation().getLat());
            loc.setLng(branch.getLocation().getLng());
            loc.setDireccion(branch.getLocation().getDireccion());
            response.setLocation(loc);
        }

        if (branch.getMenus() != null) {
            response.setMenus(branch.getMenus().stream()
                    .map(this::menuToResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private MenuDetailResponse menuToResponse(MenuDetail menu) {
        MenuDetailResponse response = new MenuDetailResponse();
        response.setId(menu.getId());
        response.setNombre(menu.getNombre());

        if (menu.getItems() != null) {
            response.setItems(menu.getItems().stream()
                    .map(this::menuItemToResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private MenuItemDetailResponse menuItemToResponse(MenuItemDetail item) {
        MenuItemDetailResponse response = new MenuItemDetailResponse();
        response.setId(item.getId());
        response.setNombre(item.getNombre());
        response.setDescripcion(item.getDescripcion());
        response.setPrecio(item.getPrecio());

        if (item.getImagenes() != null) {
            response.setImages(item.getImagenes().stream()
                    .map(img -> {
                        MenuImageDetailResponse ir = new MenuImageDetailResponse();
                        ir.setUrl(img.getUrl());

                        return ir;
                    }).collect(Collectors.toList()));
        }

        return response;
    }

}
