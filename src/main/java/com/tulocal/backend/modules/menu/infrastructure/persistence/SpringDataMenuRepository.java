package com.tulocal.backend.modules.menu.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface SpringDataMenuRepository extends JpaRepository<MenuEntity, UUID> {
    List<MenuEntity> findByOwnerUserId(UUID ownerUserId);
}