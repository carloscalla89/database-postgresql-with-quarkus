package org.acme.cross.error;

import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.jboss.logging.Logger;

// La anotación @Provider es obligatoria para que Quarkus registre esta clase
@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

    // Usamos el logger para guardar el error real en nuestros servidores
    private static final Logger LOG = Logger.getLogger(PersistenceExceptionMapper.class);

    @Override
    public Response toResponse(PersistenceException exception) {

        // 1. Logueamos el error técnico real para el equipo de desarrollo
        LOG.error("Error en la capa de persistencia: ", exception);

        // 2. Extraemos la causa específica de Hibernate
        Throwable cause = exception.getCause();

        // 3. Evaluamos el tipo de error para dar una respuesta adecuada al cliente

        // Caso A: La base de datos está caída o la red falló
        if (cause instanceof JDBCConnectionException) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(ErrorResponse.of(
                            503,
                            "Service Unavailable",
                            "El servicio de base de datos no está disponible en este momento. Intente más tarde."
                    ))
                    .build();
        }

        // Caso B: Violación de restricciones (ej. UNIQUE email, o Foreign Key no existe)
        if (cause instanceof ConstraintViolationException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(ErrorResponse.of(
                            409,
                            "Conflict",
                            "La operación no se pudo completar porque genera un conflicto de datos (ej. registro duplicado)."
                    ))
                    .build();
        }

        // Caso C: Cualquier otro error (SQL mal formado, tabla no existe, etc.)
        // Retornamos un 500 genérico para no exponer información técnica.
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorResponse.of(
                        500,
                        "Internal Server Error",
                        "Ocurrió un error inesperado al procesar los datos en el servidor."
                ))
                .build();
    }
}
