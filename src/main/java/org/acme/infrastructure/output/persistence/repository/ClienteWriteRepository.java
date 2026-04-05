package org.acme.infrastructure.output.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.cross.runtime.BasePanacheWriteRepository;
import org.acme.infrastructure.output.persistence.entity.Client;

import java.util.UUID;

// --- Repositorio de Escritura para CLIENTE ---
@ApplicationScoped
public class ClienteWriteRepository extends BasePanacheWriteRepository<Client, UUID> {
    // Hereda registrar(), actualizar(), eliminar() y buscarPorId()
    // Aquí puedes agregar validaciones previas a la escritura si lo deseas.
}

