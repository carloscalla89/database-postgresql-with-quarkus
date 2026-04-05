package org.acme.infrastructure.input.rest.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.domain.OrderService;
import org.acme.infrastructure.input.rest.dto.ApiResponse;
import org.acme.infrastructure.input.rest.dto.OrderDto;

import java.util.List;

@Slf4j
@Path("/api/v1/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @POST
    public Response createOrder(OrderDto orderDto) {
        ApiResponse<OrderDto> apiResponse = orderService.createOrder(orderDto);

        return Response.status(apiResponse.getStatus()).entity(apiResponse).build();
    }

    @GET
    @Path("/client/{clientId}")
    public Response getOrdersByClient(@PathParam("clientId") String clientId) {
        ApiResponse<List<OrderDto>> apiResponse = orderService.listOrdersByClient(clientId);

        return Response.status(apiResponse.getStatus()).entity(apiResponse).build();
    }

}
