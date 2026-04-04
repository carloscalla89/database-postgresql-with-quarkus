package org.acme.infrastructure.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.infrastructure.input.rest.dto.ClientDto;
import org.acme.infrastructure.output.persistence.entity.Client;

@ApplicationScoped
public class ClientMapper {

    // Entity to Record DTO
    public ClientDto toDto(Client client) {
        if (client == null) {
            return null;
        }

        return ClientDto
                .builder()
                .id(client.getId().toString())
                .firstName(client.firstName)
                .lastName(client.lastName)
                .email(client.email)
                .build();

    }

    // Record DTO to Entity
    public Client toEntity(ClientDto clientDto) {
        if (clientDto == null) {
            return null;
        }
        Client client = new Client();

        // Notice we use the record's accessor methods: id(), firstName(), etc.
        client.firstName = (clientDto.getFirstName());
        client.lastName = (clientDto.getLastName());
        client.email =(clientDto.getEmail());

        return client;
    }
}
