package com.example.springboot.services;

import com.example.springboot.dtos.OrderRecordDto;
import com.example.springboot.exceptions.ResourceNotFoundException;
import com.example.springboot.models.Client;
import com.example.springboot.models.Order;
import com.example.springboot.models.Product;
import com.example.springboot.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void getAllOrders_ShouldReturnListOfOrders() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertThat(result).hasSize(2);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void getOrderById_ShouldReturnOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        assertThat(result).isNotNull();
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void getOrderById_ShouldThrowException_WhenNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void saveOrder_ShouldReturnSavedOrder() {
        UUID clientId = UUID.randomUUID();
        OrderRecordDto orderRecordDto = new OrderRecordDto(clientId, Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        Client client = new Client();
        Set<Product> products = new HashSet<>(Arrays.asList(new Product(), new Product()));
        Order order = new Order();
        order.setOrderedAt(LocalDateTime.now());

        when(clientService.getClientById(clientId)).thenReturn(client);
        for (Product product : products) {
            when(productService.getProductById(any())).thenReturn(product);
        }
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.save(orderRecordDto);

        assertThat(result).isNotNull();
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrder() {
        UUID id = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        OrderRecordDto orderRecordDto = new OrderRecordDto(clientId, Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        Order existingOrder = new Order();
        when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        Order result = orderService.updateOrder(id, orderRecordDto);

        assertThat(result).isNotNull();
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void deleteOrder_ShouldRemoveOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).delete(order);
    }
}
