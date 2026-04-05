package org.acme.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.acme.cross.runtime.BaseAuditEntity;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "client")
public class Client extends BaseAuditEntity {

    // 1. Llave Primaria Centralizada
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    public String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    public String lastName;

    @Column(nullable = false, length = 100, unique = true)
    public String email;

    // Relación Bidireccional: 1 Cliente -> N Pedidos
    // 'mappedBy' apunta al nombre del campo en la clase Pedido.
    // 'cascade' permite que si guardas un Cliente con pedidos en la lista, se guarden ambos.
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<OrderClient> orderClients = new ArrayList<>();

    // Helper method recomendado para mantener la bidireccionalidad sincronizada
    public void agregarPedido(OrderClient orderClient) {
        orderClients.add(orderClient);
        orderClient.client = this;
    }

    public void removerPedido(OrderClient orderClient) {
        orderClients.remove(orderClient);
        orderClient.client = null;
    }
}
