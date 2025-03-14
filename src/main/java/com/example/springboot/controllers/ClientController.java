package com.example.springboot.controllers;

import com.example.springboot.dtos.ClientRecordDto;
import com.example.springboot.models.Client;
import com.example.springboot.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    public ResponseEntity<Client> saveClient(@RequestBody @Valid ClientRecordDto clientRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(clientRecordDto));
    }

    @GetMapping("/clients")
    public List<Client> getAllClients() {
        List<Client> clientList = clientService.getAllClients();
        if (!clientList.isEmpty()) {
            for (Client client : clientList) {
                UUID clientId = client.getClientId();
                client.add(linkTo(methodOn(ClientController.class).getOneClient(clientId)).withSelfRel());
            }
        }
        return clientList;
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Object> getOneClient(@PathVariable(value = "id") UUID id) {
        var client = clientService.getClientById(id);
        client.add(linkTo(methodOn(ClientController.class).getAllClients()).withRel("Clients List"));
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<Object> updateOneClient(@PathVariable(value = "id") UUID id,
                           @RequestBody @Valid ClientRecordDto clientRecordDto) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateClient(id, clientRecordDto));
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable(value = "id") UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
