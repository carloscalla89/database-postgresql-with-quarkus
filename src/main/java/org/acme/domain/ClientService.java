package org.acme.domain;

import org.acme.infrastructure.input.rest.dto.ApiReponse;
import org.acme.infrastructure.input.rest.dto.ClientDto;

public interface ClientService {

    ApiReponse<ClientDto> createClient(ClientDto clientDto);
    ApiReponse<ClientDto> findClientsById(String id);
    ApiReponse<ClientDto> getClientsSearchParams(String searchParam, int page, int limit);
}
