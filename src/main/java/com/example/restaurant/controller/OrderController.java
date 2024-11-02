package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The type Order controller.
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    /**
     * Instantiates a new Order controller.
     *
     * @param orderService the order service
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Gets all order.
     *
     * @return the all order
     */
    @GetMapping("/orderList")
    public ResponseEntity<ResponseDTO> getAllOrder() {
        List<OrderEntity> orderList = orderService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "orderList", orderList));
    }

    /**
     * Handle options response entity.
     *
     * @return the response entity
     */
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }

    /**
     * Gets order.
     *
     * @param orderId the order id
     * @return the order
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseDTO> getOrder(@PathVariable("orderId") String orderId) {
        OrderEntity order = orderService.getById(Integer.parseInt(orderId));
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", order));
    }

    /**
     * Update order response entity.
     *
     * @param id    the id
     * @param order the order
     * @return the response entity
     */
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

    /**
     * Create order response entity.
     *
     * @param order the order
     * @return the response entity
     */
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

    /**
     * Delete order response entity.
     *
     * @param id the id
     * @return the response entity
     */
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
