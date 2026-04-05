package org.acme.infrastructure.output.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;

import org.acme.cross.runtime.BasePanacheReadRepository;
import org.acme.infrastructure.input.rest.dto.OrderDto;
import org.acme.infrastructure.output.persistence.entity.OrderClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// --- Repositorio de Lectura para PEDIDO ---
@ApplicationScoped
public class PedidoReadRepository extends BasePanacheReadRepository<OrderClient, UUID> {


    // ==========================================
    // MÉTODO 1: Usando Panache / HQL (El más limpio)
    // ==========================================
    public List<OrderClient> findOrdersByClientIdHql(UUID clientId) {
        // HQL navega por el objeto Java, por eso usamos "client.id" y "isActive"
        return find("client.id = ?1 and isActive = true", clientId).list();
    }


    /**
     * Obtiene solo el ID y el Total usando SQL Nativo y mapeándolo a un DTO.
     */
    public List<OrderDto> findOrdersByClientIdUsingDto(UUID clientId) {

        // 1. Defines tu SQL con los campos específicos
        String sql = "SELECT id, total FROM order_client WHERE client_id = ?1";

        return executeNativeQueryToDto(
                sql,
                tuple -> {
                    // CAMBIO 1: PostgreSQL devuelve minúsculas ("id", "total").
                    // CAMBIO 2: Extraemos el "id" como java.util.UUID.class, y luego le hacemos .toString() para el DTO.
                    String idStr = tuple.get("id", java.util.UUID.class).toString();
                    BigDecimal total = tuple.get("total", BigDecimal.class);

                    return OrderDto.builder()
                            .id(idStr)
                            .mount(total)
                            .build();
                },
                // CAMBIO 3: Pasamos el objeto UUID directamente, SIN el .toString().
                // Hibernate es lo suficientemente inteligente para mapear el objeto UUID de Java al tipo UUID de Postgres.
                clientId
        );


    }


}
