package org.acme.domain;

import org.acme.infrastructure.input.rest.dto.ApiResponse;
import org.acme.infrastructure.input.rest.dto.OrderDto;

import java.util.List;

public interface OrderService {

    ApiResponse<OrderDto> createOrder(OrderDto orderDto);
    ApiResponse<List<OrderDto>> listOrdersByClient(String clientId);
}
