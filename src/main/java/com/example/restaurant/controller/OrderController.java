package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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


    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseDTO> getOrder(@PathVariable("orderId") String orderId) {
        OrderEntity order = orderService.getById(Integer.parseInt(orderId));
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", order));
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<ResponseDTO> updateOrder(@PathVariable("orderId") String id, @RequestBody OrderEntity order) {
        OrderEntity orderUpdate = orderService.getById(Integer.parseInt(id));
        OrderEntity orderResponse = null;
        if (Objects.nonNull(orderUpdate) && !Objects.isNull(order)) {
            orderResponse = orderService.bindingOrderData(orderUpdate, order);
            orderUpdate.setUpdatedDate(new Date());
            orderService.saveOrUpdate(orderUpdate);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", orderResponse));
    }

    @PostMapping("/addOrder")
    public ResponseEntity<ResponseDTO> createOrder(@RequestBody OrderEntity order) {
        OrderEntity orderEntity = new OrderEntity();
        OrderEntity orderResponse = null;
        if (Objects.nonNull(order)) {
            orderResponse = orderService.bindingOrderData(orderEntity, order);
            orderService.saveOrUpdate(orderEntity);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", orderResponse));
    }

    @PostMapping("/deleteOrder/{orderId}")
    public ResponseEntity<ResponseDTO> deleteOrder(@PathVariable("orderId") String id) {
        OrderEntity order = orderService.getById(Integer.parseInt(id));
        order.setStatus(false);
        orderService.saveOrUpdate(order);
        OrderEntity orderResponse = new OrderEntity();
        orderService.bindingOrderData(orderResponse, order);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted", orderResponse));
    }
}
