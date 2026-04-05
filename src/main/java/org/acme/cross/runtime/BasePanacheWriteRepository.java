package org.acme.cross.runtime;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.Optional;

/**
 * Repositorio base genérico de ESCRITURA.
 * Al agregar <T, ID>, permitimos que la entidad y su tipo de llave primaria sean dinámicos.
 */
// 2. IMPORTANTE: Agregamos ID a los genéricos e implementamos PanacheRepositoryBase
public abstract class BasePanacheWriteRepository<T, ID> implements PanacheRepositoryBase<T, ID> {

    // ==========================================
    // 2. REGISTRAR
    // ==========================================
    public void saveEntity(T entity) {
        // Aseguramos que entre como activo por defecto
        persist(entity);
    }

    // ==========================================
    // 3. ACTUALIZAR
    // ==========================================
    public T updateEntity(T entity) {
        // En Hibernate, si una entidad está "desconectada" (ej. viene de un JSON),
        // se usa merge() para reconectarla y actualizarla en la base de datos.
        return getEntityManager().merge(entity);
    }

    // ==========================================
    // 4. ELIMINAR (Borrado Físico)
    // ==========================================
    /**
     * Borrado FÍSICO dinámico.
     * @param id El identificador dinámico (UUID, Long, Integer, etc.).
     * @return true si se eliminó, false si no existía.
     */
    public boolean deleteEntity(ID id) {
        // Al heredar de PanacheRepositoryBase<T, ID>, deleteById(id)
        // ya entiende perfectamente qué tipo de dato le estás pasando.
        return deleteById(id);
    }

}
