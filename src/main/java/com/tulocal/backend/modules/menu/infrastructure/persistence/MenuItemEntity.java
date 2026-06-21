package com.tulocal.backend.modules.menu.infrastructure.persistence;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "menu_items", schema = "public")
@Data

public class MenuItemEntity {

     @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "menu_id")
    private UUID menuId;

    private String nombre;

    @Column(columnDefinition = "text")
    private String descripcion;

    private BigDecimal precio;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
    
}
