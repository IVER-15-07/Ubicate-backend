package com.tulocal.backend.modules.branch.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites", schema = "public")
@Data
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "notify_offers")
    private Boolean notifyOffers = true;

    @Column(name = "notify_new_branch")
    private Boolean notifyNewBranch = true;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
}