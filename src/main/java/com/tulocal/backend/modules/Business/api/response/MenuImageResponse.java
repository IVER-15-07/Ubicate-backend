package com.tulocal.backend.modules.Business.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MenuImageResponse {
    private UUID id;
    private String url;
    private Integer orden;
    private LocalDateTime creadoEn;
}