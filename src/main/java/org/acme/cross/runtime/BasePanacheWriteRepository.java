package org.acme.cross.runtime;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.Optional;

/**
 * Repositorio base genérico.
 * La restricción <T extends BaseAuditEntity> nos asegura que todas las entidades
 * que pasen por aquí tienen un campo 'id' y un campo 'isActive'.
 */
public abstract class BasePanacheWriteRepository<T> implements PanacheRepository<T> {

    // ==========================================
    // 2. REGISTRAR
    // ==========================================
    public void registrar(T entity) {
        // Aseguramos que entre como activo por defecto
        persist(entity);
    }

    // ==========================================
    // 3. ACTUALIZAR
    // ==========================================
    public T actualizar(T entity) {
        // En Hibernate, si una entidad está "desconectada" (ej. viene de un JSON),
        // se usa merge() para reconectarla y actualizarla en la base de datos.
        return getEntityManager().merge(entity);
    }

    // ==========================================
    // 4. ELIMINAR (Soft Delete)
    // ==========================================
    public boolean eliminar(Long id) {
        Optional<T> entityOpt = findByIdOptional(id);

        if (entityOpt.isPresent()) {
            T entity = entityOpt.get();
            // Al estar dentro de una transacción, el cambio a 'false'
            // se guardará automáticamente en la BD.
            return true;
        }
        return false;
    }

}
