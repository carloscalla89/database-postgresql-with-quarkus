package org.acme.infrastructure.input.rest.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.domain.ClientService;
import org.acme.domain.OrderService;
import org.acme.infrastructure.input.rest.dto.ApiReponse;
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

        ApiReponse<ClientDto> apiReponse = clientService.createClient(clientDto);

        return Response.status(apiReponse.getStatus()).entity(apiReponse).build();
    }

    @GET
    @Path("/{clientId}")
    public Response searchClientByID(@PathParam("clientId") String clientId) {

        ApiReponse<ClientDto> apiReponse = clientService.findClientsById(clientId);

        return Response.status(apiReponse.getStatus()).entity(apiReponse).build();

    }

    @GET
    public Response searchClientSearchParam(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("limit") @DefaultValue("10") int limit,
            @QueryParam("searchParam") String searchParam) {

        ApiReponse<ClientDto> apiReponse = clientService.getClientsSearchParams(searchParam, page, limit);

        return Response.status(apiReponse.getStatus()).entity(apiReponse).build();

    }
}
