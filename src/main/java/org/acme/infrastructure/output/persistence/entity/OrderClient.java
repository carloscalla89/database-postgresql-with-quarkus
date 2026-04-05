package org.acme.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.acme.cross.runtime.BaseAuditEntity;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "order_client")
public class OrderClient extends BaseAuditEntity {

    // 1. Llave Primaria Centralizada
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Mapeo del DECIMAL(10, 2) de tu script Flyway
    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal total;

    @Column(name = "notes", columnDefinition = "TEXT")
    public String notes;

    // Relación Bidireccional: N Pedidos -> 1 Cliente
    // FetchType.LAZY es una mejor práctica de rendimiento; evita cargar el cliente
    // desde la base de datos a menos que explícitamente llames a pedido.cliente.nombre
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    public Client client;
}
