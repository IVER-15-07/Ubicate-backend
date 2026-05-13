package com.tulocal.backend.modules.Business.api.controller;

import com.tulocal.backend.modules.Business.application.mapper.BusinessMapper;
import com.tulocal.backend.modules.Business.application.usecase.GetAllBusinessByIdUseCase;
import com.tulocal.backend.modules.Business.application.usecase.GetAllBusinessUseCase;
import com.tulocal.backend.modules.Business.application.usecase.SearchBusinessByNameUseCase;
import com.tulocal.backend.modules.Business.application.usecase.GetBusinessByCategoryUseCase;
import com.tulocal.backend.modules.Business.api.response.BusinessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tulocal.backend.common.ApiResponse;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {
    private final GetAllBusinessUseCase getAllBusinessUseCase;
    private final GetAllBusinessByIdUseCase getAllBusinessByIdUseCase;
    private final SearchBusinessByNameUseCase searchBusinessByNameUseCase;
    private final GetBusinessByCategoryUseCase getBusinessByCategoryUseCase;
    private final BusinessMapper businessMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BusinessResponse>>> getAllBusinesses() {
        List<BusinessResponse> responses = getAllBusinessUseCase.execute()
                .stream()
                .map(businessMapper::toResponse)
                .collect(Collectors.toList());
         return ResponseEntity.ok(ApiResponse.ok("Negocios obtenidos correctamente", responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusinessResponse>> getBusinessById(@PathVariable UUID id) {
        BusinessResponse response = businessMapper.toResponse(getAllBusinessByIdUseCase.execute(id));
        return ResponseEntity.ok(ApiResponse.ok("Negocio obtenido correctamente", response));
    }

    @GetMapping("/Buscar")
    public ResponseEntity<ApiResponse<List<BusinessResponse>>> searchBusinessByName(@RequestParam String nombre) {
        List<BusinessResponse> responses = searchBusinessByNameUseCase.execute(nombre)
                .stream()
                .map(businessMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Negocios encontrados", responses));
    }
    @GetMapping("/categoria/{categoryId}")
    public ResponseEntity<ApiResponse<List<BusinessResponse>>> getBusinessByCategory(@PathVariable Integer categoryId) {
        List<BusinessResponse> responses = getBusinessByCategoryUseCase.execute(categoryId)
                .stream()
                .map(businessMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Negocios de la categoría obtenidos correctamente", responses));
    }



}
