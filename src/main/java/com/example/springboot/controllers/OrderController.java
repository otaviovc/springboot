package com.example.springboot.controllers;

import com.example.springboot.dtos.OrderRecordDto;
import com.example.springboot.models.Order;
import com.example.springboot.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Object> saveOrder(@RequestBody @Valid OrderRecordDto orderRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(orderRecordDto));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();
        if (!orderList.isEmpty()) {
            for (Order order : orderList) {
                UUID id = order.getOrderId();
                order.add(linkTo(methodOn(OrderController.class).getOneOrder(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Object> getOneOrder(@PathVariable(value="id") UUID id) {
        var order = orderService.getOrderById(id);
        order.add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("Orders List"));

        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable(value="id") UUID id,
                                              @RequestBody @Valid OrderRecordDto orderRecordDto) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderRecordDto));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable(value="id") UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
