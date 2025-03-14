package com.example.springboot.services;

import com.example.springboot.dtos.OrderRecordDto;
import com.example.springboot.exceptions.ResourceNotFoundException;
import com.example.springboot.models.Order;
import com.example.springboot.models.Product;
import com.example.springboot.repositories.OrderRepository;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ClientService clientService, ProductService productService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.productService = productService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    public Order save(OrderRecordDto orderRecordDto) {
        var client = clientService.getClientById(orderRecordDto.clientId());
        var productList = new HashSet<Product>();
        for (UUID productId : orderRecordDto.productIds()) {
            var product = productService.getProductById(productId);
            productList.add(product);
        }

        var order = new Order();
        order.setClient(client);
        order.setProducts(new HashSet<>(productList));
        order.setOrderedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order updateOrder(UUID id, OrderRecordDto orderRecordDto) {
        var order = getOrderById(id);
        var client = clientService.getClientById(orderRecordDto.clientId());

        var productList = new HashSet<Product>();
        for (UUID productId : orderRecordDto.productIds()) {
            var product = productService.getProductById(productId);
            productList.add(product);
        }

        order.setClient(client);
        order.setProducts(new HashSet<>(productList));
        order.setOrderedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public void deleteOrder(UUID id) {
        var order = getOrderById(id);
        orderRepository.delete(order);
    }
}
