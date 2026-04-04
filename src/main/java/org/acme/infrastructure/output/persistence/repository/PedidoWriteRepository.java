package org.acme.infrastructure.output.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.cross.runtime.BasePanacheWriteRepository;
import org.acme.infrastructure.output.persistence.entity.OrderClient;

// --- Repositorio de Escritura para PEDIDO ---
@ApplicationScoped
public class PedidoWriteRepository extends BasePanacheWriteRepository<OrderClient> {
    // Hereda operaciones transaccionales hacia DB2
}
