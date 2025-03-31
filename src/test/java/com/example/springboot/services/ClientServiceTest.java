package com.example.springboot.services;

import com.example.springboot.dtos.ClientRecordDto;
import com.example.springboot.exceptions.ResourceNotFoundException;
import com.example.springboot.models.Client;
import com.example.springboot.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void saveClient_ShouldReturnSavedClient_WhenValidInput() {
        ClientRecordDto clientRecordDto = new ClientRecordDto("John", "john123", "john@example.com");
        Client savedClient = new Client();
        BeanUtils.copyProperties(clientRecordDto, savedClient);

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        Client result = clientService.save(clientRecordDto);

        assertThat(result).isEqualTo(savedClient);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void getAllClients_ShouldReturnListOfClients() {
        Client client1 = new Client();
        Client client2 = new Client();
        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<Client> result = clientService.getAllClients();

        assertThat(result).hasSize(2);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void getClientById_ShouldReturnClient_WhenExists() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        when(clientRepository.findById(eq(id))).thenReturn(Optional.of(client));

        Client result = clientService.getClientById(id);

        assertThat(result).isEqualTo(client);
        verify(clientRepository, times(1)).findById(eq(id));
    }

    @Test
    void getClientById_ShouldThrowResourceNotFoundException_WhenNotExists() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(eq(id))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(id));
        verify(clientRepository, times(1)).findById(eq(id));
    }

    @Test
    void updateClient_ShouldReturnUpdatedClient_WhenValidInput() {
        UUID id = UUID.randomUUID();
        ClientRecordDto clientRecordDto = new ClientRecordDto("John", "john123", "john@example.com");
        Client client = new Client();
        when(clientRepository.findById(eq(id))).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.updateClient(id, clientRecordDto);

        assertThat(result).isEqualTo(client);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void deleteClient_ShouldDeleteClient_WhenExists() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        when(clientRepository.findById(eq(id))).thenReturn(Optional.of(client));

        clientService.deleteClient(id);

        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void deleteClient_ShouldThrowDatabaseConstraintViolationException_WhenDataIntegrityViolation() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        when(clientRepository.findById(eq(id))).thenReturn(Optional.of(client));
        doThrow(DataIntegrityViolationException.class).when(clientRepository).delete(client);

        assertThrows(RuntimeException.class, () -> clientService.deleteClient(id));
        verify(clientRepository, times(1)).delete(client);
    }
}
