package com.example.springboot.services;

import com.example.springboot.dtos.ClientRecordDto;
import com.example.springboot.exceptions.DatabaseConstraintViolationException;
import com.example.springboot.exceptions.ResourceNotFoundException;
import com.example.springboot.models.Client;
import com.example.springboot.repositories.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client save(ClientRecordDto clientRecordDto) {
        var client = new Client();
        BeanUtils.copyProperties(clientRecordDto, client);
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(UUID id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found: " + id));
    }

    public Client updateClient(UUID id, ClientRecordDto clientRecordDto) {
        var client = getClientById(id);
        BeanUtils.copyProperties(clientRecordDto, client);
        return clientRepository.save(client);
    }

    public void deleteClient(UUID id) {
        try {
            var client = getClientById(id);
            clientRepository.delete(client);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseConstraintViolationException("Cannot delete client because there are orders associated with it.");
        }
    }
}
