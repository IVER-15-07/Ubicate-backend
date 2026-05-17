package com.tulocal.backend.modules.details.domain.model;

import lombok.Data;
import java.util.UUID;
@Data
public class MenuImageDetail {
    private UUID id;
    private UUID menuItemId;
    private String url;
    
}
