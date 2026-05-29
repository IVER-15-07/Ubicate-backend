package com.tulocal.backend.modules.Business.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MenuImage {
    private UUID id;
    private UUID menuItemId;
    private String url;
    private Integer orden;
    private LocalDateTime creadoEn;
}