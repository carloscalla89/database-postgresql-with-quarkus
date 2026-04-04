package org.acme.cross.health;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;


import java.lang.annotation.Annotation;
import java.sql.Connection;

@Slf4j
@Readiness
@ApplicationScoped
public class GenericDatabaseHealthCheck implements HealthCheck {

    // @Any le dice a CDI: "Tráeme TODOS los beans de tipo AgroalDataSource que existan"
    // Instance<> es un iterador que nos permite evaluarlos en tiempo de ejecución
    @Inject
    @Any
    Instance<AgroalDataSource> dataSources;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Database Connections Health");
        boolean allDatabasesUp = true;

        // Si el programador no configuró ninguna base de datos, lo reportamos.
        if (dataSources.isUnsatisfied()) {
            return responseBuilder.up().withData("status", "No databases configured in application").build();
        }

        // Iteramos sobre cada datasource encontrado dinámicamente
        // Usamos handles() para poder leer los metadatos (como el nombre que le dio el programador)
        for (Instance.Handle<AgroalDataSource> handle : dataSources.handles()) {

            String dsName = extractDataSourceName(handle);
            AgroalDataSource dataSource = handle.get();

            boolean isUp = checkDatabase(dataSource);

            // Agregamos el resultado al JSON
            responseBuilder.withData("DS: " + dsName, isUp ? "UP" : "DOWN");

            if (!isUp) {
                allDatabasesUp = false;
            }
        }

        return allDatabasesUp ? responseBuilder.up().build() : responseBuilder.down().build();
    }

    /**
     * Extrae el nombre del Datasource leyendo las anotaciones CDI.
     */
    private String extractDataSourceName(Instance.Handle<AgroalDataSource> handle) {
        // Buscamos la anotación @DataSource("nombre")
        for (Annotation qualifier : handle.getBean().getQualifiers()) {
            if (qualifier instanceof DataSource) {
                return ((DataSource) qualifier).value();
            }
        }
        // Si no tiene la anotación, significa que es el datasource por defecto
        return "<default>";
    }

    /**
     * Valida la conexión de forma agnóstica al motor de base de datos.
     */
    private boolean checkDatabase(AgroalDataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            // Espera máximo 3 segundos por respuesta del servidor a nivel TCP
            return connection.isValid(3);
        } catch (Exception e) {
            log.error("Error checkDatabase:{}", e.getMessage());
            // Se captura cualquier error (Timeout, credenciales malas, red caída)
            return false;
        }
    }
}
