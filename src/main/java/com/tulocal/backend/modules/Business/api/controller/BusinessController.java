package com.tulocal.backend.modules.Business.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.Business.api.request.BusinessRequest;
import com.tulocal.backend.modules.Business.api.request.UpdateBusinessRequest;
import com.tulocal.backend.modules.Business.api.response.BusinessResponse;
import com.tulocal.backend.modules.Business.application.mapper.BusinessMapper;
import com.tulocal.backend.modules.Business.application.usecase.CreateBusinessUseCase;
import com.tulocal.backend.modules.Business.application.usecase.DeleteBusinessUseCase;
import com.tulocal.backend.modules.Business.application.usecase.GetAllBusinessByIdUseCase;
import com.tulocal.backend.modules.Business.application.usecase.GetAllBusinessUseCase;
import com.tulocal.backend.modules.Business.application.usecase.GetBusinessByCategoryUseCase;
import com.tulocal.backend.modules.Business.application.usecase.SearchBusinessByNameUseCase;
import com.tulocal.backend.modules.Business.application.usecase.UpdateBusinessUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {

    private final CreateBusinessUseCase createBusinessUseCase;
    private final UpdateBusinessUseCase updateBusinessUseCase;
    private final DeleteBusinessUseCase deleteBusinessUseCase;
    private final GetAllBusinessUseCase getAllBusinessUseCase;
    private final GetAllBusinessByIdUseCase getAllBusinessByIdUseCase;
    private final SearchBusinessByNameUseCase searchBusinessByNameUseCase;
    private final GetBusinessByCategoryUseCase getBusinessByCategoryUseCase;
    private final BusinessMapper businessMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<BusinessResponse>> createBusiness(@Valid @RequestBody BusinessRequest request) {
        BusinessResponse response = businessMapper.toResponse(createBusinessUseCase.execute(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Negocio creado correctamente", response));
    }

    @PutMapping("/{businessId}")
    public ResponseEntity<ApiResponse<BusinessResponse>> updateBusiness(
            @PathVariable UUID businessId,
            @Valid @RequestBody UpdateBusinessRequest request) {
        BusinessResponse response = businessMapper.toResponse(updateBusinessUseCase.execute(businessId, request));
        return ResponseEntity.ok(ApiResponse.ok("Negocio actualizado correctamente", response));
    }

    @DeleteMapping("/{businessId}")
    public ResponseEntity<ApiResponse<Void>> deleteBusiness(@PathVariable UUID businessId) {
        deleteBusinessUseCase.execute(businessId);
        return ResponseEntity.ok(ApiResponse.ok("Negocio dado de baja correctamente", null));
    }

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
