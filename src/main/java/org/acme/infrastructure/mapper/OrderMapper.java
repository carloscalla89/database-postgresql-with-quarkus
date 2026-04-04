package org.acme.infrastructure.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.infrastructure.input.rest.dto.OrderDto;
import org.acme.infrastructure.output.persistence.entity.Client;
import org.acme.infrastructure.output.persistence.entity.OrderClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderMapper {

    public OrderClient toEntity(OrderDto orderDto, Client client) {
        if (orderDto == null ) {
            return null;
        }

        OrderClient orderClient = new OrderClient();
        orderClient.total = orderDto.getMount();
        orderClient.notes = orderDto.getNotes();
        orderClient.client = client;

        return orderClient;

    }

    public List<OrderDto> toListOrderDto(List<OrderClient> list) {

        return list
                .stream()
                .map(resp ->
                        OrderDto
                                .builder()
                                .id(resp.getId().toString())
                                .mount(resp.total)
                                .notes(resp.notes)
                                .build())
                .toList();
    }
}
