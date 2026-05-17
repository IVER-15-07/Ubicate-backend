package com.tulocal.backend.modules.details.api.controller;
import com.tulocal.backend.modules.details.application.mapper.DetailsMapper;
import com.tulocal.backend.modules.details.application.usecase.GetBusinessDetailByBranchUseCase;
import com.tulocal.backend.modules.details.application.usecase.GetBusinessDetailUseCase;
import com.tulocal.backend.modules.details.api.response.BusinessDetailResponse;
import com.tulocal.backend.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/api/details")
@RequiredArgsConstructor
public class DetailsController {
    
    private final GetBusinessDetailUseCase getBusinessDetailUseCase;
    private final GetBusinessDetailByBranchUseCase getBusinessDetailByBranchUseCase;
    private final DetailsMapper detailsMapper;

    @GetMapping("/{businessId}")
      public ResponseEntity<ApiResponse<BusinessDetailResponse>> getBusinessDetail(@PathVariable UUID businessId) {
        BusinessDetailResponse response = detailsMapper.toResponse(
            getBusinessDetailUseCase.execute(businessId)
        );
        return ResponseEntity.ok(ApiResponse.ok("Detalle obtenido correctamente", response));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<BusinessDetailResponse>> getBusinessDetailByBranch(@PathVariable UUID branchId) {
        BusinessDetailResponse response = detailsMapper.toResponse(
                getBusinessDetailByBranchUseCase.execute(branchId)
        );
        return ResponseEntity.ok(ApiResponse.ok("Detalle del negocio por branch obtenido correctamente", response));
    }

}
