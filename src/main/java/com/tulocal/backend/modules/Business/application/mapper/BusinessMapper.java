package com.tulocal.backend.modules.Business.application.mapper;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.api.response.BusinessResponse;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {

    public BusinessResponse toResponse(Business business) {

        BusinessResponse response = new BusinessResponse();
        response.setId(business.getId());
        response.setOwnerUserId(business.getOwnerUserId());
        response.setNombre(business.getNombre());
        response.setDescripcion(business.getDescripcion());
        response.setCategoryId(business.getCategoryId());
        response.setLogoUrl(business.getLogoUrl());
        response.setBannerUrl(business.getBannerUrl());
        response.setIsActive(business.getIsActive());
        response.setCreadoEn(business.getCreadoEn());

        return response;

    }

}
