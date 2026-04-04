package org.acme.cross.runtime;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Repositorio base genérico de Nivel 1.
 * Sirve para CUALQUIER entidad (T), auditada o no auditada.
 */
public abstract class BasePanacheReadRepository<T, ID> implements PanacheRepositoryBase<T, ID> {

    // ==========================================
    // BUSCAR POR ID (Solo si está activo)
    // ==========================================
    public Optional<T> searchById(ID id) { // Ahora recibe el tipo dinámico 'ID'
        // Filtramos por el ID y aseguramos que el borrado lógico sea verdadero
        return find("id = ?1", id).firstResultOptional();
    }


    /**
     * Helper DINÁMICO para SQL Nativo que mapea el resultado a cualquier DTO.
     * * @param sql    La consulta SQL Nativa a ejecutar.
     * @param mapper Función Lambda que indica cómo convertir cada Tuple en el DTO deseado.
     * @param params Los parámetros para la consulta (varargs).
     * @param <R>    El tipo de DTO que se va a retornar.
     * @return       Lista del DTO mapeado.
     */
    @SuppressWarnings("unchecked")
    protected <R> List<R> executeNativeQueryToDto(String sql, Function<Tuple, R> mapper, Object... params) {

        // Creamos el query nativo pidiendo explícitamente un Tuple
        Query query = getEntityManager().createNativeQuery(sql, Tuple.class);

        // Asignamos los parámetros de forma dinámica
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }

        // Ejecutamos y obtenemos los resultados en bruto
        List<Tuple> results = query.getResultList();

        // Aplicamos la función mapper para transformar cada Tuple al DTO deseado
        return results.stream()
                .map(mapper)
                .toList();
    }
}
