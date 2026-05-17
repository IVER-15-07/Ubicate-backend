package com.tulocal.backend.modules.details.domain.service;

import com.tulocal.backend.modules.details.domain.model.BusinessDetail;
import java.util.UUID;

public interface DetailsService {
    BusinessDetail getBusinessDetails(UUID businessId);
    
}
