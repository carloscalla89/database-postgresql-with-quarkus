package org.acme.domain;

import org.acme.infrastructure.input.rest.dto.ApiResponse;
import org.acme.infrastructure.input.rest.dto.ClientDto;

public interface ClientService {

    ApiResponse<ClientDto> createClient(ClientDto clientDto);
    ApiResponse<ClientDto> findClientsById(String id);
    ApiResponse<ClientDto> getClientsSearchParams(String searchParam, int page, int limit);
}
