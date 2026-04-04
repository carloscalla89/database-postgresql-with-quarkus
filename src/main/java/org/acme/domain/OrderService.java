package org.acme.domain;

import org.acme.infrastructure.input.rest.dto.ApiReponse;
import org.acme.infrastructure.input.rest.dto.OrderDto;

import java.util.List;

public interface OrderService {

    ApiReponse<OrderDto> createOrder(OrderDto orderDto);
    ApiReponse<List<OrderDto>> listOrdersByClient(String clientId);
}
