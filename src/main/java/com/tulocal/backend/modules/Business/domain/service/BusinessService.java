package com.tulocal.backend.modules.Business.domain.service;


import com.tulocal.backend.modules.Business.domain.model.Business;
import java.util.List;
import java.util.UUID;

public interface BusinessService {
    List<Business> getAllBusinesses();
    Business getBusinessById(UUID id);
    
}
