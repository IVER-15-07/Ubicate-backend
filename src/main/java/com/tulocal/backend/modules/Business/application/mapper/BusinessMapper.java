package com.tulocal.backend.modules.Business.application.mapper;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.api.response.BusinessResponse;
import com.tulocal.backend.modules.Business.api.response.LocationResponse;
import com.tulocal.backend.modules.Business.api.response.CategoryResponse;
import org.springframework.stereotype.Component;

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

        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setLat(business.getLat());
        locationResponse.setLng(business.getLng());
        locationResponse.setDireccion(business.getDireccion());

        response.setLocation(locationResponse);

        return response;

    }

}
