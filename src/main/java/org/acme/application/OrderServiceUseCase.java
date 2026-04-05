package org.acme.application;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.OrderService;
import org.acme.infrastructure.input.rest.dto.ApiResponse;
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
    public ApiResponse<OrderDto> createOrder(OrderDto orderDto) {

        Client client = clienteReadRepo.findById(UUID.fromString(orderDto.getClientId()));

        OrderClient orderClient = orderMapper.toEntity(orderDto, client);

        pedidoWriteRepo.saveEntity(orderClient);

        ApiResponse<OrderDto> apiResponse = new ApiResponse<>();
        apiResponse.setData(OrderDto.builder().id(orderClient.getId().toString()).build());
        apiResponse.setStatus(HttpResponseStatus.CREATED.code());
        apiResponse.setDescription(HttpResponseStatus.CREATED.reasonPhrase());

        return apiResponse;
    }

    @Override
    public ApiResponse<List<OrderDto>> listOrdersByClient(String clientId) {

        ApiResponse<List<OrderDto>> apiResponse = new ApiResponse<>();
        apiResponse.setData(pedidoReadRepository.findOrdersByClientIdUsingDto(UUID.fromString(clientId)));
        apiResponse.setStatus(HttpResponseStatus.OK.code());
        apiResponse.setDescription(HttpResponseStatus.OK.reasonPhrase());

        return apiResponse;

    }

}
