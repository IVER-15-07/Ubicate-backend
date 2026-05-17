package com.tulocal.backend.modules.details.domain.repository;


import com.tulocal.backend.modules.details.domain.model.BusinessDetail;
import java.util.UUID;

public interface DetailsRepository {
    
    BusinessDetail getBusinessDetails(UUID businessId);
}
