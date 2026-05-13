package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchBusinessByNameUseCase {

    private final BusinessRepository businessRepository;

    public List<Business> execute(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return businessRepository.findAll();
        }
        return businessRepository.searchByNombreOrDescripcion(nombre.trim());
    }
}
