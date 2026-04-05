package org.acme.application;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.acme.cross.dto.PageResponseDto;
import org.acme.domain.ClientService;
import org.acme.infrastructure.input.rest.dto.ApiResponse;
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
    public ApiResponse<ClientDto> createClient(ClientDto clientDto) {

        Client client = clientMapper.toEntity(clientDto);

        clienteWriteRepo.saveEntity(client);

        log.info("ID CLIENT:{}", client.getId().toString());

        ApiResponse<ClientDto> apiResponse = new ApiResponse<>();
        apiResponse.setData(clientMapper.toDto(client));
        apiResponse.setStatus(HttpResponseStatus.CREATED.code());
        apiResponse.setDescription(HttpResponseStatus.CREATED.reasonPhrase());

        return apiResponse;
    }

    @Override
    public ApiResponse<ClientDto> findClientsById(String id) {

        Client client = clienteReadRepo.searchById(UUID.fromString(id)).orElseThrow(NoSuchElementException::new);

        ApiResponse<ClientDto> apiResponse = new ApiResponse<>();
        apiResponse.setData(clientMapper.toDto(client));
        apiResponse.setStatus(HttpResponseStatus.OK.code());
        apiResponse.setDescription(HttpResponseStatus.OK.reasonPhrase());

        return apiResponse;

    }

    @Override
    public ApiResponse<ClientDto> getClientsSearchParams(String searchParam, int page, int limit) {

        PageResponseDto<ClientDto> pageResponseDto;

        if (searchParam != null) {
            pageResponseDto = clienteReadRepo.getClientsSearchParamPaginated(searchParam, page, limit);
        } else {
            pageResponseDto = clienteReadRepo.getAllClientsPaginated(page, limit);
        }

        ApiResponse<ClientDto> apiResponse = new ApiResponse<>();
        apiResponse.setElements(pageResponseDto.data());
        apiResponse.setCurrentPage(pageResponseDto.currentPage());
        apiResponse.setTotalElements((int)pageResponseDto.totalElements());
        apiResponse.setTotalPages(pageResponseDto.totalPages());
        apiResponse.setStatus(HttpResponseStatus.OK.code());
        apiResponse.setDescription(HttpResponseStatus.OK.reasonPhrase());

        return apiResponse;


    }


}
