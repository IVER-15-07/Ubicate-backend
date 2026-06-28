package com.tulocal.backend.modules.menu.api.controller;

import com.tulocal.backend.common.response.ApiResponse;
import com.tulocal.backend.modules.menu.api.request.AddMenuItemRequest;
import com.tulocal.backend.modules.menu.api.request.CreateMenuRequest;
import com.tulocal.backend.modules.menu.api.request.UpdateMenuItemRequest;
import com.tulocal.backend.modules.menu.api.response.MenuItemResponse;
import com.tulocal.backend.modules.menu.api.response.MenuResponse;
import com.tulocal.backend.modules.menu.application.mapper.MenuItemMapper;
import com.tulocal.backend.modules.menu.application.mapper.MenuMapper;
import com.tulocal.backend.modules.menu.application.usecase.AddMenuItemUseCase;
import com.tulocal.backend.modules.menu.application.usecase.UpdateMenuItemUseCase;
import com.tulocal.backend.modules.menu.application.usecase.CreateMenuUseCase;
import com.tulocal.backend.modules.menu.application.usecase.GetMyMenusUseCase;
import com.tulocal.backend.modules.menu.application.usecase.DeleteMenuItemUseCase;
import com.tulocal.backend.modules.menu.application.usecase.DeleteMenuUseCase;
import com.tulocal.backend.security.jwt.TokenPayload;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

        private final CreateMenuUseCase createMenuUseCase;
        private final AddMenuItemUseCase addMenuItemUseCase;
        private final GetMyMenusUseCase getMyMenusUseCase;
        private final DeleteMenuUseCase deleteMenuUseCase;
        private final DeleteMenuItemUseCase deleteMenuItemUseCase;
        private final UpdateMenuItemUseCase updateMenuItemUseCase;
        private final MenuMapper menuMapper;
        private final MenuItemMapper menuItemMapper;

        @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
                        @Valid @ModelAttribute CreateMenuRequest request,
                        // fotos opcionales: photos[0], photos[1], photos[2] — una por item
                        @RequestParam(required = false) List<MultipartFile> photos,
                        @AuthenticationPrincipal TokenPayload tokenPayload) throws Exception {

                UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
                var result = createMenuUseCase.execute(request, ownerUserId, photos);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.ok("Menú creado correctamente",
                                                menuMapper.toResponse(result.menu(), result.items())));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteMenu(
                        @PathVariable UUID id,
                        @AuthenticationPrincipal TokenPayload tokenPayload) {

                UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
                deleteMenuUseCase.execute(id, ownerUserId);

                return ResponseEntity.ok(ApiResponse.ok("Menú eliminado correctamente", null));
        }

        @PutMapping(value = "/items/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<MenuItemResponse>> updateItem(
                        @PathVariable UUID id,
                        @Valid @ModelAttribute UpdateMenuItemRequest request,
                        @RequestParam(value = "photo", required = false) MultipartFile photo,
                        @AuthenticationPrincipal TokenPayload tokenPayload) throws Exception {

                UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
                var item = updateMenuItemUseCase.execute(id, request, photo, ownerUserId);

                return ResponseEntity.ok(ApiResponse.ok("Platillo actualizado correctamente",
                                menuItemMapper.toResponse(item)));
        }

        @GetMapping("/items/{id}")
        public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItemById(
                        @PathVariable UUID id,
                        @AuthenticationPrincipal TokenPayload tokenPayload) {

                UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
                var item = getMyMenusUseCase.execute(id, ownerUserId);

                return ResponseEntity.ok(ApiResponse.ok("Platillo encontrado",
                                menuItemMapper.toResponse(item)));
        }

        @DeleteMapping("/items/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteItem(
                        @PathVariable UUID id,
                        @AuthenticationPrincipal TokenPayload tokenPayload) {

                UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
                deleteMenuItemUseCase.execute(id, ownerUserId);

                return ResponseEntity.ok(ApiResponse.ok("Platillo eliminado correctamente", null));
        }

        @PostMapping(value = "/items/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<List<MenuItemResponse>>> addItems(
                        @Valid @ModelAttribute AddMenuItemRequest request,
                        @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
                        @AuthenticationPrincipal TokenPayload tokenPayload) throws Exception {

                UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
                var items = addMenuItemUseCase.execute(request, ownerUserId, photos);

                var response = items.stream().map(menuItemMapper::toResponse).toList();
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.ok("Platillos agregados correctamente", response));
        }

        @GetMapping("/my-menus")
        public ResponseEntity<ApiResponse<List<MenuResponse>>> getMyMenus(
                        @AuthenticationPrincipal TokenPayload tokenPayload) {

                UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
                List<MenuResponse> response = getMyMenusUseCase.execute(ownerUserId)
                                .stream()
                                .map(r -> menuMapper.toResponse(r.menu(), r.items()))
                                .toList();

                return ResponseEntity.ok(ApiResponse.ok("Tus menús", response));
        }
}