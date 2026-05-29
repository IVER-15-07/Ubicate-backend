package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.api.request.BusinessRequest;
import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CreateBusinessUseCase {

    private final BusinessRepository businessRepository;

    public Business execute(BusinessRequest request) {
        Business business = new Business();
        business.setOwnerUserId(request.getOwnerUserId());
        business.setNombre(request.getNombre().trim());
        business.setDescripcion(request.getDescripcion());
        business.setCategoryId(request.getCategoryId());
        business.setLogoUrl(request.getLogoUrl());
        business.setBannerUrl(request.getBannerUrl());
        business.setBranches(new ArrayList<>());

        return businessRepository.save(business);
    }
}