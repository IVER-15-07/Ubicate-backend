package com.tulocal.backend.modules.favorites.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddFavoriteBusinessRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID businessId;

    private Boolean notifyOffers;
    private Boolean notifyNewBranch;
}
