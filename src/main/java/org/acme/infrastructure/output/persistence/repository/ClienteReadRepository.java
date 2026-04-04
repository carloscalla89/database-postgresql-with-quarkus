package org.acme.infrastructure.output.persistence.repository;

import io.quarkus.hibernate.orm.PersistenceUnit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.cross.runtime.BasePanacheReadRepository;
import org.acme.infrastructure.output.persistence.entity.Client;

import java.util.UUID;

// --- Repositorio de Lectura para CLIENTE ---
@ApplicationScoped
public class ClienteReadRepository extends BasePanacheReadRepository<Client, UUID> {

    /*

    @Inject
    @PersistenceUnit("readonly") // Apunta a AlloyDB (PostgreSQL)
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Client findById(String id) {
        // Siempre recordamos agregar la validación del borrado lógico
        return find("id", UUID.fromString(id)).firstResult();
    }

     */

}
