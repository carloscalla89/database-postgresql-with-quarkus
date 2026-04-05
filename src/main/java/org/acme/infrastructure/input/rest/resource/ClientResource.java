package org.acme.infrastructure.input.rest.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.domain.ClientService;
import org.acme.infrastructure.input.rest.dto.ApiResponse;
import org.acme.infrastructure.input.rest.dto.ClientDto;

@Slf4j
@Path("/api/v1/client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    private final ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = clientService;
    }

    @POST
    public Response createClient(ClientDto clientDto) {

        ApiResponse<ClientDto> apiResponse = clientService.createClient(clientDto);

        return Response.status(apiResponse.getStatus()).entity(apiResponse).build();
    }

    @GET
    @Path("/{clientId}")
    public Response searchClientByID(@PathParam("clientId") String clientId) {

        ApiResponse<ClientDto> apiResponse = clientService.findClientsById(clientId);

        return Response.status(apiResponse.getStatus()).entity(apiResponse).build();

    }

    @GET
    public Response searchClientSearchParam(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("limit") @DefaultValue("10") int limit,
            @QueryParam("searchParam") String searchParam) {

        ApiResponse<ClientDto> apiResponse = clientService.getClientsSearchParams(searchParam, page, limit);

        return Response.status(apiResponse.getStatus()).entity(apiResponse).build();

    }
}
