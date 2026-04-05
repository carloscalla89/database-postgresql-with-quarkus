package org.acme.infrastructure.output.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import org.acme.cross.dto.PageResponseDto;
import org.acme.cross.runtime.BasePanacheReadRepository;
import org.acme.infrastructure.input.rest.dto.ClientDto;
import org.acme.infrastructure.output.persistence.entity.Client;

import java.util.UUID;

// --- Repositorio de Lectura para CLIENTE ---
@ApplicationScoped
public class ClienteReadRepository extends BasePanacheReadRepository<Client, UUID> {

    public PageResponseDto<ClientDto> getAllClientsPaginated(int page, int limit) {

        // Simplemente le pasas el PanacheQuery, los datos de página, y tu lambda
        return getPaginatedResults(
                findAll(Sort.by("createdAt").descending()), // La consulta
                page,
                limit,
                client -> ClientDto.builder().id(client.getId().toString()).build() // El Mapper
        );
    }

    public PageResponseDto<ClientDto> getClientsSearchParamPaginated(String searchParam, int page, int limit) {

        PanacheQuery<Client> query = find(
                "(lower(firstName) like lower(?1) or lower(lastName) like lower(?1))",
                Sort.by("firstName").ascending(),
                searchParam
        );

        // Simplemente le pasas el PanacheQuery, los datos de página, y tu lambda
        return getPaginatedResults(
                query, // La consulta
                page,
                limit,
                client -> ClientDto.builder().id(client.getId().toString()).build() // El Mapper
        );
    }

}
