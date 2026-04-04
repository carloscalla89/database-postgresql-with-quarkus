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

        // 2. Llamas a tu nuevo método dinámico
        return executeNativeQueryToDto(
                sql,
                // 3. Este bloque es el "mapper". Define cómo se extraen los campos de este query específico.
                tuple -> {
                    // Recuerda: Db2 devuelve las columnas en MAYÚSCULAS
                    String idStr = tuple.get("ID", String.class);
                    BigDecimal total = tuple.get("TOTAL", BigDecimal.class);

                    return OrderDto.builder()
                            .id(idStr)
                            .mount(total)
                            .build();
                },
                // 4. Pasas los parámetros dinámicos
                clientId.toString()
        );


    }


}
