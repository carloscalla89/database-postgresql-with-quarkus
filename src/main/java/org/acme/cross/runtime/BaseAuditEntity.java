package org.acme.cross.runtime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

// @MappedSuperclass le dice a Hibernate que esta clase no es una tabla en sí,
// sino que sus campos deben añadirse a las tablas de las clases hijas.
@Getter
@MappedSuperclass
public abstract class BaseAuditEntity extends PanacheEntityBase {

    // 1. Auditoría de Fechas (Autogeneradas por Hibernate)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    // 2. Bloqueo Optimista (Evita que 2 usuarios sobreescriban datos al mismo tiempo)
    @Version
    public Integer version;

    // 3. Auditoría de Usuarios (Opcional, pero muy recomendado)
    @Column(name = "created_by")
    public String createdBy;

    @Column(name = "updated_by")
    public String updatedBy;
}