package org.acme.application;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.OrderService;
import org.acme.infrastructure.input.rest.dto.ApiReponse;
import org.acme.infrastructure.input.rest.dto.OrderDto;
import org.acme.infrastructure.mapper.OrderMapper;
import org.acme.infrastructure.output.persistence.entity.Client;
import org.acme.infrastructure.output.persistence.entity.OrderClient;
import org.acme.infrastructure.output.persistence.repository.ClienteReadRepository;
import org.acme.infrastructure.output.persistence.repository.PedidoReadRepository;
import org.acme.infrastructure.output.persistence.repository.PedidoWriteRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderServiceUseCase implements OrderService {

    private final ClienteReadRepository clienteReadRepo;
    private final PedidoWriteRepository pedidoWriteRepo;
    private final PedidoReadRepository pedidoReadRepository;
    private final OrderMapper orderMapper;

    public OrderServiceUseCase(ClienteReadRepository clienteReadRepo, PedidoWriteRepository pedidoWriteRepo,
                               PedidoReadRepository pedidoReadRepository, OrderMapper orderMapper) {
        this.clienteReadRepo = clienteReadRepo;
        this.pedidoWriteRepo = pedidoWriteRepo;
        this.pedidoReadRepository = pedidoReadRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    @Override
    public ApiReponse<OrderDto> createOrder(OrderDto orderDto) {

        Client client = clienteReadRepo.findById(UUID.fromString(orderDto.getClientId()));

        OrderClient orderClient = orderMapper.toEntity(orderDto, client);

        pedidoWriteRepo.saveEntity(orderClient);

        ApiReponse<OrderDto> apiReponse = new ApiReponse<>();
        apiReponse.setData(OrderDto.builder().id(orderClient.getId().toString()).build());
        apiReponse.setStatus(HttpResponseStatus.CREATED.code());
        apiReponse.setDescription(HttpResponseStatus.CREATED.reasonPhrase());

        return apiReponse;
    }

    @Override
    public ApiReponse<List<OrderDto>> listOrdersByClient(String clientId) {

        ApiReponse<List<OrderDto>> apiReponse = new ApiReponse<>();
        apiReponse.setData(pedidoReadRepository.findOrdersByClientIdUsingDto(UUID.fromString(clientId)));
        apiReponse.setStatus(HttpResponseStatus.OK.code());
        apiReponse.setDescription(HttpResponseStatus.OK.reasonPhrase());

        return apiReponse;

    }

}
