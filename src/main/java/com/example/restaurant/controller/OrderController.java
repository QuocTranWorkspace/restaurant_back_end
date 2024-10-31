package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orderList")
    public ResponseEntity<ResponseDTO> getAllOrder() {
        List<OrderEntity> orderList = orderService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "orderList", orderList));
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
