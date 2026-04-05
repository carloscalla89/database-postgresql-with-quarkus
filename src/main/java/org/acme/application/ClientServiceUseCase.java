package org.acme.application;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.acme.cross.dto.PageResponseDto;
import org.acme.domain.ClientService;
import org.acme.infrastructure.input.rest.dto.ApiReponse;
import org.acme.infrastructure.input.rest.dto.ClientDto;
import org.acme.infrastructure.mapper.ClientMapper;
import org.acme.infrastructure.output.persistence.entity.Client;
import org.acme.infrastructure.output.persistence.repository.ClienteReadRepository;
import org.acme.infrastructure.output.persistence.repository.ClienteWriteRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class ClientServiceUseCase implements ClientService {

    private final ClienteReadRepository clienteReadRepo;
    private final ClienteWriteRepository clienteWriteRepo;
    private final ClientMapper clientMapper;

    public ClientServiceUseCase(ClienteReadRepository clienteReadRepo, ClienteWriteRepository clienteWriteRepo,
                                ClientMapper clientMapper) {
        this.clienteReadRepo = clienteReadRepo;
        this.clienteWriteRepo = clienteWriteRepo;
        this.clientMapper = clientMapper;
    }

    @Transactional
    @Override
    public ApiReponse<ClientDto> createClient(ClientDto clientDto) {

        Client client = clientMapper.toEntity(clientDto);

        clienteWriteRepo.saveEntity(client);

        log.info("ID CLIENT:{}", client.getId().toString());

        ApiReponse<ClientDto> apiReponse = new ApiReponse<>();
        apiReponse.setData(clientMapper.toDto(client));
        apiReponse.setStatus(HttpResponseStatus.CREATED.code());
        apiReponse.setDescription(HttpResponseStatus.CREATED.reasonPhrase());

        return apiReponse;
    }

    @Override
    public ApiReponse<ClientDto> findClientsById(String id) {

        Client client = clienteReadRepo.searchById(UUID.fromString(id)).orElseThrow(NoSuchElementException::new);

        ApiReponse<ClientDto> apiReponse = new ApiReponse<>();
        apiReponse.setData(clientMapper.toDto(client));
        apiReponse.setStatus(HttpResponseStatus.OK.code());
        apiReponse.setDescription(HttpResponseStatus.OK.reasonPhrase());

        return apiReponse;

    }

    @Override
    public ApiReponse<ClientDto> getClientsSearchParams(String searchParam, int page, int limit) {

        PageResponseDto<ClientDto> pageResponseDto;

        if (searchParam != null) {
            pageResponseDto = clienteReadRepo.getClientsSearchParamPaginated(searchParam, page, limit);
        } else {
            pageResponseDto = clienteReadRepo.getAllClientsPaginated(page, limit);
        }

        ApiReponse<ClientDto> apiReponse = new ApiReponse<>();
        apiReponse.setElements(pageResponseDto.data());
        apiReponse.setCurrentPage(pageResponseDto.currentPage());
        apiReponse.setTotalElements((int)pageResponseDto.totalElements());
        apiReponse.setTotalPages(pageResponseDto.totalPages());
        apiReponse.setStatus(HttpResponseStatus.OK.code());
        apiReponse.setDescription(HttpResponseStatus.OK.reasonPhrase());

        return apiReponse;


    }


}
