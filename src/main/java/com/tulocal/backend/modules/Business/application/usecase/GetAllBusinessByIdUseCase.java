package com.tulocal.backend.modules.Business.application.usecase;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;


@Component
@RequiredArgsConstructor


public class GetAllBusinessByIdUseCase {

    private final BusinessRepository businessRepository;

    public Business execute(UUID id) {
        return businessRepository.findById(id);
    }
    
}
