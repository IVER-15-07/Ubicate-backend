package com.tulocal.backend.modules.details.application.usecase;

import com.tulocal.backend.modules.details.domain.model.BusinessDetail;
import com.tulocal.backend.modules.details.domain.repository.DetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetBusinessDetailByBranchUseCase {

    private final DetailsRepository detailsRepository;

    public BusinessDetail execute(UUID branchId) {
        return detailsRepository.getBusinessDetailsByBranchId(branchId);
    }
}