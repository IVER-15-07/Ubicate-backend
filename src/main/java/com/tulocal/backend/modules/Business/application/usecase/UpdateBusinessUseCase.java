package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.api.request.UpdateBusinessRequest;
import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateBusinessUseCase {

    private final BusinessRepository businessRepository;

    @Transactional
    public Business execute(UUID businessId, UpdateBusinessRequest request) {
        Business business = businessRepository.findById(businessId);
        if (business == null) {
            throw new IllegalArgumentException("No existe el negocio indicado");
        }

        business.setNombre(request.getNombre().trim());
        business.setDescripcion(request.getDescripcion());
        business.setCategoryId(request.getCategoryId());
        business.setLogoUrl(request.getLogoUrl());
        business.setBannerUrl(request.getBannerUrl());

        return businessRepository.update(business);
    }
}
