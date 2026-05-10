package com.tulocal.backend.modules.Business.api;

import com.tulocal.backend.modules.Business.application.BusinessMapFeed;
import com.tulocal.backend.modules.Business.application.BusinessMapService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
public class BusinessMapController {

    private final BusinessMapService businessMapService;

    public BusinessMapController(BusinessMapService businessMapService) {
        this.businessMapService = businessMapService;
    }

    @GetMapping("/businesses")
    public ResponseEntity<?> getBusinesses(
            @RequestParam(required = false) Double centerLat,
            @RequestParam(required = false) Double centerLon,
            @RequestParam(required = false) String centerLabel,
            @RequestParam(required = false) Integer radiusMeters,
            @RequestParam(required = false) Integer limit) {
        try {
            BusinessMapFeed feed = businessMapService.getMapFeed(centerLat, centerLon, centerLabel, radiusMeters, limit);
            return ResponseEntity.ok(feed);
        } catch (Exception exception) {
            return ResponseEntity.status(500).body(new ErrorResponse(
                    "Error al recuperar negocios del mapa",
                    exception.getMessage(),
                    "Verifica que la base de datos está conectada y que existen negocios con ubicación guardada"
            ));
        }
    }

    public record ErrorResponse(
            String error,
            String detalle,
            String sugerencia
    ) {
    }
}